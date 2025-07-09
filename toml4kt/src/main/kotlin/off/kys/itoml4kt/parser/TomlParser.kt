package off.kys.itoml4kt.parser

import off.kys.itoml4kt.parser.util.Empty
import off.kys.itoml4kt.parser.util.TomlParsingException
import off.kys.itoml4kt.parser.util.TomlValue
import java.io.File
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.readText

/**
 * <h6>TomlParser</h6>
 */
class TomlParser {
    private val tomlContent: String

    private val rootTable: TomlValue.TableValue

    private val tables: MutableMap<String, TomlValue.TableValue>
    private var currentTable: TomlValue.TableValue
    private var currentTableName = String.Empty

    constructor(tomlContent: String) {
        this.tomlContent = tomlContent.removeTomlComments()
        this.rootTable = TomlValue.TableValue()
        this.tables = mutableMapOf()
        this.currentTable = rootTable
        tables[String.Empty] = rootTable
    }

    constructor(tomlFile: File) {
        require(tomlFile.extension == "toml") { "Unsupported file: ${tomlFile.extension}" }
        this.tomlContent = tomlFile.readText().removeTomlComments()
        this.rootTable = TomlValue.TableValue()
        this.tables = mutableMapOf()
        this.currentTable = rootTable
        tables[String.Empty] = rootTable
    }

    constructor(tomlPath: Path) {
        require(tomlPath.extension == "toml") { "Unsupported file: ${tomlPath.extension}" }
        this.tomlContent = tomlPath.readText().removeTomlComments()
        this.rootTable = TomlValue.TableValue()
        this.tables = mutableMapOf()
        this.currentTable = rootTable
        tables[String.Empty] = rootTable
    }

    fun parse(): TomlValue.TableValue {
        val lines = tomlContent.lineSequence()
            .map { it.trim() }
            .mapIndexed { index, line -> Pair(index + 1, line) }
            .filter { (_, line) -> line.isNotEmpty() && !line.startsWith("#") }

        for ((lineNumber, line) in lines) {
            try {
                when {
                    line.startsWith("[[") && line.endsWith("]]") -> handleArrayTable(line, lineNumber)
                    line.startsWith("[") && line.endsWith("]") -> handleTableDefinition(line, lineNumber)
                    line.contains('=') -> handleKeyValuePair(line, lineNumber)
                    else -> throw TomlParsingException("Invalid syntax", lineNumber)
                }
            } catch (e: TomlParsingException) {
                throw TomlParsingException(e.message ?: "Unknown error", lineNumber)
            }
        }
        return rootTable
    }

    private fun handleTableDefinition(line: String, lineNumber: Int) {
        val tableName = line.substring(1, line.length - 1).trim()
        validateTableName(tableName, lineNumber)
        currentTableName = tableName
        currentTable = getOrCreateTable(tableName.split('.'), lineNumber)
    }

    private fun handleArrayTable(line: String, lineNumber: Int) {
        val tableName = line.substring(2, line.length - 2).trim()
        validateTableName(tableName, lineNumber)
        currentTableName = tableName
        val parentTables = tableName.split('.')
        val arrayTable = TomlValue.TableValue()

        val array = getOrCreateArray(parentTables, lineNumber)
        (array as? TomlValue.ArrayValue)?.values?.add(arrayTable)
            ?: throw TomlParsingException("Array table defined in non-array context", lineNumber)

        currentTable = arrayTable
    }

    private fun getOrCreateArray(keys: List<String>, lineNumber: Int): TomlValue {
        var current: TomlValue = rootTable
        for (key in keys) {
            current = when (current) {
                is TomlValue.TableValue -> {
                    current.entries.getOrPut(key) { TomlValue.TableValue() }
                }

                is TomlValue.ArrayValue -> {
                    current.values.lastOrNull() ?: throw TomlParsingException("Invalid array context", lineNumber)
                }

                else -> throw TomlParsingException("Invalid context for array creation", lineNumber)
            }
        }

        return if (current is TomlValue.ArrayValue) {
            current
        } else {
            val newArray = TomlValue.ArrayValue(mutableListOf())
            getParentTable(keys, lineNumber).entries[keys.last()] = newArray
            newArray
        }
    }

    private fun getOrCreateTable(keys: List<String>, lineNumber: Int): TomlValue.TableValue {
        var current: TomlValue = rootTable
        for (key in keys) {
            current = when (current) {
                is TomlValue.TableValue -> current.entries.getOrPut(key) { TomlValue.TableValue() }
                else -> throw TomlParsingException("Invalid table path", lineNumber)
            }
        }
        return current as? TomlValue.TableValue
            ?: throw TomlParsingException("Invalid table definition", lineNumber)
    }

    private fun getParentTable(keys: List<String>, lineNumber: Int): TomlValue.TableValue {
        return if (keys.size > 1) {
            getOrCreateTable(keys.dropLast(1), lineNumber)
        } else {
            rootTable
        }
    }

    private fun validateTableName(name: String, lineNumber: Int) {
        if (name.isEmpty()) throw TomlParsingException("Empty table name", lineNumber)
        if (name.startsWith('.') || name.endsWith('.')) {
            throw TomlParsingException("Invalid table name: starts or ends with dot", lineNumber)
        }
    }

    private fun handleKeyValuePair(line: String, lineNumber: Int) {
        val (keyPart, valuePart) = splitKeyValue(line, lineNumber)
        val keys = parseKey(keyPart, lineNumber)
        val value = parseValue(valuePart, lineNumber)
        insertValue(keys, value, lineNumber)
    }

    private fun splitKeyValue(line: String, lineNumber: Int): Pair<String, String> {
        val equalsIndex = line.indexOf('=')
        if (equalsIndex == -1) throw TomlParsingException("Missing '=' in key-value pair", lineNumber)

        val key = line.substring(0, equalsIndex).trim()
        val value = line.substring(equalsIndex + 1).trim()
        return Pair(key, value)
    }

    private fun parseKey(key: String, lineNumber: Int): List<String> {
        return key.split('.').map { part ->
            when {
                part.isEmpty() -> throw TomlParsingException("Empty key part", lineNumber)
                part.startsWith('"') && part.endsWith('"') -> part.removeSurrounding("\"")
                part.startsWith('\'') && part.endsWith('\'') -> part.removeSurrounding("'")
                part.matches(Regex("[A-Za-z0-9_-]+")) -> part
                else -> throw TomlParsingException("Invalid key format: $part", lineNumber)
            }
        }
    }

    private fun parseValue(value: String, lineNumber: Int): TomlValue {
        return when {
            value.startsWith('"') -> parseString(value, lineNumber)
            value.startsWith('\'') -> parseString(value, lineNumber)
            value.startsWith('[') -> parseArray(value, lineNumber)
            value.startsWith('{') -> parseInlineTable(value)
            value.matches(Regex("[-+]?\\d+")) -> TomlValue.IntegerValue(value.toInt())
            value.matches(Regex("[-+]?\\d+\\.\\d+([eE][-+]?\\d+)?")) -> TomlValue.FloatValue(value.toFloat())
            value.equals("true", ignoreCase = true) -> TomlValue.BooleanValue(true)
            value.equals("false", ignoreCase = true) -> TomlValue.BooleanValue(false)
            else -> throw TomlParsingException("Invalid value format: $value", lineNumber)
        }
    }

    private fun parseString(value: String, lineNumber: Int): TomlValue.StringValue {
        return try {
            val trimmed = value.trim()
            when {
                trimmed.startsWith("\"\"\"") -> TomlValue.StringValue(trimmed.removeSurrounding("\"\"\"").trimIndent())
                trimmed.startsWith("'''") -> TomlValue.StringValue(trimmed.removeSurrounding("'''").trimIndent())
                trimmed.startsWith("\"") -> TomlValue.StringValue(trimmed.removeSurrounding("\""))
                trimmed.startsWith("'") -> TomlValue.StringValue(trimmed.removeSurrounding("'"))
                else -> throw TomlParsingException("Invalid string format", lineNumber)
            }
        } catch (e: Exception) {
            throw TomlParsingException("Error parsing string: ${e.message}", lineNumber)
        }
    }

    private fun parseArray(value: String, lineNumber: Int): TomlValue.ArrayValue {
        val cleaned = value.trim().removeSurrounding("[", "]").trim()
        if (cleaned.isEmpty()) return TomlValue.ArrayValue(mutableListOf())

        val elements = mutableListOf<TomlValue>()
        val currentElement = StringBuilder()
        var inString = false
        var quoteChar: Char? = null
        var depth = 0

        for (char in cleaned) {
            when {
                char == ',' && !inString && depth == 0 -> {
                    elements.add(parseValue(currentElement.toString().trim(), lineNumber))
                    currentElement.clear()
                }

                char == '"' || char == '\'' -> {
                    if (inString && quoteChar == char) {
                        inString = false
                        quoteChar = null
                    } else if (!inString) {
                        inString = true
                        quoteChar = char
                    }
                    currentElement.append(char)
                }

                char == '[' || char == '{' -> {
                    depth++
                    currentElement.append(char)
                }

                char == ']' || char == '}' -> {
                    depth--
                    currentElement.append(char)
                }

                else -> currentElement.append(char)
            }
        }

        if (currentElement.isNotEmpty()) {
            elements.add(parseValue(currentElement.toString().trim(), lineNumber))
        }

        return TomlValue.ArrayValue(elements)
    }

    private fun parseInlineTable(value: String): TomlValue.TableValue {
        val table = TomlValue.TableValue()
        val cleaned = value.trim().removeSurrounding("{", "}").trim()
        if (cleaned.isEmpty()) return table

        val parser = TomlParser(cleaned)
        val parsed = parser.parse()
        table.entries.putAll(parsed.entries)
        return table
    }

    private fun insertValue(keys: List<String>, value: TomlValue, lineNumber: Int) {
        var current: TomlValue.TableValue = currentTable
        for (i in 0 until keys.size - 1) {
            val key = keys[i]
            val next = current.entries[key]
            current = when (next) {
                null -> {
                    val newTable = TomlValue.TableValue()
                    current.entries[key] = newTable
                    newTable
                }

                is TomlValue.TableValue -> next
                else -> throw TomlParsingException("Cannot create table in non-table context", lineNumber)
            }
        }

        val lastKey = keys.last()
        if (currentTableName.isNotEmpty() && currentTableName == keys.joinToString(".")) {
            // This is an array table, so we allow duplicate keys
            current.entries[lastKey] = value
        } else {
            // This is a regular table, so we check for duplicate keys
            if (current.entries.containsKey(lastKey)) {
                throw TomlParsingException("Duplicate key: $lastKey", lineNumber)
            }
            current.entries[lastKey] = value
        }
    }

    private fun String.removeTomlComments() = lineSequence()
        .map { it.substringBefore("#").trimEnd() } // Remove everything after #
        .filter { it.isNotBlank() } // Remove empty lines
        .joinToString("\n")
}
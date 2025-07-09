package off.kys.itoml4kt.parser.extension

import off.kys.itoml4kt.parser.util.TomlValue

fun TomlValue.TableValue.getString(key: String): String =
    (entries[key] as? TomlValue.StringValue)!!.value

fun TomlValue.TableValue.getStringOrNull(key: String): String? =
    (entries[key] as? TomlValue.StringValue)?.value

fun TomlValue.TableValue.getInt(key: String): Int =
    (entries[key] as? TomlValue.IntegerValue)!!.value

fun TomlValue.TableValue.getIntOrNull(key: String): Int? =
    (entries[key] as? TomlValue.IntegerValue)?.value

fun TomlValue.TableValue.getFloat(key: String): Float =
    (entries[key] as? TomlValue.FloatValue)!!.value

fun TomlValue.TableValue.getFloatOrNull(key: String): Float? =
    (entries[key] as? TomlValue.FloatValue)?.value

fun TomlValue.TableValue.getBoolean(key: String): Boolean =
    (entries[key] as? TomlValue.BooleanValue)!!.value

fun TomlValue.TableValue.getBooleanOrNull(key: String): Boolean? =
    (entries[key] as? TomlValue.BooleanValue)?.value

fun TomlValue.TableValue.getArray(key: String): List<TomlValue> =
    (entries[key] as? TomlValue.ArrayValue)!!.values

fun TomlValue.TableValue.getArrayOrNull(key: String): List<TomlValue>? =
    (entries[key] as? TomlValue.ArrayValue)?.values

fun TomlValue.TableValue.getTable(key: String): TomlValue.TableValue =
    (entries[key] as? TomlValue.TableValue)!!

fun TomlValue.TableValue.getTableOrNull(key: String): TomlValue.TableValue? =
    (entries[key] as? TomlValue.TableValue)

fun TomlValue.TableValue.getOrCreateTable(key: String): TomlValue.TableValue =
    (entries[key] as? TomlValue.TableValue) ?: TomlValue.TableValue().also { entries[key] = it }

fun TomlValue.TableValue.getStringAt(key: String): String {
    val keys = key.split(".") // Split the key into parts
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTable(k)
    }

    // Get the final value using the last key
    return currentTable.getString(keys.last())
}

fun TomlValue.TableValue.getStringAtOrNull(key: String): String? {
    val keys = key.split(".") // Split the key into parts
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTableOrNull(k) ?: return null
    }

    // Get the final value using the last key
    return currentTable.getStringOrNull(keys.last())
}

fun TomlValue.TableValue.getIntAt(key: String): Int {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTable(k)
    }

    // Get the final value using the last key
    return currentTable.getInt(keys.last())
}

fun TomlValue.TableValue.getIntAtOrNull(key: String): Int? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTableOrNull(k) ?: return null
    }

    // Get the final value using the last key
    return currentTable.getIntOrNull(keys.last())
}

fun TomlValue.TableValue.getFloatAt(key: String): Float {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTable(k)
    }

    // Get the final value using the last key
    return currentTable.getFloat(keys.last())
}

fun TomlValue.TableValue.getFloatAtOrNull(key: String): Float? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTableOrNull(k) ?: return null
    }

    // Get the final value using the last key
    return currentTable.getFloatOrNull(keys.last())
}

fun TomlValue.TableValue.getBooleanAt(key: String): Boolean {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTable(k)
    }

    // Get the final value using the last key
    return currentTable.getBoolean(keys.last())
}

fun TomlValue.TableValue.getBooleanAtOrNull(key: String): Boolean? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTableOrNull(k) ?: return null
    }

    // Get the final value using the last key
    return currentTable.getBooleanOrNull(keys.last())
}

fun TomlValue.TableValue.getArrayAt(key: String): List<TomlValue> {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTable(k)
    }

    // Get the final array using the last key
    return currentTable.getArray(keys.last())
}

fun TomlValue.TableValue.getArrayAtOrNull(key: String): List<TomlValue>? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    // Traverse through nested tables
    for (k in keys.dropLast(1)) {
        currentTable = currentTable.getTableOrNull(k) ?: return null
    }

    // Get the final array using the last key
    return currentTable.getArrayOrNull(keys.last())
}

fun TomlValue.TableValue.getStringAt(index: Int, key: String): String {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k]
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue
                    ?: throw NoSuchElementException("Index $index out of bounds for array at '$k'")
            }

            is TomlValue.TableValue -> value
            else -> throw IllegalArgumentException("Invalid path: $key")
        }
    }

    return currentTable.getString(keys.last())
}

fun TomlValue.TableValue.getStringAtOrNull(index: Int, key: String): String? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k] ?: return null
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue ?: return null
            }

            is TomlValue.TableValue -> value
            else -> return null
        }
    }

    return currentTable.getStringOrNull(keys.last())
}

fun TomlValue.TableValue.getIntAt(index: Int, key: String): Int {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k]
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue
                    ?: throw NoSuchElementException("Index $index out of bounds for array at '$k'")
            }

            is TomlValue.TableValue -> value
            else -> throw IllegalArgumentException("Invalid path: $key")
        }
    }

    return currentTable.getInt(keys.last())
}

fun TomlValue.TableValue.getIntAtOrNull(index: Int, key: String): Int? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k] ?: return null
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue ?: return null
            }

            is TomlValue.TableValue -> value
            else -> return null
        }
    }

    return currentTable.getIntOrNull(keys.last())
}

fun TomlValue.TableValue.getBooleanAt(index: Int, key: String): Boolean {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k]
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue
                    ?: throw NoSuchElementException("Index $index out of bounds for array at '$k'")
            }

            is TomlValue.TableValue -> value
            else -> throw IllegalArgumentException("Invalid path: $key")
        }
    }

    return currentTable.getBoolean(keys.last())
}

fun TomlValue.TableValue.getBooleanAtOrNull(index: Int, key: String): Boolean? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k] ?: return null
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue ?: return null
            }

            is TomlValue.TableValue -> value
            else -> return null
        }
    }

    return currentTable.getBooleanOrNull(keys.last())
}

fun TomlValue.TableValue.getArrayAt(index: Int, key: String): List<TomlValue> {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k]
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue
                    ?: throw NoSuchElementException("Index $index out of bounds for array at '$k'")
            }

            is TomlValue.TableValue -> value
            else -> throw IllegalArgumentException("Invalid path: $key")
        }
    }

    return currentTable.getArray(keys.last())
}

fun TomlValue.TableValue.getArrayAtOrNull(index: Int, key: String): List<TomlValue>? {
    val keys = key.split(".")
    var currentTable: TomlValue.TableValue = this

    for (k in keys.dropLast(1)) {
        val value = currentTable.entries[k] ?: return null
        currentTable = when (value) {
            is TomlValue.ArrayValue -> {
                value.values.getOrNull(index) as? TomlValue.TableValue ?: return null
            }

            is TomlValue.TableValue -> value
            else -> return null
        }
    }

    return currentTable.getArrayOrNull(keys.last())
}
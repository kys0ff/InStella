package off.kys.itoml4kt.parser.extension

import off.kys.itoml4kt.parser.util.TomlParsingException

fun catch(block: () -> Unit) = try {
    block()
} catch (e: TomlParsingException) {
    println("TOML Parsing Error: ${e.message}")
}
package off.kys.itoml4kt.parser.util

class TomlParsingException(message: String, line: Int? = null) :
    Exception(if (line != null) "Line $line: $message" else message)
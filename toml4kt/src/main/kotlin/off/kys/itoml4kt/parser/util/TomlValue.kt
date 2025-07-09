package off.kys.itoml4kt.parser.util

sealed class TomlValue {
    data class StringValue(val value: String) : TomlValue()
    data class IntegerValue(val value: Int) : TomlValue()
    data class FloatValue(val value: Float) : TomlValue()
    data class BooleanValue(val value: Boolean) : TomlValue()
    data class ArrayValue(val values: MutableList<TomlValue>) : TomlValue()
    data class TableValue(val entries: MutableMap<String, TomlValue> = mutableMapOf()) : TomlValue() {
        fun TableValue.toFormattedString(): String =
            entries.entries.joinToString(prefix = "{ ", postfix = " }", separator = ", ") { (key, value) ->
                "$key=$value"
            }

        fun toMap(): Map<String, Any?> = entries.mapValues {
            when (val value = it.value) {
                is StringValue -> value.value
                is IntegerValue -> value.value
                is FloatValue -> value.value
                is BooleanValue -> value.value
                is ArrayValue -> value.values.map { element -> unwrap(element) }
                is TableValue -> value.toMap()
            }
        }

        private fun unwrap(value: TomlValue): Any = when (value) {
            is StringValue -> value.value
            is IntegerValue -> value.value
            is FloatValue -> value.value
            is BooleanValue -> value.value
            is ArrayValue -> value.values.map { unwrap(it) }
            is TableValue -> value.toMap()
        }
    }

    fun TomlValue.toDisplayString(): String = when (this) {
        is StringValue -> value
        is IntegerValue -> value.toString()
        is FloatValue -> value.toString()
        is BooleanValue -> value.toString()
        is ArrayValue -> values.joinToString(prefix = "[", postfix = "]") { it.toString() }
        is TableValue -> toFormattedString()
    }

}
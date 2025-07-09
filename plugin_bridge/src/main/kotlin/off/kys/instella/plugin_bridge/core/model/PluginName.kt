package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginName(val value: String) {
    operator fun invoke() = value
}

fun String.toPluginName() = PluginName(this)
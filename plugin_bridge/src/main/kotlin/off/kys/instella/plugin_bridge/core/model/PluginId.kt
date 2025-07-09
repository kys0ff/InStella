package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginId(val value: String) {
    operator fun invoke() = value
}

fun String.toPluginId() = PluginId(this)
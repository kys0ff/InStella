package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginDescription(val value: String) {
    operator fun invoke() = value
}
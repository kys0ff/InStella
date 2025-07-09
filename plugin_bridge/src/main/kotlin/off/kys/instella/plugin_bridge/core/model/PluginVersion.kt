package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginVersion(val value: String) {
    operator fun invoke() = value
}
package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginAuthor(val value: String) {
    operator fun invoke() = value
}
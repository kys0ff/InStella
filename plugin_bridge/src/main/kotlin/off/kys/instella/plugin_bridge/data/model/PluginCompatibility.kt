package off.kys.instella.plugin_bridge.data.model

data class PluginCompatibility(
    val appVersion: String,
    val distributions: List<PluginDistribution>,
)
package off.kys.instella.plugin_bridge.core.model

@JvmInline
value class PluginLicense(val value: String) {
    operator fun invoke() = value
}

fun String.toPluginLicense() = PluginLicense(this)
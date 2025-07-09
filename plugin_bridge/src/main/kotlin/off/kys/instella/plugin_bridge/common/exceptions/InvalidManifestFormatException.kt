package off.kys.instella.plugin_bridge.common.exceptions

class InvalidManifestFormatException(details: String) :
    PluginManifestException("Invalid manifest TOML format:\n$details")
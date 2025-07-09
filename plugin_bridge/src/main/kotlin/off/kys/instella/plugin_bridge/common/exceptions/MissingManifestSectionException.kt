package off.kys.instella.plugin_bridge.common.exceptions

class MissingManifestSectionException(section: String) :
    PluginManifestException("Missing required section: [$section]")
package off.kys.instella.plugin_bridge.common.exceptions

class MissingManifestFieldException(field: String) :
    PluginManifestException("Missing required field: $field")
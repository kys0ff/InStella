package off.kys.instella.plugin_bridge.common.exceptions

class InvalidManifestFieldValueException(field: String, value: String) :
    PluginManifestException("Invalid value '$value' for field: $field")
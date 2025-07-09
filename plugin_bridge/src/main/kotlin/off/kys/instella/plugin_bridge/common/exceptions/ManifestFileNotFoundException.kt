package off.kys.instella.plugin_bridge.common.exceptions

import java.io.File

class ManifestFileNotFoundException(file: File) :
    PluginManifestException("Manifest file not found: ${file.absolutePath}")
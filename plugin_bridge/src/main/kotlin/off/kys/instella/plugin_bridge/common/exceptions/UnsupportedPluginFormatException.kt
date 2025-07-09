package off.kys.instella.plugin_bridge.common.exceptions

import java.io.File

class UnsupportedPluginFormatException(pluginFile: File) : Exception(
    buildString {
        appendLine("Unsupported plugin format detected for: '${pluginFile.absolutePath}'.")
        appendLine("Only `.par` files are accepted. Detected extension: '.${pluginFile.extension}'.")
        append("Please verify the plugin file is correctly named and in the right format.")
    }
)
package off.kys.instella.plugin_bridge.data.model

import java.io.File

data class PluginEntryPoints(
    val getInfoSrc: File,
    val installSrc: File,
    val uninstallSrc: File,
)
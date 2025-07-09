package off.kys.instella.plugin_bridge.data.model

import off.kys.instella.plugin_bridge.core.model.*
import java.io.File

data class PluginSection(
    val id: PluginId,
    val name: PluginName,
    val icon: File?,
    val version: PluginVersion,
    val description: PluginDescription,
    val author: PluginAuthor,
    val license: PluginLicense,
)
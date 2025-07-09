package kys.off.instella.plugins

import kys.off.instella.utils.extensions.mkdirs
import off.kys.instella.plugin_bridge.core.model.PluginId
import off.kys.instella.shared_env.SharedEnv
import java.io.File
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

object PluginsManager {

    fun install(plugin: File, pluginId: PluginId): Boolean = SharedEnv.dataDir.resolve("plugins").apply {
        if (!exists()) mkdirs()
    }.resolve(pluginId()).apply {
        if (!exists()) plugin.copyTo(this.toFile())
    }.exists()

    fun uninstall(pluginId: PluginId): Boolean = SharedEnv.dataDir.resolve(pluginId()).deleteIfExists()

}
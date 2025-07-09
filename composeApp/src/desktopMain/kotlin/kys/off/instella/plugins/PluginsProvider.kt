package kys.off.instella.plugins

import kys.off.instella.utils.plugin.Plugin
import off.kys.instella.shared_env.SharedEnv
import kotlin.io.path.listDirectoryEntries

class PluginsProvider {

    /**
     * Return `true` if any plugin supports the given format (e.g., "tar.gz", "AppImage").
     */
    fun getPluginIfSupports(format: String): Plugin? {
        SharedEnv.dataDir.resolve("plugins").apply {
            listDirectoryEntries()
        }
        // TODO: Add handler to get the supported pkg types of plugins
        return TODO()
    }

}
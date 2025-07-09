package off.kys.instella.plugin_bridge.data.model

import off.kys.instella.plugin_bridge.data.model.util.OptionalPermissions
import off.kys.instella.plugin_bridge.data.model.util.RequiredPermissions

data class PluginManifest(
    val section: PluginSection,
    val entryPoints: PluginEntryPoints,
    val permissions: Pair<RequiredPermissions, OptionalPermissions>,
    val compatibility: PluginCompatibility,
    val declaredDeprecationLevel: PluginDeprecation? = null,
)
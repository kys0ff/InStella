package off.kys.instella.plugin_bridge.data.model

import java.io.File

data class PluginStructure(
    val manifest: PluginManifest,
    val pluginFile: File, // Root folder of the plugin (required for resolving sources)
    val status: PluginStatus = PluginStatus.ENABLED, // Current runtime load status
    val permissionsGranted: Set<PluginPermission> = emptySet(), // Resolved/accepted permissions
    val effectiveDeprecationLevel: PluginDeprecation = PluginDeprecation.NONE,
    val signatureValid: Boolean = false, // Signature check
    val trustedSource: Boolean = false,
    val originRepo: String? = null,
)
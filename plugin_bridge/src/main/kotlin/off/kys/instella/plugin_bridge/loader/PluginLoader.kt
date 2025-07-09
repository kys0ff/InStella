package off.kys.instella.plugin_bridge.loader

import off.kys.instella.plugin_bridge.common.exceptions.*
import off.kys.instella.plugin_bridge.core.model.*
import off.kys.instella.plugin_bridge.data.model.*
import off.kys.instella.plugin_bridge.data.model.util.OptionalPermissions
import off.kys.instella.plugin_bridge.data.model.util.RequiredPermissions
import off.kys.itoml4kt.parser.TomlParser
import off.kys.itoml4kt.parser.extension.getArrayOrNull
import off.kys.itoml4kt.parser.extension.getStringAtOrNull
import off.kys.itoml4kt.parser.extension.getStringOrNull
import off.kys.itoml4kt.parser.extension.getTableOrNull
import off.kys.itoml4kt.parser.util.TomlValue
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
import kotlin.io.path.createTempFile

class PluginLoader(
    private val pluginFile: File,
) {

    private var pluginTemp: File? = null

    init {
        if (pluginFile.extension != "par")
            throw UnsupportedPluginFormatException(pluginFile)
    }

    private fun load(): PluginStructure {
        val pluginPar = ZipFile(pluginFile)
        val plugin = getPluginTempDir()

        fun getManifestFile() = File(plugin, "manifest.toml")
        fun getParsedManifest() = TomlParser(getManifestFile()).parse()

        pluginPar.use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (entry.isDirectory) {
                    plugin.mkdir()
                } else {
                    plugin.parentFile?.mkdirs()
                    zip.getInputStream(entry).use { input ->
                        FileOutputStream(plugin).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }

        if (getManifestFile().isFile.not() or getManifestFile().exists().not())
            throw ManifestFileNotFoundException(getManifestFile())

        val parsedManifest = getParsedManifest()

        return PluginStructure(
            manifest = PluginManifest(
                section = parsePluginSection(parsedManifest),
                entryPoints = parseEntryPoints(parsedManifest),
                permissions = parsePermissions(parsedManifest),
                compatibility = parseCompat(parsedManifest),
                declaredDeprecationLevel = parseDeprecation(parsedManifest)
            ),
            pluginFile = pluginFile,
            status = PluginStatus.ENABLED,
            permissionsGranted = emptySet(),
            effectiveDeprecationLevel = PluginDeprecation.NONE,
            signatureValid = true,
            trustedSource = true,
            originRepo = "repo:official",
        )
    }

    private fun parsePluginSection(manifest: TomlValue.TableValue): PluginSection {
        val parsedIcon = manifest.getStringAtOrNull("plugin.icon")
        val pluginIcon = if (parsedIcon != null) File(
            parsedIcon.replace(
                "%assets%",
                File(getPluginTempDir(), "assets").absolutePath
            )
        ) else null

        return PluginSection(
            id = manifest.getStringAtOrNull("plugin.id")?.toPluginId() ?: throw MissingManifestFieldException("id"),
            name = manifest.getStringAtOrNull("plugin.name")?.toPluginName()
                ?: throw MissingManifestFieldException("name"),
            icon = pluginIcon,
            version = manifest.getStringAtOrNull("plugin.version")?.toPluginVersion()
                ?: throw MissingManifestFieldException("version"),
            description = manifest.getStringAtOrNull("plugin.description")?.toPluginDescription()
                ?: throw MissingManifestFieldException("description"),
            author = manifest.getStringAtOrNull("plugin.author")?.toPluginAuthor()
                ?: throw MissingManifestFieldException("author"),
            license = (manifest.getStringAtOrNull("plugin.license") ?: "none").toPluginLicense(),
        )
    }

    private fun parseEntryPoints(manifest: TomlValue.TableValue): PluginEntryPoints {
        val table = manifest.getTableOrNull("entry_points") ?: throw MissingManifestSectionException("entry_points")

        return PluginEntryPoints(
            getInfoSrc = getPluginTempDir(
                table.getStringAtOrNull("get_info") ?: throw MissingManifestFieldException("entry_points.get_info")
            ),
            installSrc = getPluginTempDir(
                table.getStringAtOrNull("install") ?: throw MissingManifestFieldException("entry_points.install")
            ),
            uninstallSrc = getPluginTempDir(
                table.getStringAtOrNull("uninstall") ?: throw MissingManifestFieldException(
                    "entry_points.uninstall"
                )
            )
        )
    }

    private fun parsePermissions(manifest: TomlValue.TableValue): Pair<RequiredPermissions, OptionalPermissions> {
        val table = manifest.getTableOrNull("permissions") ?: throw MissingManifestSectionException("permissions")

        return Pair(
            first = table.getArrayOrNull("required")?.toList()?.map { PluginPermission.valueOf(it.toString()) }
                ?: emptyList(),
            second = table.getArrayOrNull("optional")?.toList()?.map { PluginPermission.valueOf(it.toString()) }
                ?: emptyList()
        )
    }

    private fun parseCompat(manifest: TomlValue.TableValue): PluginCompatibility {
        val table = manifest.getTableOrNull("compat") ?: throw MissingManifestSectionException("compat")

        return PluginCompatibility(
            appVersion = table.getStringOrNull("app_version")
                ?: throw MissingManifestFieldException("compat.app_version"),
            distributions = table.getArrayOrNull("distros")?.toList()?.map { PluginDistribution.valueOf(it.toString()) }
                ?: emptyList()
        )
    }

    private fun parseDeprecation(manifest: TomlValue.TableValue): PluginDeprecation {
        val table = manifest.getTableOrNull("deprecation") ?: throw MissingManifestSectionException("deprecation")
        val levelStr = table.getStringOrNull("level")?.lowercase() ?: "none"
        val level = when (levelStr) {
            "none" -> PluginDeprecation.NONE
            "legacy" -> PluginDeprecation.LEGACY
            "obsolete" -> PluginDeprecation.OBSOLETE
            "blocked" -> PluginDeprecation.BLOCKED
            else -> throw InvalidManifestFieldValueException("deprecation.level", levelStr)
        }
        return level
    }

    private fun getPluginTempDir(child: String? = null): File {
        if (pluginTemp == null) {
            val tempDir = createTempFile(
                prefix = pluginFile.name.replace("-", "_").replace(" ", "_") + "_"
            ).toFile().apply { delete() }

            val dir = File(tempDir.parentFile, tempDir.name).apply {
                mkdirs()
                deleteOnExit()
            }

            pluginTemp = dir
        }

        return if (child != null) File(pluginTemp, child) else pluginTemp!!
    }

}
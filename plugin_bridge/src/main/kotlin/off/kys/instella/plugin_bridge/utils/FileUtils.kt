package off.kys.instella.plugin_bridge.utils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object FileUtils {

    fun getAppConfigDir(appName: String = "instella"): Path {
        val homeDir = System.getProperty("user.home")
        val configDir = Paths.get(homeDir, ".config", appName)

        // Ensure it exists
        if (!Files.exists(configDir))
            Files.createDirectories(configDir)

        return configDir
    }

}

package off.kys.instella.shared_env

import java.nio.file.Path

/**
 * ConfigResolver provides utilities for resolving and loading application configuration from files
 * and environment variables. It is mainly used to parse configuration data required by the application
 * at runtime.
 *
 * Functions:
 * - `loadAppConfig()`: Loads the configuration key-value pairs by resolving the config file path
 *   and parsing the file.
 * - `resolveConfigPath(defaultPath: Path)`: Resolves the configuration file path, using an environment
 *   variable override if present, or falling back to the default path.
 * - `readEnvFile(path: Path)`: Parses a given file for key-value pairs in the format `key=value`,
 *   ignoring lines that are commented or incorrectly formatted.
 */
object ConfigResolver {

    /**
     * Loads the application configuration by determining the configuration file path and
     * parsing its contents into key-value pairs.
     *
     * The method resolves the path to the configuration file, allowing for an environment
     * variable override, and then reads the file for configuration data. Lines in the file
     * that are commented out or not in a valid key-value format are ignored.
     *
     * @return A map of configuration key-value pairs loaded from the resolved configuration file.
     *         If the file does not exist or is empty, an empty map is returned.
     */
    fun loadAppConfig(): Map<String, String> {
        val configPath = resolveConfigPath(AppDirectories.configDir())
        return readEnvFile(configPath)
    }

    /**
     * Resolves the path to the application's configuration file. If the "INSTELLA_CONFIG"
     * environment variable is set, its value is used as the path to the configuration file.
     * Otherwise, a default path is derived by appending "config.env" to the provided default path.
     *
     * @param defaultPath The base directory to use when the "INSTELLA_CONFIG" environment
     *                    variable is not set.
     * @return The resolved configuration file path. If the environment variable is set, the
     *         returned path will be absolute; otherwise, it will be relative to the default path.
     */
    fun resolveConfigPath(defaultPath: Path): Path {
        val envOverride = System.getenv("INSTELLA_CONFIG")
        return if (envOverride != null) EnvPaths.absolute(envOverride) else defaultPath.resolve("config.env")
    }

    /**
     * Reads a file at the specified path and extracts key-value pairs in the format `key=value`.
     * Lines that do not contain an equals sign (`=`) or start with `#` (comments) are ignored.
     *
     * @param path The path to the file that contains the environment configuration data.
     * @return A map of key-value pairs extracted from the file. Returns an empty map if the file
     *         does not exist or contains no valid key-value pairs.
     */
    fun readEnvFile(path: Path): Map<String, String> {
        val file = path.toFile()
        if (!file.exists()) return emptyMap()
        return file.readLines()
            .filter { it.contains("=") && !it.startsWith("#") }.associate {
                val (k, v) = it.split("=", limit = 2)
                k.trim() to v.trim()
            }
    }
}
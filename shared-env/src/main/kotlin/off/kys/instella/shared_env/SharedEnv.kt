package off.kys.instella.shared_env

import java.nio.file.Path

/**
 * SharedEnv provides an interface for accessing application-specific directories
 * and environment-related properties. It serves as a central repository for
 * retrieving common file paths and system-related information.
 *
 * Properties:
 * - `configDir`: Path to the application's configuration directory.
 * - `cacheDir`: Path to the application's cache directory.
 * - `dataDir`: Path to the application's data directory.
 * - `logDir`: Path to the application's log directory.
 *
 * - `isDebug`: Indicates if the application is running in debug mode.
 * - `userName`: Retrieves the name of the current system user.
 * - `hostName`: Retrieves the hostname of the machine running the application.
 */
object SharedEnv {
    /**
     * Retrieves the path to the application's configuration directory.
     *
     * The configuration directory is a centralized location under the `.config` folder
     * within the user's home directory. It is used for storing application-specific
     * configuration files. The path is derived dynamically based on the application's
     * name and the underlying operating system's environment.
     *
     * @return The `Path` object representing the configuration directory for the application.
     */
    val configDir: Path get() = AppDirectories.configDir()

    /**
     * Provides the path to the application's cache directory.
     *
     * This directory is located under the ".cache" folder in the user's home directory
     * and is specific to the application. It is intended for storing temporary or
     * non-essential data that can be recreated if needed.
     */
    val cacheDir: Path get() = AppDirectories.cacheDir()

    /**
     * Provides the path to the application's persistent data storage directory.
     *
     * The returned directory is determined based on the user's home directory and the application's name.
     * It is located under the ".local/share" directory, which is typically used for storing
     * application-specific data that should persist between application runs.
     *
     * This path can be used to store files or other data that needs to remain available for the application.
     */
    val dataDir: Path get() = AppDirectories.dataDir()

    /**
     * Provides the path to the application's log directory.
     *
     * The log directory is located under the `.local/state` directory
     * in the user's home directory and is specific to the application.
     * It serves as a centralized location for storing log files generated
     * during the application's runtime.
     */
    val logDir: Path get() = AppDirectories.logDir()

    /**
     * Indicates whether the application is running in debug mode.
     *
     * This property retrieves its value from the environment by invoking `Env.isDebugMode()`.
     * Debug mode is determined by the presence of the "INSTELLA_DEBUG" environment variable
     * set to "1". When enabled, debug mode can be used for enhanced logging, verbose output,
     * or other development-focused features.
     *
     * @return `true` if the application is in debug mode; `false` otherwise.
     */
    val isDebug: Boolean get() = Env.isDebugMode()

    /**
     * Provides the name of the current system user.
     *
     * This property retrieves the value of the system property `user.name` to determine
     * the name of the user under which the application is running. It delegates to the
     * `userName` function within the `Env` object to obtain the username.
     *
     * Useful for identifying the user's context in which the application is executed.
     */
    val userName: String get() = Env.userName()

    /**
     * Provides the hostname of the machine where the application is currently running.
     *
     * The value is determined using environment variables `HOSTNAME` and `COMPUTERNAME`.
     * If neither variable is set, the hostname will default to "unknown".
     */
    val hostName: String get() = Env.hostName()
}
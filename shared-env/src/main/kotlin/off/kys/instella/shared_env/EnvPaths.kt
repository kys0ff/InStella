package off.kys.instella.shared_env

import java.nio.file.Path
import java.nio.file.Paths

/**
 * EnvPaths provides utility methods for retrieving commonly used file paths.
 * This object simplifies access to standard environment-dependent directories
 * and operations related to file paths.
 *
 * Functions:
 * - `homeDir()`: Returns the path to the current user's home directory.
 * - `tempDir()`: Returns the path to the system's temporary directory.
 * - `currentWorkingDir()`: Returns the path to the current working directory.
 * - `absolute(path: String)`: Converts a relative path string to an absolute path.
 */
object EnvPaths {
    /**
     * Retrieves the current user's home directory path.
     *
     * This function leverages the system property `user.home` to determine the home directory
     * of the user under which the application is being executed.
     *
     * @return The user's home directory as a Path object.
     */
    fun homeDir(): Path = Paths.get(System.getProperty("user.home"))

    /**
     * Retrieves the system's temporary directory path.
     *
     * This function returns the path to the temporary directory as specified
     * by the system property `java.io.tmpdir`. It is commonly used for storing
     * temporary files and data during the application's runtime.
     *
     * @return Path to the system's temporary directory.
     */
    fun tempDir(): Path = Paths.get(System.getProperty("java.io.tmpdir"))

    /**
     * Retrieves the current working directory of the application.
     *
     * This function returns the absolute path of the directory in which the
     * application was started. It is resolved using the `user.dir` system property.
     *
     * @return The path representing the current working directory.
     */
    fun currentWorkingDir(): Path = Paths.get(System.getProperty("user.dir"))

    /**
     * Converts the provided string path to its absolute path representation.
     *
     * This function resolves the given string path to an absolute filesystem path
     * and returns it as a `Path` object. The resolution is based on the current
     * working directory if the input is a relative path.
     *
     * @param path The string representation of the path to be converted to an absolute path.
     * @return The resolved absolute `Path` representation of the specified string path.
     */
    fun absolute(path: String): Path = Paths.get(path).toAbsolutePath()
}


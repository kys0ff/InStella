package off.kys.instella.shared_env

/**
 * Provides basic information about the operating system.
 *
 * This object includes utility properties for identifying specific
 * operating system characteristics. For example, it can determine
 * if the application is running on a Linux-based system.
 *
 * Properties:
 * - `isLinux`: Checks if the operating system is Linux.
 */
object OsInfo {
    /**
     * Indicates whether the current operating system is Linux.
     *
     * This property checks the system property `os.name` to determine if the application
     * is running on a Linux-based operating system. It performs a case-insensitive comparison
     * to ensure compatibility across different capitalization styles in the property value.
     *
     * @return `true` if the operating system is Linux; otherwise, `false`.
     */
    val isLinux: Boolean
        get() = System.getProperty("os.name").contains("Linux", ignoreCase = true)
}
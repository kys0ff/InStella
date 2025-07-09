package off.kys.instella.shared_env

/**
 * Env provides utility functions for querying environment and system-related properties.
 * This object simplifies access to system-level information such as terminal state,
 * user details, hostname, and debug mode flag.
 *
 * Functions:
 * - `isRunningInTerminal()`: Determines if the application is being executed in a terminal.
 * - `userName()`: Retrieves the name of the current system user.
 * - `hostName()`: Retrieves the hostname of the machine executing the application.
 * - `isDebugMode()`: Checks if the application is running in debug mode by inspecting
 *   a specific environment variable.
 */
object Env {
    /**
     * Determines if the current application is running in a terminal.
     *
     * This method checks if a console is available for the current runtime environment.
     * It can be used to identify whether the application is executed in an interactive
     * terminal or in a non-terminal context, such as a background process or a GUI application.
     *
     * @return `true` if the application is running in a terminal; `false` otherwise.
     */
    fun isRunningInTerminal(): Boolean =
        System.console() != null

    /**
     * Retrieves the current user's name.
     *
     * This function utilizes the system property `user.name` to obtain the name of the user
     * under which the application is currently running.
     *
     * @return The name of the current system user as a String.
     */
    fun userName(): String =
        System.getProperty("user.name")

    /**
     * Retrieves the hostname of the machine where the application is running.
     *
     * This function checks environment variables `HOSTNAME` and `COMPUTERNAME` in that order
     * to determine the hostname. If neither variable is set, it defaults to returning "unknown".
     *
     * @return The hostname as a string, or "unknown" if the hostname cannot be determined.
     */
    fun hostName(): String =
        System.getenv("HOSTNAME") ?: System.getenv("COMPUTERNAME") ?: "unknown"

    /**
     * Determines if the application is running in debug mode based on the environment variable
     * "INSTELLA_DEBUG". If the variable is set to "1", debug mode is active.
     *
     * @return `true` if the "INSTELLA_DEBUG" environment variable is set to "1", otherwise `false`.
     */
    fun isDebugMode(): Boolean =
        System.getenv("INSTELLA_DEBUG") == "1"
}


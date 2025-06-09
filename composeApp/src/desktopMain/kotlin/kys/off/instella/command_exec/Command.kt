@file:Suppress("unused")

package kys.off.instella.command_exec

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream

fun List<String>.execute(
    workingDir: File? = null,
    env: Map<String, String>? = null,
): CommandResult = runBlocking {
    val process = ProcessBuilder(this@execute)
        .apply {
            workingDir?.let { directory(it) }
            env?.let { environment().putAll(it) }
            redirectErrorStream(false)  // Keep stdout/stderr separate
        }
        .start()

    // Capture output streams in parallel threads
    val stdout = process.inputStream.readTextAsync()
    val stderr = process.errorStream.readTextAsync()
    val exitCode = process.waitFor()

    return@runBlocking CommandResult(
        exitCode = exitCode,
        stdout = stdout.await(),
        stderr = stderr.await()
    )
}

fun String.runCommand(
    workingDir: File? = null,
    env: Map<String, String>? = null,
): CommandResult = runBlocking {
    split("\\s".toRegex())
        .execute(workingDir, env)
}

fun CommandResult.throwOnError(): CommandResult {
    if (exitCode != 0) {
        throw RuntimeException(
            "Command failed with code $exitCode:\n" +
                    "STDERR: $stderr\n" +
                    "STDOUT: $stdout"
        )
    }
    return this
}

fun List<String>.executeWithLiveOutput(
    stdout: (String) -> Unit = {},
    stderr: (String) -> Unit = {},
    workingDir: File? = null,
): Int = ProcessBuilder(this)
    .apply {
        workingDir?.let { directory(it) }
        redirectErrorStream(false)
    }
    .start()
    .apply {
        // Capture streams in separate threads
        val outThread = inputStream.liveOutput(stdout)
        val errThread = errorStream.liveOutput(stderr)
        waitFor()
        outThread.join()
        errThread.join()
    }
    .exitValue()

fun List<List<String>>.pkexecMultiCommand(
    workingDir: File? = null,
    env: Map<String, String>? = null,
    chainMode: String = ";",
): CommandResult {
    require(chainMode in setOf(";", "&&")) { "Invalid chain mode. Use ';' or '&&'" }

    val script = this.joinToString(" $chainMode ") { command ->
        command.joinToString(" ") { arg ->
            when {
                arg.isEmpty() -> "''"
                arg.any { it in " \t\"'$&|<>;`" } -> "'${arg.replace("'", "'\\''")}'"
                else -> arg
            }
        }
    }

    val fullCommand = listOf("pkexec", "sh", "-c", script)
    return fullCommand.execute(workingDir, env)
}

fun pkexecMultiCommand(
    vararg commands: List<String>,
    workingDir: File? = null,
    env: Map<String, String>? = null,
    chainMode: String = ";",
): CommandResult = commands.toList().pkexecMultiCommand(workingDir, env, chainMode)

fun List<String>.pkexecCommand(
    workingDir: File? = null,
    env: Map<String, String>? = null,
): CommandResult = listOf(this).pkexecMultiCommand(workingDir, env, ";")

private fun InputStream.readTextAsync() = runBlocking {
    async {
        bufferedReader().use { it.readText() }
    }
}

private fun InputStream.liveOutput(consumer: (String) -> Unit) =
    Thread {
        bufferedReader().forEachLine(consumer)
    }.apply { start() }
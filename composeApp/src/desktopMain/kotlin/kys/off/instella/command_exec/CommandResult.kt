package kys.off.instella.command_exec

data class CommandResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String
)
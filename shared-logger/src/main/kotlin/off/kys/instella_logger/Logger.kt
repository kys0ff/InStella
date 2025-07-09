package off.kys.instella_logger

import off.kys.instella_logger.models.Level
import off.kys.instella_logger.models.UseCase
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object Logger {

    private val lock = ReentrantLock()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    private const val LOG_FILE_PATH = "logs/app.log"
    private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024L // 5 MB

    @Volatile
    var logLevel: Level = Level.DEBUG

    private val listeners = mutableListOf<(Level, UseCase, String) -> Unit>()

    private val logFile by lazy {
        val file = File(LOG_FILE_PATH)
        file.parentFile?.mkdirs()
        file
    }

    fun addListener(listener: (Level, UseCase, String) -> Unit) {
        lock.withLock {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: (Level, UseCase, String) -> Unit) {
        lock.withLock {
            listeners.remove(listener)
        }
    }

    private fun rotateLogFileIfNeeded() {
        if (logFile.exists() && logFile.length() > MAX_FILE_SIZE_BYTES) {
            val rotatedFile = File("$LOG_FILE_PATH.${System.currentTimeMillis()}")
            if (rotatedFile.exists()) rotatedFile.delete()
            val success = logFile.renameTo(rotatedFile)
            if (!success) {
                println("Logger: Failed to rotate log file.")
            }
        }
    }

    private fun log(level: Level, useCase: UseCase, message: String) {
        if (level.priority < logLevel.priority) return

        val timestamp = dateFormat.format(Date())
        val tag = "[${useCase.name}]"
        val formatted = "$timestamp $tag [${level.name}] $message"

        lock.withLock {
            // Console output with color
            val reset = "\u001B[0m"
            val color = level.colorCode
            println("$color$formatted$reset")

            // File output (no color)
            try {
                rotateLogFileIfNeeded()
                FileWriter(logFile, true).use { it.write(formatted + "\n") }
            } catch (ex: IOException) {
                println("Logger: Error writing to log file: ${ex.message}")
            }

            // Notify listeners
            for (listener in listeners) {
                try {
                    listener(level, useCase, formatted)
                } catch (ex: Exception) {
                    println("Logger: Listener error: ${ex.message}")
                }
            }
        }
    }

    // Overloads for convenience
    fun debug(message: String, useCase: UseCase = UseCase.OTHER) = log(Level.DEBUG, useCase, message)
    fun info(message: String, useCase: UseCase = UseCase.OTHER) = log(Level.INFO, useCase, message)
    fun warn(message: String, useCase: UseCase = UseCase.OTHER) = log(Level.WARN, useCase, message)
    fun error(message: String, throwable: Throwable? = null, useCase: UseCase = UseCase.OTHER) {
        val fullMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceToString()}"
        } else {
            message
        }
        log(Level.ERROR, useCase, fullMessage)
    }
}

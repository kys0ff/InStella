package kys.off.instella.utils.file_picker

import java.io.File

class FilePicker private constructor(
    private val title: String,
    private val initialDirectory: File?,
    private val filePatterns: List<String>,
    private val allowMultiple: Boolean,
    private val onSelection: (List<File>) -> Unit,
    private val onCancel: () -> Unit,
) {
    fun show() {
        try {
            val command = buildCommand()
            val process = ProcessBuilder(command).start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()

            when (process.exitValue()) {
                0 -> handleSuccess(output)
                else -> onCancel()
            }
        } catch (e: Exception) {
            System.err.println("Error showing file picker: ${e.message}")
            onCancel()
        }
    }

    private fun buildCommand(): List<String> {
        return mutableListOf("zenity", "--file-selection").apply {
            add("--title=$title")
            initialDirectory?.let { add("--filename=${it.absolutePath}") }
            if (allowMultiple) add("--multiple")
            if (filePatterns.isNotEmpty()) {
                add("--file-filter=${filePatterns.joinToString(" ")}")
            }
        }
    }

    private fun handleSuccess(output: String) {
        val files = if (allowMultiple) {
            output.split(Regex("\\|?\\s+"))
                .filter { it.isNotBlank() }
                .map { File(it) }
        } else {
            listOf(File(output))
        }
        onSelection(files)
    }

    class Builder {
        var title: String = "Select File"
        var initialDirectory: File? = null
        var filePatterns: MutableList<String> = mutableListOf()
        var allowMultiple: Boolean = false
        private var onSelection: (List<File>) -> Unit = { _ -> }
        private var onCancel: () -> Unit = {}

        fun title(block: () -> String) {
            title = block()
        }

        fun initialDir(block: () -> File) {
            initialDirectory = block()
        }

        fun filter(block: () -> String) {
            filePatterns.add(block())
        }

        fun allowMultiple(block: () -> Boolean = { true }) {
            allowMultiple = block()
        }

        fun onSelect(block: (List<File>) -> Unit) {
            onSelection = block
        }

        fun onCancel(block: () -> Unit) {
            onCancel = block
        }

        fun build() = FilePicker(
            title,
            initialDirectory,
            filePatterns,
            allowMultiple,
            onSelection,
            onCancel
        )
    }
}

package kys.off.instella.utils.file_picker

fun filePicker(block: FilePickerScope.() -> Unit) = FilePicker.Builder().apply(block).build().show()

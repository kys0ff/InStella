package kys.off.instella.utils.extensions

import java.io.File
import kotlin.math.ln
import kotlin.math.pow

fun File.getFormattedSize(): String {
    if (!exists()) return "0 B" // Handle non-existent files

    val bytes = length().toDouble()
    if (bytes == 0.0) return "0 B"

    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val exponent = (ln(bytes) / ln(1024.0)).toInt().coerceAtMost(units.size - 1)
    val value = bytes / 1024.0.pow(exponent)

    return "%.1f %s".format(value, units[exponent])
}
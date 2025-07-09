package kys.off.instella.utils.extensions

import java.nio.file.Path

@Suppress("SpellCheckingInspection")
fun Path.mkdirs() = toFile().mkdirs()
package off.kys.itoml4kt

import off.kys.itoml4kt.parser.TomlParser
import off.kys.itoml4kt.parser.extension.catch
import off.kys.itoml4kt.parser.extension.getArrayAt
import kotlin.io.path.Path

private fun main() = catch {
    val parser = TomlParser(Path("/home/kys0adam/Documents/code/plugin_design/manifest.toml"))
    val root = parser.parse()

    root.run {
        getArrayAt("permissions.required").forEach {
            println(it.toDisplayString())
        }
    }
}
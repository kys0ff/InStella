package off.kys.instella.plugin_bridge.utils.extensions.lua

import org.luaj.vm2.LuaNumber
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

fun LuaTable.toListString(): List<String> {
    val result = mutableListOf<String>()
    var index = LuaValue.ONE
    while (true) {
        val value = this.rawget(index)
        if (value.isnil()) break
        result.add(value.tojstring())  // Convert each value to a String
        index = index.add(1) as LuaNumber
    }
    return result
}
package off.kys.instella.plugin_bridge.utils.extensions.lua

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

operator fun Varargs?.get(key: String): LuaValue = this?.arg1()?.get(key) ?: LuaValue.NIL

fun Varargs?.getString(key: String): String = this?.arg1()?.get(key).toString()

fun Varargs?.getBoolean(key: String): Boolean = this?.arg1()?.get(key)?.toboolean() == true

fun Varargs?.getTable(): List<LuaValue> {
    val table = mutableListOf<LuaValue>()
    val luaTable: LuaTable = this?.checktable(1) ?: return emptyList()
    1.rangeTo(luaTable.length()).forEach { i ->
        table.add(luaTable.get(i).checktable())
    }
    return table
}
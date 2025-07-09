package off.kys.instella.plugin_bridge.utils.extensions.lua

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

fun createLuaTable(block: LuaTable.() -> Unit): LuaTable {
    val luaTable = LuaTable()
    block(luaTable)
    return luaTable
}

fun LuaTable.addZeroFunction(name: String, function: () -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : ZeroArgFunction() {
            override fun call(): LuaValue = function()
        }
    )

fun LuaTable.addFunction(name: String, function: (LuaValue) -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue = function(arg)
        }
    )

fun LuaTable.addFunction(name: String, function: (LuaValue, LuaValue) -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : TwoArgFunction() {
            override fun call(
                arg1: LuaValue,
                arg2: LuaValue,
            ): LuaValue? = function(arg1, arg2)
        }
    )

fun LuaTable.addFunction(name: String, function: (LuaValue, LuaValue, LuaValue) -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : ThreeArgFunction() {
            override fun call(
                arg1: LuaValue,
                arg2: LuaValue,
                arg3: LuaValue,
            ): LuaValue? = function(arg1, arg2, arg3)
        }
    )

fun LuaTable.get(name: String, block: LuaTable.() -> Unit): LuaValue = get(name).apply { block() }
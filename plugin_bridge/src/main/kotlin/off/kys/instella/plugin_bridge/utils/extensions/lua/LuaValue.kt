package off.kys.instella.plugin_bridge.utils.extensions.lua

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

fun LuaValue.addZeroFunction(name: String, function: () -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : ZeroArgFunction() {
            override fun call(): LuaValue = function()
        }
    )

fun LuaValue.addFunction(name: String, function: (LuaValue) -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue = function(arg)
        }
    )

fun LuaValue.addFunction(name: String, function: (LuaValue, LuaValue) -> LuaValue) =
    set(
        /* key = */ name,
        /* value = */ object : TwoArgFunction() {
            override fun call(
                arg1: LuaValue,
                arg2: LuaValue,
            ): LuaValue? = function(arg1, arg2)
        }
    )

fun LuaValue.addFunction(name: String, function: (LuaValue, LuaValue, LuaValue) -> LuaValue) =
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

fun Any.toLuaValue(): LuaValue = when (this) {
    is String -> LuaValue.valueOf(this)
    is Int -> LuaValue.valueOf(this)
    is Double -> LuaValue.valueOf(this)
    is Boolean -> LuaValue.valueOf(this)
    else -> {
        throw IllegalArgumentException("Unsupported type: ${this.javaClass.name}")
    }
}

fun Any.toLuaValueOrString(): LuaValue = try {
    toLuaValue()
} catch (e: IllegalArgumentException) {
    e.printStackTrace()
    this@toLuaValueOrString.toString().toLuaValue()
}

fun Any.toLuaValueOrNull(): LuaValue? = try {
    toLuaValue()
} catch (e: IllegalArgumentException) {
    e.printStackTrace()
    null
}

package off.kys.instella.plugin_bridge.loader

import off.kys.instella_logger.Logger
import off.kys.instella_logger.models.UseCase
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.jse.JsePlatform
import java.io.File

class LuaScriptLoader {
    private val globals: Globals
    private val scriptPath: String
    private var script: LuaValue? = null

    constructor(scriptPath: String) {
        this.scriptPath = scriptPath
        this.globals = JsePlatform.standardGlobals()
    }

    @Suppress("unused")
    constructor(scriptFile: File) {
        this.scriptPath = scriptFile.absolutePath
        this.globals = JsePlatform.standardGlobals()
    }

    // Load the Lua script
    fun loadScript(globalsBlock: Globals.() -> Unit = {}) {
        try {
            globalsBlock(globals)

            script = globals.loadfile(scriptPath)
            script?.call()
            Logger.info("Script loaded successfully from $scriptPath", useCase = UseCase.PLUGIN)
        } catch (e: Exception) {
            println("loadScript: $e")
        }
    }

    // Invoke a Lua function
    operator fun invoke(functionName: String, vararg args: String): Varargs? {
        val function = globals[functionName] // Get the Lua function by name
        return if (function.isfunction()) {
            try {
                val luaArgs = args.map { LuaValue.valueOf(it) }.toTypedArray()
                function.invoke(LuaValue.varargsOf(luaArgs)) // Pass arguments as varargs
            } catch (e: Exception) {
                Logger.error("Error invoking function $functionName", e, UseCase.PLUGIN)
                null
            }
        } else {
            Logger.error("Function $functionName not found or is not callable", useCase = UseCase.PLUGIN)
            null
        }
    }

    // Get a global variable from Lua
    fun getGlobalVariable(variableName: String): LuaValue? =
        globals[variableName]?.takeIf { !it.isnil() }

    // Set a global variable in Lua
    fun setGlobalVariable(variableName: String, value: String) {
        globals[variableName] = LuaValue.valueOf(value)
    }

    // Check if a function exists
    fun hasFunction(functionName: String): Boolean {
        val function = globals[functionName]
        return function?.isfunction() == true
    }

    // Run arbitrary Lua code
    fun run(luaCode: String): LuaValue? {
        return try {
            val chunk = globals.load(luaCode)
            chunk.call()
        } catch (e: Exception) {
            Logger.error("Error running code", e, UseCase.PLUGIN)
            null
        }
    }

    // Utility to call a function and get a string result
    fun get(functionName: String, vararg args: String): String? {
        val result = invoke(functionName, *args)
        return result?.arg1()?.tojstring()
    }
}
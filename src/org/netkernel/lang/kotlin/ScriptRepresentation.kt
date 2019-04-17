package org.netkernel.lang.kotlin

import org.netkernel.layer0.nkf.INKFRequestContext
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*

@KotlinScript(fileExtension = "nk.kts", displayName = "NetKernel Kotlin Script", compilationConfiguration = NetKernelScriptConfiguration::class)
abstract class NetKernelKotlinScript(val context: INKFRequestContext)

object NetKernelScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports("org.netkernel.layer0.nkf.INKFRequestContext")
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
//    providedProperties(Pair("context", INKFRequestContext::class))
    baseClass(NetKernelKotlinScript::class)
})

class ScriptRepresentation(val script: CompiledScript<*>)

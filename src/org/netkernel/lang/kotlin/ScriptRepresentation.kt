package org.netkernel.lang.kotlin

import org.netkernel.lang.kotlin.knkf.RequestContext
import org.netkernel.layer0.nkf.INKFRequest
import org.netkernel.layer0.nkf.INKFRequestContext
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*

@KotlinScript(fileExtension = "nk.kts", displayName = "NetKernel Kotlin Script", compilationConfiguration = NetKernelScriptConfiguration::class)
abstract class NetKernelKotlinScript(context: INKFRequestContext): RequestContext(context)

class Request<T>(context: NetKernelKotlinScript, representationClass: Class<T>, uri: String) {
    val rawRequest: INKFRequest = context.context.createRequest(uri)

    init {
        rawRequest.setRepresentationClass(representationClass)
    }

    fun addArgument(paramName: String, paramUri: String) {
        rawRequest.addArgument(paramName, paramUri)
    }

    fun addArgumentByValue(paramName: String, paramValue: String) {
        rawRequest.addArgumentByValue(paramName, paramValue)
    }
}

object NetKernelScriptConfiguration : ScriptCompilationConfiguration({
    defaultImports("org.netkernel.layer0.nkf.INKFRequestContext")
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    baseClass(NetKernelKotlinScript::class)
})

class ScriptRepresentation(val script: CompiledScript<*>)

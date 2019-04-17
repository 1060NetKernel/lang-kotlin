package org.netkernel.lang.kotlin

import kotlinx.coroutines.runBlocking
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardTransreptorImpl
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.resultOrNull
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptTransreptor: StandardTransreptorImpl() {
    init {
        this.declareThreadSafe()
        this.declareToRepresentation(ScriptRepresentation::class.java)
    }

    override fun onTransrept(context: INKFRequestContext?) {
        checkNotNull(context)

        val compilationConfiguration = ScriptCompilationConfiguration(listOf(NetKernelScriptConfiguration)){
            jvm {
                dependenciesFromClassloader(classLoader = context.getKotlinCompilerClassLoader(), wholeClasspath = true)
            }
        }

        val script = context.sourcePrimary(String::class.java)

        val compiledScriptResult = runBlocking {
            BasicJvmScriptingHost().compiler(script.toScriptSource(), compilationConfiguration)
        }

        compiledScriptResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                throw e
            }

            context.logRaw(it.severity.getNKFLogLevel(), "${it.message} ${it.location}")
        }

        val compiledScript = compiledScriptResult.resultOrNull()
        checkNotNull(compiledScript) // should not be null if we compiled without error

        val rep = ScriptRepresentation(compiledScript)
        context.createResponseFrom(rep)
    }
}

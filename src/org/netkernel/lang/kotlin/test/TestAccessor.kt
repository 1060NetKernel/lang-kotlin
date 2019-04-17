package org.netkernel.lang.kotlin.test

import kotlinx.coroutines.runBlocking
import org.netkernel.lang.kotlin.NetKernelScriptConfiguration
import org.netkernel.lang.kotlin.getKotlinCompilerClassLoader
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.module.standard.endpoint.StandardAccessorImpl
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class TestAccessor: StandardAccessorImpl() {
    override fun onSource(aContext: INKFRequestContext?) {
        checkNotNull(aContext)

        val compileClassLoader = aContext.getKotlinCompilerClassLoader()

        val compilationConfiguration = ScriptCompilationConfiguration(listOf(NetKernelScriptConfiguration)){
            jvm {
                dependenciesFromClassloader(classLoader = compileClassLoader, wholeClasspath = true)
            }
        }

        val script = """
            |context.createResponseFrom("Hello")
            |
        """.trimMargin()

        val compiledScriptResult = runBlocking {
            BasicJvmScriptingHost().compiler(script.toScriptSource(), compilationConfiguration)
        }

        compiledScriptResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                throw e
            }
            println("${it.severity} ${it.message}")
        }

        val compiledScript = compiledScriptResult.resultOrNull()
        checkNotNull(compiledScript)

        println("Compiled")

        val evalConfig = ScriptEvaluationConfiguration {
            providedProperties(Pair("context", aContext))
        }

        val evalResult = runBlocking {
            BasicJvmScriptingHost().evaluator(compiledScript, evalConfig)
        }

        evalResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                e.printStackTrace()
                throw e
            }
            println("${it.severity} ${it.message}")
        }
    }
}

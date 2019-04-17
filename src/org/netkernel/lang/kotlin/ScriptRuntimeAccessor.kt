package org.netkernel.lang.kotlin

import kotlinx.coroutines.runBlocking
import org.netkernel.layer0.nkf.INKFRequestContext
import org.netkernel.layer0.util.RequestScopeClassLoader
import org.netkernel.module.standard.endpoint.StandardAccessorImpl
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptRuntimeAccessor: StandardAccessorImpl() {
    init {
        this.declareThreadSafe()
    }

    override fun onSource(context: INKFRequestContext?) {
        checkNotNull(context)

        val cl = RequestScopeClassLoader(context.getKernelContext().requestScope)
        Thread.currentThread().contextClassLoader = cl

        val script = context.source("arg:operator", ScriptRepresentation::class.java)

        val evalConfig = ScriptEvaluationConfiguration {
            providedProperties(Pair("context", context))
        }

        val evalResult = runBlocking {
            BasicJvmScriptingHost().evaluator(script.script, evalConfig)
        }

        evalResult.reports.forEach {
            val e = it.exception
            if (e != null) {
                throw e
            }

            context.logRaw(it.severity.getNKFLogLevel(), "${it.message} ${it.location}")
        }
    }
}

package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestContext

abstract class RequestContext(val context: INKFRequestContext) {
    inline fun <reified T> request(
            uri: String,
            init: Request<T>.() -> Unit
    ): Request<T> {
        val request = Request(this, T::class.java, uri)
        request.init()
        return request
    }

    inline fun <reified T> issue(request: () -> Request<String>): T {
        val response = context.issueRequest(request().rawRequest)
        check(response is T)
        return response
    }

    fun createResponseFrom(response: Any) {
        context.createResponseFrom(response)
    }

    fun createResponseFrom(response: () -> Any) {
        context.createResponseFrom(response())
    }

    inline fun <reified T> source(uri: String): T {
        return context.source(uri, T::class.java)
    }
}

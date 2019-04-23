package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequestContext

abstract class RequestContext(val context: INKFRequestContext) {

    inline fun <reified T> issue(request: () -> RequestWithResponse<T>): T {
        val response = context.issueRequest(request().rawRequest)
        check(response is T)
        return response
    }

    inline fun <reified T> issue(request: RequestWithResponse<T>): T {
        val response = context.issueRequest(request.rawRequest)
        check(response is T)
        return response
    }

    fun issue(request: () -> Request) {
        context.issueRequest(request().rawRequest)
    }

    fun issue(request: Request) {
        context.issueRequest(request.rawRequest)
    }

    fun response(response: Any) {
        context.createResponseFrom(response)
    }

    fun response(response: () -> Any) {
        context.createResponseFrom(response())
    }

    inline fun <reified T> sourceRequest(
            uri: String,
            init: SourceRequest<T>.() -> Unit
    ): SourceRequest<T> {
        val request = SourceRequest(this, T::class.java, uri)
        request.init()
        return request
    }

    inline fun <reified T> sourceRequest(uri: String): SourceRequest<T> {
        return SourceRequest(this, T::class.java, uri)
    }

    inline fun <reified T> source(uri: String): T {
        return source(uri) {}
    }

    inline fun <reified T> source(uri: String, init: SourceRequest<T>.() -> Unit): T {
        return issue(sourceRequest(uri, init))
    }

    fun sinkRequest(
            uri: String,
            init: SinkRequest.() -> Unit
    ): SinkRequest {
        val request = SinkRequest(this, uri)
        request.init()
        return request
    }


    fun sink(uri: String) {
        sink(uri) {}
    }

    fun sink(uri: String, init: SinkRequest.() -> Unit) {
        issue(sinkRequest(uri, init))
    }

    fun newRequest(
            uri: String,
            init: NewRequest.() -> Unit
    ): NewRequest {
        val request = NewRequest(this, uri)
        request.init()
        return request
    }

    fun new(uri: String): Boolean {
        return new(uri) {}
    }

    fun new(uri: String, init: NewRequest.() -> Unit): Boolean {
        return issue(newRequest(uri, init))
    }

    fun existsRequest(
            uri: String,
            init: ExistsRequest.() -> Unit
    ): ExistsRequest {
        val request = ExistsRequest(this, uri)
        request.init()
        return request
    }

    fun existsRequest(uri: String): ExistsRequest {
        return ExistsRequest(this, uri)
    }

    fun exists(uri: String): Boolean {
        return exists(uri) {}
    }

    fun exists(uri: String, init: ExistsRequest.() -> Unit): Boolean {
        return issue(existsRequest(uri, init))
    }

    fun deleteRequest(
            uri: String,
            init: DeleteRequest.() -> Unit
    ): DeleteRequest {
        val request = DeleteRequest(this, uri)
        request.init()
        return request
    }

    fun deleteRequest(uri: String): ExistsRequest {
        return ExistsRequest(this, uri)
    }

    fun delete(uri: String): Boolean {
        return delete(uri) {}
    }

    fun delete(uri: String, init: DeleteRequest.() -> Unit): Boolean {
        return issue(deleteRequest(uri, init))
    }

    inline fun <reified T> transrept(value: Any): T {
        return context.transrept(value, T::class.java)
    }

    inline fun <reified T> transrept(value: () -> Any): T {
        return context.transrept(value(), T::class.java)
    }
}

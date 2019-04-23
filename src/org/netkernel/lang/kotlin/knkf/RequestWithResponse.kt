package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequest

abstract class Request(protected val requestContext: RequestContext, uri: String, verb: Verb) {
    val rawRequest: INKFRequest = requestContext.context.createRequest(uri)

    init {
        rawRequest.setVerb(verb.nkfVerb)
    }
}

abstract class RequestWithResponse<T>(requestContext: RequestContext, representationClass: Class<T>, uri: String, verb: Verb): Request(requestContext, uri, verb) {
    init {
        rawRequest.setRepresentationClass(representationClass)
    }

    fun argument(paramName: String, paramValue: String) {
        rawRequest.addArgumentByValue(paramName, paramValue)
    }

    fun argument(paramName: String, paramRequest: () -> SourceRequest<*>) {
        rawRequest.addArgumentByRequest(paramName, paramRequest().rawRequest)
    }

    fun argument(paramName: String, paramRequest: SourceRequest<*>) {
        rawRequest.addArgumentByRequest(paramName, paramRequest.rawRequest)
    }
}

interface RequestWithPrimaryArgument {
    fun primaryArgument(value: Any)
    fun primaryArgument(request: () -> SourceRequest<*>)
}

class SourceRequest<T>(requestContext: RequestContext, representationClass: Class<T>, uri: String) : RequestWithResponse<T>(requestContext, representationClass, uri, Verb.SOURCE)

class ExistsRequest(requestContext: RequestContext, uri: String) : RequestWithResponse<Boolean>(requestContext, Boolean::class.java, uri, Verb.EXISTS)

class DeleteRequest(requestContext: RequestContext, uri: String) : RequestWithResponse<Boolean>(requestContext, Boolean::class.java, uri, Verb.DELETE)

class NewRequest(requestContext: RequestContext, uri: String) : RequestWithResponse<Boolean>(requestContext, Boolean::class.java, uri, Verb.NEW), RequestWithPrimaryArgument {
    override fun primaryArgument(value: Any) {
        rawRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(request: () -> SourceRequest<*>) {
        rawRequest.addPrimaryArgumentFromResponse(requestContext.context.issueRequestForResponse(request().rawRequest))
    }
}

class SinkRequest(requestContext: RequestContext, uri: String) : Request(requestContext, uri, Verb.SINK), RequestWithPrimaryArgument {
    override fun primaryArgument(value: Any) {
        rawRequest.addPrimaryArgument(value)
    }

    override fun primaryArgument(request: () -> SourceRequest<*>) {
        rawRequest.addPrimaryArgumentFromResponse(requestContext.context.issueRequestForResponse(request().rawRequest))
    }
}

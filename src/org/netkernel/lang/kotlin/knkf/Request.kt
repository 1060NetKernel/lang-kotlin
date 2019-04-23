package org.netkernel.lang.kotlin.knkf

import org.netkernel.layer0.nkf.INKFRequest

class Request<T>(context: RequestContext, representationClass: Class<T>, uri: String) {
    val rawRequest: INKFRequest = context.context.createRequest(uri)

    init {
        rawRequest.setRepresentationClass(representationClass)
    }

    fun verb(verb: Verb) {
        rawRequest.setVerb(verb.nkfVerb)
    }

    fun addArgument(paramName: String, paramUri: String) {
        rawRequest.addArgument(paramName, paramUri)
    }

    fun addArgumentByValue(paramName: String, paramValue: String) {
        rawRequest.addArgumentByValue(paramName, paramValue)
    }

    fun addArgugmentByRequest(paramName: String, paramRequest: () -> Request<*>) {
        rawRequest.addArgumentByRequest(paramName, paramRequest().rawRequest)
    }

    fun addArgugmentByRequest(paramName: String, paramRequest: Request<*>) {
        rawRequest.addArgumentByRequest(paramName, paramRequest.rawRequest)
    }
}

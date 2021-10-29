package com.github.websocket.message

class WebServiceRequest private constructor(private val builder: Builder) {

    private var header: RequestHeaderVO?=null
    private var body: Map<String,Any>?=null

    init {
        header = builder.header
        body = builder.body
    }

    fun getHeader() = header

    fun getBody() = body

    class Builder private constructor() {

        var header: RequestHeaderVO?=null

        var body: Map<String,Any>?=null

        constructor(init: Builder.() -> Unit): this() { init() }

        fun header(init: Builder.() -> RequestHeaderVO) = apply { header = init() }

        fun body(init: Builder.() -> Map<String,Any>?) = apply { body = init() }

        fun build(): WebServiceRequest = WebServiceRequest(this)
    }
}
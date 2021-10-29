package com.github.websocket.message

data class WebServiceResponse(

    val header: ResponseHeaderVO,
    val body: MutableMap<String,Any>
)
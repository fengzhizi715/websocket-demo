package com.github.websocket.utils

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun sha256HMAC(secret:String, data:String):String{

    val sha256HMAC = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
    sha256HMAC.init(secretKey)
    val bytes = sha256HMAC.doFinal(data.toByteArray())

    return Base64.getEncoder().encodeToString(bytes)
}
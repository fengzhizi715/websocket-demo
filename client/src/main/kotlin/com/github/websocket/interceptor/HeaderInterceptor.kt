package com.github.websocket.interceptor

import com.github.websocket.utils.sha256HMAC
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 *
 * @FileName:
 *          com.github.websocket.interceptor.HeaderInterceptor
 * @author: Tony Shen
 * @date: 2021-10-30 12:56 上午
 * @version: V1.0 配合 OKHttpRunner 使用的拦截器，用于 WebSocket 鉴权
 */
class HeaderInterceptor(private val portId:String,private val securityKey:String,private val idNo:String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().addHeader().build())
    }

    /**
     * 添加请求头
     */
    private fun Request.Builder.addHeader(): Request.Builder {

        val appKey = portId
        val secret = securityKey
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val clientIdNo = idNo
        val timestamp = System.currentTimeMillis().toString()

        //加密
        val headerStr = "$appKey$clientIdNo$nonce$timestamp"
        val sign = sha256HMAC(secret, headerStr)

        addHeader("app-key", appKey)
        addHeader("client-id", clientIdNo)
        addHeader("nonce", nonce)
        addHeader("request-timestamp", timestamp)
        addHeader("sign", sign)
        return this
    }

}

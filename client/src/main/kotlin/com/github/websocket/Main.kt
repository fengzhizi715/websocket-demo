package com.github.websocket

import cn.netdiscovery.http.core.HttpClient
import cn.netdiscovery.http.core.HttpClientBuilder
import cn.netdiscovery.http.core.websocket.ReconnectWebSocketWrapper
import cn.netdiscovery.http.core.websocket.WSConfig
import cn.netdiscovery.http.core.websocket.WSStatus
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @FileName:
 *          com.github.websocket.Main
 * @author: Tony Shen
 * @date: 2021/10/30 12:23 上午
 * @version: V1.0 <描述当前版本功能>
 */
val topLevelClass = object : Any() {}.javaClass.enclosingClass
val logger: Logger = LoggerFactory.getLogger(topLevelClass)

private var websocket: WebSocket?=null
private var status: WSStatus?=null
private var ws: ReconnectWebSocketWrapper?=null

private val httpClient: HttpClient by lazy {
    HttpClientBuilder()
        .baseUrl("http://127.0.0.1:9876/ws")
        .pingInterval(10, TimeUnit.SECONDS) // websocket 保活
        .allTimeouts(30, TimeUnit.SECONDS)
//        .addInterceptor(HeaderInterceptor(builder.portId,builder.securityKey,builder.machineId))  // websocket 鉴权
        .build()
}

fun main() {

    // 支持重试的 WebSocket 客户端
    ws = httpClient.websocket("http://127.0.0.1:9876/ws",listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            logger.info("connection opened...")

            websocket = webSocket
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            logger.info("received instruction: $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            logger.info("connection closing: $code, $reason")

            websocket = null
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            logger.error("connection closed: $code, $reason")

            websocket = null
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            logger.error("websocket connection error")

            websocket = null
        }
    },wsConfig = WSConfig())

    ws?.onConnectStatusChangeListener = {
        logger.info("${it.name}")
        status = it
    }
}
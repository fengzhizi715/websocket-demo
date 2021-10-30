package com.github.websocket

import com.github.websocket.extension.writeWSResponse
import com.github.websocket.message.RequestHeaderVO
import com.github.websocket.message.ResponseHeaderVO
import com.github.websocket.message.WebServiceRequest
import com.github.websocket.message.WebServiceResponse
import com.github.websocket.message.enum.WebSocketActionEnum
import com.github.websocket.utils.DeviceUtils
import com.github.websocket.utils.GsonUtils
import com.github.websocket.utils.uuid
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @FileName:
 *          com.github.websocket.WebSocketManager
 * @author: Tony Shen
 * @date: 2021/10/29 11:52 下午
 * @version: V1.0 <描述当前版本功能>
 */
object WebSocketManager {

    private val logger = LoggerFactory.getLogger(WebSocketManager::class.java)

    private val channelMap = ConcurrentHashMap<String,Channel>() // Map 存储 channelId、Channel

    fun addChannel(channel: Channel) {
        channelMap.put(channel.id().asShortText(), channel)
    }

    fun removeChannel(channelId:String) {
        channelMap.remove(channelId)
    }

    /**
     * 根据 channelId，断开 channel
     */
    fun disConnectChannel(channelId:String) {
        channelMap.get(channelId)?.let {
            it.close()
        }
    }

    fun handlerWebSocketFrame(ctx: ChannelHandlerContext, msg: WebSocketFrame) {

        if (msg !is TextWebSocketFrame) {
            logger.error("Only text messages are supported")
            throw UnsupportedOperationException(String.format("%s frame types not supported", msg.javaClass.simpleName))
        }

        val request = msg.text()         // 收到应答消息
        logger.info("receive request：${request}")

        val webServiceRequest = GsonUtils.fromJson<WebServiceRequest>(request, WebServiceRequest::class.java)

        webServiceRequest?.let { request->

            val header = request.getHeader()
            val clientIdNo = header?.clientIdNo
            if (clientIdNo == null && DeviceUtils.machineId != clientIdNo) {
                throw RuntimeException("clientIdNo is null or clientIdNo is illegal")
            }

            val body = request.getBody()

            when(body?.get("action").toString()) {
                "heartbeat" -> {
                    ctx.writeWSResponse {
                        val requestHeaderVO = generateResponseHeader(header?.msgSn)

                        val responseBody = mutableMapOf<String, Any>().apply {
                            this["action"] = WebSocketActionEnum.HEARTBEAT.getActionName()
                            this["msg"] = "pong"
                        }
                        WebServiceResponse(requestHeaderVO,responseBody)
                    }
                }
            }
        }
    }

    private fun generateResponseHeader(msgSn:String?) = ResponseHeaderVO(DeviceUtils.machineId, "Response", msgSn)

    fun generatePushHeader() = RequestHeaderVO(DeviceUtils.machineId, "Push", uuid(),"Windows","1.0")
}
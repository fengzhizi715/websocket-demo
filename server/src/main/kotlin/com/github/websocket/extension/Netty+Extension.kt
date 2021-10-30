package com.github.websocket.extension

import com.github.websocket.message.WebServiceRequest
import com.github.websocket.message.WebServiceResponse
import com.github.websocket.utils.GsonUtils
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @FileName:
 *          com.github.websocket.extension.`Netty+Extension`
 * @author: Tony Shen
 * @date: 2021/10/30 11:09 上午
 * @version: V1.0 <描述当前版本功能>
 */
val topLevelClass = object : Any() {}.javaClass.enclosingClass
val logger: Logger = LoggerFactory.getLogger(topLevelClass)

fun ChannelHandlerContext.channelId():String {
    val channel = this.channel()
    return channel.id().asShortText()
}

/**
 * 响应 ws 客户端，发送 WebServiceResponse 给客户端
 */
fun ChannelHandlerContext.writeWSResponse(block: ()-> WebServiceResponse) {
    val msg = GsonUtils.toJson(block.invoke())

    logger.info(msg)
    this.writeAndFlush(TextWebSocketFrame(msg))
}

/**
 * 将 WebServiceRequest 主动推送给 ws 客户端
 */
fun ChannelHandlerContext.writeWSRequest(block: ()-> WebServiceRequest.Builder) {
    val msg = GsonUtils.toJson(block.invoke())

    logger.info(msg)
    this.writeAndFlush(TextWebSocketFrame(msg))
}

/**
 * 将 WebServiceRequest 主动推送给 ws 客户端
 */
fun Channel.writeWSRequest(block: ()-> WebServiceRequest.Builder) {
    val msg = GsonUtils.toJson(block.invoke())

    logger.info(msg)
    this.writeAndFlush(TextWebSocketFrame(msg))
}

/**
 * channel 准备发送消息
 */
fun Channel.prepareWrite(): Channel? = this.takeIf{ it.isActive }
package com.github.websocket.handler

import com.github.websocket.WebSocketManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import org.slf4j.LoggerFactory

/**
 *
 * @FileName:
 *          com.github.websocket.handler.TextWebSocketFrameHandler
 * @author: Tony Shen
 * @date: 2021/10/29 11:46 下午
 * @version: V1.0 <描述当前版本功能>
 */
class TextWebSocketFrameHandler : SimpleChannelInboundHandler<Object>() {

    private val logger = LoggerFactory.getLogger(TextWebSocketFrameHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Object) {

        if (msg is FullHttpRequest) {
//            handleHttpRequest(ctx, msg as FullHttpRequest)
        } else if (msg is WebSocketFrame) {
            WebSocketManager.handlerWebSocketFrame(ctx, msg as WebSocketFrame)
        }
    }

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext) {

        logger.info("handlerAdded：" + ctx.channel().id().asLongText())
        WebSocketManager.addChannel(ctx.channel())
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {

        logger.info("handlerRemoved：" + ctx.channel().id().asLongText())
        WebSocketManager.removeChannel(ctx.channel().id().asShortText())
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error("WebSocket 发生异常",cause)
        WebSocketManager.removeChannel(ctx.channel().id().asShortText())
        ctx.close()
    }
}
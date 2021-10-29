package com.github.websocket

import com.github.websocket.handler.TextWebSocketFrameHandler
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler
import org.slf4j.LoggerFactory

/**
 *
 * @FileName:
 *          com.github.websocket.WebSocketServer
 * @author: Tony Shen
 * @date: 2021/10/29 11:29 下午
 * @version: V1.0 <描述当前版本功能>
 */
object WebSocketServer {

    private val logger = LoggerFactory.getLogger(WebSocketServer::class.java)

    private lateinit var boss: EventLoopGroup
    private lateinit var worker: EventLoopGroup

    private const val WS_PORT = 9876

    /**
     * 启动 WebSocket 服务的方法
     */
    fun execute() {
        boss = NioEventLoopGroup(1)
        worker = NioEventLoopGroup()
        val bootstrap = ServerBootstrap()
        bootstrap.group(boss, worker).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 100)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(object : ChannelInitializer<NioSocketChannel>() {

                @Throws(Exception::class)
                override fun initChannel(nioSocketChannel: NioSocketChannel) {
                    val pipeline: ChannelPipeline = nioSocketChannel.pipeline()
                    pipeline.addLast(HttpServerCodec())
                    pipeline.addLast(ChunkedWriteHandler())
                    pipeline.addLast(HttpObjectAggregator(8192))
                    pipeline.addLast(WebSocketServerProtocolHandler("/ws"))
                    pipeline.addLast(TextWebSocketFrameHandler())
                }
            })

        val future: ChannelFuture = bootstrap.bind(WS_PORT)

        future.addListener(object : ChannelFutureListener {

            @Throws(Exception::class)
            override fun operationComplete(channelFuture: ChannelFuture) {
                if (channelFuture.isSuccess) {
                    logger.info("WebSocket Server is starting...")
                } else {
                    logger.error("WebSocket Server failed",channelFuture.cause())
                }
            }
        })
    }

    fun shutdown() {

        logger.info("WebSocket Server is shutdown...")

        worker.shutdownGracefully()
        boss.shutdownGracefully()
    }
}
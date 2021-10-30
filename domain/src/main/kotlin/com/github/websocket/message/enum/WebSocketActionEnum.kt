package com.github.websocket.message.enum

/**
 *
 * @FileName:
 *          com.github.websocket.enum.WebSocketActionEnum
 * @author: Tony Shen
 * @date: 2021/10/30 11:12 上午
 * @version: V1.0 <描述当前版本功能>
 */
enum class WebSocketActionEnum(private val actionName:String) {

    LOGIN("login"),
    HEARTBEAT("heartbeat");

    fun getActionName() = actionName
}
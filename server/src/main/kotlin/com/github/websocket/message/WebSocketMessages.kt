package com.github.websocket.message

data class RequestHeaderVO(

    /**
     * 客户端唯一标识,由客户端自己生成
     */
    val clientIdNo: String,

    /**
     * 请求类型
     */
    val method: String?,

    /**
     * 请求的唯一标识
     */
    var msgSn: String?,

    /**
     * 操作系统
     */
    var os: String?,

    /**
     * 软件版本号
     */
    var version: String? = null,

    var extension: String? = null
)

data class ResponseHeaderVO(

    /**
     * 客户端唯一标识,由客户端自己生成
     */
    val clientIdNo: String,

    /**
     * 请求类型
     */
    val method: String?,

    /**
     * 请求的唯一标识
     */
    var msgSn: String?
)
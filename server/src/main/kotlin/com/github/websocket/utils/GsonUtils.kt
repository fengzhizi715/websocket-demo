package com.github.websocket.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.lang.reflect.Type

object GsonUtils {

    val gson = Gson()

    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }

    fun toJson(data: Any?): String {
        return gson.toJson(data)
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    inline fun <reified T : Any> jsonToArrayList(jsonStr: String, clazz: Class<T>): ArrayList<T> {

        val jsonParser = JsonParser()
        val jsonElements = jsonParser.parse(jsonStr).asJsonArray//获取JsonArray对象

        val beans = arrayListOf<T>()
        for (bean in jsonElements) {
            beans.add(gson.fromJsonElement(bean))
        }
        return beans
    }

    inline fun <reified T : Any> Gson.fromJsonElement(jsonElement: JsonElement): T = fromJson(jsonElement, T::class.java)
}
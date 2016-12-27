package com.krev.trycrypt.utils

import com.krev.trycrypt.application.Config

object JsonUtils {
    fun write(a: Any) = Config.mapper.writeValueAsString(a)!!

    fun <T : Any> writeArray(items: Array<T>) = StringBuilder("{").apply {
        items.forEach { append("\"${it.javaClass.simpleName.toLowerCase()}\":${write(it)},") }
    }.toString().trimEnd(',') + "}"

    fun <T> read(a: String, clazz: Class<T>): T = Config.mapper.readValue(a, clazz)
}
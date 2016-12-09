package com.krev.trycrypt.utils

import com.krev.trycrypt.application.Config

/**
 * Created by Dima on 06.12.2016.
 */
object JsonUtils {
    fun json(a: Any): String = Config.mapper.writeValueAsString(a)

    fun <T> json(a: String, clazz: Class<T>): T = Config.mapper.readValue(a, clazz)
}
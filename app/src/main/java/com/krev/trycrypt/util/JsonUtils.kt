package com.krev.trycrypt.util

import android.util.Log
import com.krev.trycrypt.application.Config

object JsonUtils {
    fun write(a: Any) = Config.mapper.writeValueAsString(a)!!

    fun <T : Any> writeArray(items: Array<T>): String {
        var ret = "{"
        Log.d("JsonUtils", "writeArray: $ret")
        items.forEach {
            ret += "\"${it.javaClass.simpleName.toLowerCase()}\":${write(it)},"
            Log.d("JsonUtils", "writeArray: $ret")
        }
        return ret.trimEnd(',') + "}"
    }

    fun <T : Any> writeItems(vararg items: T) = writeArray(items)

    fun <T> read(a: String, clazz: Class<T>): T = Config.mapper.readValue(a, clazz)
}
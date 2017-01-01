package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.JSON
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read
import com.krev.trycrypt.util.JsonUtils.write
import com.krev.trycrypt.util.JsonUtils.writeArray
import okhttp3.Request
import okhttp3.RequestBody

abstract class BaseController<T : BaseEntity>(val array: Array<T>) {

    open fun get(ids: Collection<Long>) = asyncThread {
        request(Method.GET, url("&ids=" + ids.write()))
    }.thenApplyAsync { read(it, array.javaClass).asList() }!!

    open fun request(method: Method, item: T) = asyncThread { request(method, url(), body(item)) }

    protected fun body(vararg a: Any): RequestBody {
        val s = if (a.count() == 1) write(a[0]) else writeArray(a)
//        val z = s.trim('\"').filterNot { it == '\\' }
//        Log.d("BaseController", "body: >$s<")
//        Log.d("BaseController", "body: >$z<")
//        Log.d("BaseController", "body: ${Arrays.toString(a)} && ${a.count()}")
        return RequestBody.create(JSON, s)
    }

    protected fun url(params: String = "", path: String = "") =
            "$address/${array[0].javaClass.simpleName.toLowerCase()}$path?device=$device$params"

    protected fun request(method: Method, url: String, body: RequestBody = body("")): String {
        Log.d("BaseController", ">$method< ## >$url<")
        return client.newCall(when (method) {
            Method.GET -> Request.Builder().get()
            Method.POST -> Request.Builder().post(body)
            Method.PUT -> Request.Builder().put(body)
            Method.DELETE -> Request.Builder().delete(body)
        }.url(url).build()).execute().body().string()!!
    }

    private fun Collection<Long>.write() = write(this).trim('[', ']')

    enum class Method {
        GET, POST, PUT, DELETE
    }
}


package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.JSON
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.utils.AsyncUtils.asyncThread
import com.krev.trycrypt.utils.JsonUtils.read
import com.krev.trycrypt.utils.JsonUtils.write
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

@Suppress("unused")
abstract class BaseController<T : BaseEntity>(protected val array: Array<T>) {

    open fun get(ids: Collection<Long>) = asyncThread {
        request(Method.GET, url("&ids=${write(ids).trim('[', ']')}")).body().string()
    }.thenApplyAsync { read(it, array.javaClass).asList() }!!

    open fun request(method: Method, item: T) = asyncThread { request(method, url(), body(item)) }

    protected fun body(a: Any): RequestBody = RequestBody.create(JSON, write(a))

    protected fun url(params: String = "", path: String = "") =
            "$address/${array[0].javaClass.simpleName.toLowerCase()}" + "$path?device=$device$params"

    protected fun request(method: Method, url: String, body: RequestBody = body("")): Response {
        Log.d("BaseController", ">$method< ## >$url<")
        return client.newCall(when (method) {
            Method.GET -> Request.Builder().get()
            Method.POST -> Request.Builder().post(body)
            Method.PUT -> Request.Builder().put(body)
            Method.DELETE -> Request.Builder().delete(body)
        }.url(url).build()).execute()!!
    }

    enum class Method {
        GET, POST, PUT, DELETE
    }
}


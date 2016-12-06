package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.JSON
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.JsonAlias.Companion.json
import com.krev.trycrypt.utils.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

@Suppress("unused")
abstract class BaseController<T : BaseEntity>(protected val array: Array<T>) {

    open fun get(ids: Collection<Id>, consumer: Consumer<List<T>>) {
        Task(Supplier<List<T>> {
            Log.d("BaseController", "$ids")
            mapper.readValue(request(Method.GET, url() + "&ids=" + json(ids))
                    .body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    open fun post(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            request(Method.POST, url(), body(t))
        }, consumer).execute()
    }

    open fun put(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            request(Method.PUT, url(), body(t))
        }, consumer).execute()
    }

    open fun delete(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            request(Method.DELETE, url(), body(t))
        }, consumer).execute()
    }

    protected enum class Method {
        GET, POST, PUT, DELETE
    }

    protected fun body(a: Any): RequestBody = RequestBody.create(JSON, json(a))

    protected fun url(params: String = "", path: String = "") = "$address/${className()}$path?device=$device$params"

    protected fun request(method: Method, url: String,
                          body: RequestBody = body("")) = client.newCall(when (method) {
        Method.GET -> Request.Builder().get()
        Method.POST -> Request.Builder().post(body)
        Method.PUT -> Request.Builder().put(body)
        Method.DELETE -> Request.Builder().delete(body)
    }.url(url).build()).execute()!!

    protected fun className() = array[0].javaClass.simpleName.toLowerCase()
}


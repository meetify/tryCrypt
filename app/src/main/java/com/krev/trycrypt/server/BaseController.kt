package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.JSON
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.Task
import com.krev.trycrypt.utils.async.TaskKotlin
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

@Suppress("unused")
abstract class BaseController<T : BaseEntity>(protected val array: Array<T>) {

    open fun get(ids: Collection<Id>, consumer: (List<T>) -> Unit = {}) {
        TaskKotlin({
            Log.d("BaseController", "$ids")
            json(request(Method.GET, url("&ids=${json(ids)}")).body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    open fun post(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            request(Method.POST, url(), body(t))
        }, consumer).execute()
    }

    open fun put(t: T, consumer: (Response) -> Unit = {}) {
        Log.d("BaseController", "going to put new place")
        TaskKotlin({
            request(Method.PUT, url(), body(t))
        }, consumer).execute()
    }

    open fun delete(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            request(Method.DELETE, url(), body(t))
        }, consumer).execute()
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

    protected enum class Method {
        GET, POST, PUT, DELETE
    }

    protected fun className() = array[0].javaClass.simpleName.toLowerCase()
}


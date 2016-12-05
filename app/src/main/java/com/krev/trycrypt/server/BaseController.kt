package com.krev.trycrypt.server

import com.krev.trycrypt.application.Config.JSON
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.utils.async.Consumer
import com.krev.trycrypt.utils.async.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 * This class represents some base controller. It has pre-implemented methods get, post, put, delete.
 * They are implemented in that way so they can be used without modifications on some usual tasks.
 * @version 0.0.1
 * @since   0.0.1
 * @property    repo    Some custom repository that represents connection with some database
 * @property    manager Entity manager used for queries.
 * @constructor         Autowired by Spring.
 */
@Suppress("unused")
abstract class BaseController<T : BaseEntity>(internal val array: Array<T>) {

    open fun get(ids: Collection<Id>, consumer: Consumer<List<T>>) {
        Task(Supplier<List<T>> {
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

    internal enum class Method {
        GET, POST, PUT, DELETE
    }

    internal fun json(a: Any): String = mapper.writeValueAsString(a)

    internal fun body(a: Any): RequestBody = RequestBody.create(JSON, json(a))

    internal fun url(path: String = "") = "$address/${array[0].javaClass.simpleName.toLowerCase()}$path?device=$device"

    internal fun request(method: Method, url: String,
                         body: RequestBody = body("")) = client.newCall(when (method) {
        Method.GET -> Request.Builder().get()
        Method.POST -> Request.Builder().post(body)
        Method.PUT -> Request.Builder().put(body)
        Method.DELETE -> Request.Builder().delete(body)
    }.url(url).build()).execute()
}


package com.krev.trycrypt.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier
import okhttp3.*

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
            mapper.readValue(client.newCall(Request.Builder()
                    .url(url() + "&ids=${asString(ids)}")
                    .get().build()).execute().body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    internal fun url(path: String = "") = "$address/${array[0].javaClass.simpleName.toLowerCase()}$path?device=$device"

    open fun post(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            client.newCall(Request.Builder()
                    .post(createBody(t)).url(url()).build()).execute()
        }, consumer).execute()
    }

    open fun put(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            client.newCall(Request.Builder()
                    .put(createBody(t)).url(url()).build()).execute()
        }, consumer).execute()
    }

    open fun delete(t: T, consumer: Consumer<Response> = Consumer {}) {
        Task(Supplier<Response> {
            client.newCall(Request.Builder()
                    .delete(createBody(t)).url(url()).build()).execute()
        }, consumer).execute()
    }

    companion object {
        val address = "http://192.168.1.40:8080"
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")!!
        val mapper = ObjectMapper()
        var device = ""
        var user = User()
    }

    internal fun asString(a: Any): String = mapper.writeValueAsString(a)

    internal fun createBody(a: Any): RequestBody = RequestBody.create(JSON, asString(a))

}


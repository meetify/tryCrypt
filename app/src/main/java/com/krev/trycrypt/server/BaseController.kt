package com.krev.trycrypt.server

import android.os.AsyncTask
import com.fasterxml.jackson.databind.ObjectMapper
import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier
import com.krev.trycrypt.model.Id
import com.krev.trycrypt.model.entity.BaseEntity
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*

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

    open fun get(ids: ArrayList<Id>, consumer: Consumer<List<T>>) {
        Task(Supplier<List<T>> {
            mapper.readValue(client.newCall(Request.Builder()
                    .url(url() + "&ids=${asString(ids)}")
                    .get().build()).execute().body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    internal fun url() = "$address/${array[0].javaClass.name.toLowerCase()}?device=$device"

    open fun post(t: T) {
        Task(Supplier<Any> {
            client.newCall(Request.Builder()
                    .post(createBody(t)).url(url()).build()).execute()
        }, null).execute()
    }

    open fun put(t: T) {
        Task(Supplier<Any> {
            client.newCall(Request.Builder()
                    .put(createBody(t)).url(url()).build()).execute()
        }, null).execute()
    }

    open fun delete(t: T) {
        Task(Supplier<Any> {
            client.newCall(Request.Builder()
                    .delete(createBody(t)).url(url()).build()).execute()
        }, null).execute()
    }

    companion object {
        var address = "http://192.168.1.40:8080"
        var client = OkHttpClient()
        var JSON = MediaType.parse("application/json; charset=utf-8")
        var mapper = ObjectMapper()
        var device = ""

    }

    internal fun asString(a: Any): String = mapper.writeValueAsString(a)

    internal fun createBody(a: Any): RequestBody = RequestBody.create(JSON, asString(a))

    internal class Task<V>(private var supplier: Supplier<V>,
                           private var consumer: Consumer<V>?) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Nothing? {
            return consumer?.accept(supplier.accept()).let { null }
        }
    }

}


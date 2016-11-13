package com.krev.trycrypt.server

import android.os.AsyncTask
import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier
import com.krev.trycrypt.model.Id
import com.krev.trycrypt.model.entity.Login
import com.krev.trycrypt.model.entity.User
import com.vk.sdk.VKAccessToken
import okhttp3.Request

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = Login::class.java.name

    fun login(consumer: Consumer<Boolean>, mac: String, token: VKAccessToken) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), token.accessToken, mac)
            val response = client.newCall(Request.Builder().url("$address/login")
                    .post(createBody(login)).build()).execute()
            response.code() == 200
        }, consumer).execute()
    }

    fun check(consumer: Consumer<Boolean>, mac: String, token: VKAccessToken) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), "", mac)
            val response = client.newCall(Request.Builder()
                    .url("$address/login?v=${asString(login)}")
                    .get().build()).execute().body().string().trim()
            response.toBoolean()
        }, consumer).execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val response = client.newCall(Request.Builder().url("$address/user")
                        .post(createBody(user)).build()).execute()
                consumer.accept(response.code() == 200)
                return null
            }
        }.execute()
    }
}
package com.krev.trycrypt.server

import android.os.AsyncTask
import android.util.Log
import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.model.Id
import com.krev.trycrypt.model.entity.Login
import com.krev.trycrypt.model.entity.User
import com.krev.trycrypt.server.BaseController
import com.vk.sdk.VKAccessToken
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = Login::class.java.name

    fun login(consumer: Consumer<Boolean>, mac: String, token: VKAccessToken) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg voids: Void?): Void? {
                val login = Login(Id(token.userId.toLong()), token.accessToken, mac)
                val response = client.newCall(Request.Builder().url(address + "/login")
                        .post(RequestBody.create(JSON, mapper.writeValueAsString(login))).build()).execute()
                Log.d(TAG, "doInBackground: " + response)
                Log.d(TAG, "doInBackground: " + response.body().string())
                Log.d(TAG, "doInBackground: " + response.headers())
                consumer.accept(response.code() == 200)
                return null
            }
        }.execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val response = client.newCall(Request.Builder().url(address + "/user")
                        .post(RequestBody.create(JSON, mapper.writeValueAsString(user))).build()).execute()
                Log.d(TAG, "doInBackground: " + response)
                Log.d(TAG, "doInBackground: " + response.body().string())
                Log.d(TAG, "doInBackground: " + response.headers())
                consumer.accept(response.code() == 200)
                return null
            }
        }.execute()
    }
}
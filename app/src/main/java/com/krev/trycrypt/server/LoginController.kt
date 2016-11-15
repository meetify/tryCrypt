package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier
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
            Log.d("LoginController", "$address/login?v=${asString(login)}");
            val response = client.newCall(Request.Builder()
                    .url("$address/login?v=${asString(login)}")
                    .get().build()).execute()
            response.code() == 200 && response.body().string().trim().toBoolean()
        }, consumer).execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        BaseController.user = user
        UserController.post(user, Consumer {
            Log.d(TAG, "accepting something ${it.code()} ${it.headers()}")
            Log.d(TAG, "accepting something ${it.code()} ${it.request().url()}")
            consumer.accept(it.code() == 200)
        })
//        Task(Supplier {
//            client.newCall(Request.Builder().url("$address/user?device=$device")
//                    .post(createBody(user)).build()).execute().code() == 200
//        }, consumer).execute()
    }
}
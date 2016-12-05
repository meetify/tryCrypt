package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.async.Consumer
import com.krev.trycrypt.utils.async.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = Login::class.java.name

    fun login(consumer: Consumer<Boolean>) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), token.accessToken, device)
            val response = client.newCall(Request.Builder().url("$address/login")
                    .post(body(login)).build()).execute()
            response.code() == 200
        }, consumer).execute()
    }

    fun check(consumer: Consumer<Boolean>) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), "", device)
            Log.d("LoginController", "$address/login?v=${json(login)}")
            val response = client.newCall(Request.Builder()
                    .url("$address/login?v=${json(login)}")
                    .get().build()).execute()
            response.code() == 200 && response.body().string().trim().toBoolean()
        }, consumer).execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        Config.user.modify(user)
        UserController.post(user, Consumer {
            consumer.accept(it.code() == 200)
        })
//        Task(Supplier {
//            client.newCall(Request.Builder().url("$address/user?device=$device")
//                    .post(createBody(user)).build()).execute().code() == 200
//        }, consumer).execute()
    }
}
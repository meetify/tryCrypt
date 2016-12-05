package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = LoginController::class.java.name

    fun login(consumer: Consumer<Boolean>) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), token.accessToken, device)
            val response = client.newCall(Request.Builder().url("$address/login")
                    .post(body(login)).build()).execute()
            response.code() == 200
        }, consumer).execute()
    }

    fun check(consumer: Consumer<User>) {
        Task(Supplier {
            val login = Login(Id(token.userId.toLong()), "", device)
            Log.d("LoginController", "$address/login?v=${json(login)}")
            val response = client.newCall(Request.Builder()
                    .url("$address/login?v=${json(login)}")
                    .get().build()).execute()
            val u = mapper.readValue(response.body().string(), User::class.java)
            Log.d("LoginController", "got user ${mapper.writeValueAsString(u)}")
            u
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
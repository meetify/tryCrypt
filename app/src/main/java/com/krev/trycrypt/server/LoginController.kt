package com.krev.trycrypt.server

import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.JsonAlias.Companion.json
import com.krev.trycrypt.utils.Supplier
import com.krev.trycrypt.utils.async.Task

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = LoginController::class.java.name

    fun login(consumer: Consumer<Boolean>) {
        Task(Supplier {
            request(Method.POST, url(), body(login())).code() == 200
        }, consumer).execute()
    }

    fun check(consumer: Consumer<User>) {
        Task(Supplier {
            json(request(Method.GET, url("&v=${json(login())}")).body().string(), User::class.java)
        }, consumer).execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        Config.user.modify(user)
        UserController.post(user, Consumer {
            consumer.accept(it.code() == 200)
        })
    }

    fun login(): Login = Login(Id(token.userId.toLong()), token.accessToken, device)
}
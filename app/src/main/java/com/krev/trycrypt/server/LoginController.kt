package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.Task
import com.krev.trycrypt.utils.async.TaskKotlin
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier
import com.meetify.server.model.entity.UserExtended
import okhttp3.RequestBody

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = LoginController::class.java.name

    fun createLogin(consumer: Consumer<Boolean>) {
        Task(Supplier {
            request(Method.POST, url(), body(createLogin())).code() == 200
        }, consumer).execute()
    }

    fun check(consumer: Consumer<User>) {
        Task(Supplier {
            json(request(Method.GET, url("&v=${json(createLogin())}")).body().string(), User::class.java)
        }, consumer).execute()
    }

    fun register(consumer: Consumer<Boolean>, user: User) {
        Config.user.modify(user)
        UserController.post(user, Consumer {
            consumer.accept(it.code() == 200)
        })
    }

    fun login(consumer: (UserExtended) -> Unit = {}, user: User = Config.user, login: Login = createLogin()) {
        Log.d(TAG, "in login")
        TaskKotlin({
            json(request(Method.POST, url(path = "/auto"), body(user, login)).body().string(), UserExtended::class.java)
        }, consumer).execute()
    }

    fun body(vararg elems: Any): RequestBody {
        val obj = StringBuilder("{").apply { elems.forEach { append("\"${it.javaClass.simpleName.toLowerCase()}\":${json(it)},") } }
        val string = obj.toString().trimEnd(',')
        println(string + "}")
        return RequestBody.create(Config.JSON, string + "}")
    }

    fun name(any: Any) = any.javaClass.simpleName.toLowerCase()

    fun createLogin(): Login = Login(Id(token.userId.toLong()), token.accessToken, device)
}

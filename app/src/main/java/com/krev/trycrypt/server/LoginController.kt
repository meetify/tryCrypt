package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.UserExtended
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.TaskKotlin
import okhttp3.RequestBody

/**
 * Created by Dima on 12.11.2016.
 */
object LoginController : BaseController<Login>(Array(1, { Login() })) {

    private val TAG = LoginController::class.java.name

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

    fun createLogin(): Login = Login(device = device, id = token.userId.toLong(), token = token.accessToken)
}

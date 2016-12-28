package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.device
import com.krev.trycrypt.application.Config.token
import com.krev.trycrypt.server.model.UserExtended
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.server.model.entity.Login
import com.krev.trycrypt.utils.AsyncUtils.asyncThread
import com.krev.trycrypt.utils.JsonUtils.read
import com.krev.trycrypt.utils.JsonUtils.writeArray
import okhttp3.RequestBody

object LoginController : BaseController<Login>(Array(1, { Login() })) {

    fun login() = asyncThread {
        Log.d("LoginController", "logging in")
        request(Method.POST, url(), body(Config.user, createLogin())).body().string()
    }.thenApplyAsync { read(it, UserExtended::class.java) }!!

    private fun <V : BaseEntity> body(vararg items: V): RequestBody
            = RequestBody.create(Config.JSON, writeArray(items))

    fun createLogin(): Login = Login(device, token.userId.toLong(), token.accessToken)
}

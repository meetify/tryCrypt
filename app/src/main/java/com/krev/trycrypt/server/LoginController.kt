package com.krev.trycrypt.server

import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.Config.device
import com.krev.trycrypt.model.Config.token
import com.krev.trycrypt.model.UserExtended
import com.krev.trycrypt.model.entity.Login
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read

object LoginController : BaseController<Login>(Array(1, { Login() })) {

    fun login() = asyncThread {
        request(Method.POST, url(), body(Config.user, createLogin()))
    }.thenApplyAsync { read(it, UserExtended::class.java) }!!

    fun createLogin(): Login = Login(device, token.userId.toLong(), token.accessToken)
}

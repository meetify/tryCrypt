package com.krev.trycrypt.server

import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.AsyncUtils.asyncThread
import com.krev.trycrypt.utils.JsonUtils.read
import okhttp3.Response

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: (List<User>) -> Unit) = asyncThread {
        read(request(Method.GET, url("", "/friends")).body().string(), array.javaClass).asList()
    }.thenApply(consumer)!!

    fun places(consumer: (List<Place>) -> Unit = {}) = PlaceController.get(user.created + user.allowed).thenApply(consumer)!!

    fun update(location: MeetifyLocation, consumer: (Response) -> Unit = {}) = asyncThread {
        request(Method.POST, url(path = "/update"), body(location))
    }.thenApply(consumer)!!
}
package com.krev.trycrypt.server

import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: (List<User>) -> Unit) = asyncThread {
        read(request(Method.GET, url(path = "/friends")), array.javaClass).asList()
    }.thenApplyAsync(consumer)!!

    fun places(consumer: (List<Place>) -> Unit = {}) = PlaceController.get(user.created + user.allowed.keys)
            .thenApplyAsync(consumer)!!

    fun update(location: MeetifyLocation) = asyncThread {
        request(Method.POST, url(path = "/update"), body(location))
    }

    fun unvisited() = asyncThread {
        read(request(Method.GET, url(path = "/unvisited")), PlaceController.array.javaClass).asList()
    }
}
package com.krev.trycrypt.server

import com.krev.trycrypt.model.Config.user
import com.krev.trycrypt.model.UserExtended
import com.krev.trycrypt.model.entity.MeetifyLocation
import com.krev.trycrypt.model.entity.User
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends() = asyncThread {
        read(request(Method.GET, url(path = "/friends")), array.javaClass).asList()
    }

    fun places() = PlaceController.get(user.created + user.allowed.keys)

    fun update(location: MeetifyLocation) = asyncThread {
        request(Method.POST, url(path = "/update"), body(location))
    }

    fun update() = asyncThread {
        read(request(Method.GET, url(path = "/extended")), UserExtended::class.java)
    }

    fun unvisited() = asyncThread {
        read(request(Method.GET, url(path = "/unvisited")), PlaceController.array.javaClass).asList()
    }
}
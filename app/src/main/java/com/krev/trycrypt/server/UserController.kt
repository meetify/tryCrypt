package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.JsonAlias.Companion.json
import com.krev.trycrypt.utils.Supplier
import com.krev.trycrypt.utils.async.Task

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: Consumer<List<User>>) {
        Task(Supplier {
            json(request(Method.GET, url("", "/friends")).body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    fun allowed(consumer: Consumer<List<Place>>) {
        PlaceController.get(user.allowed, consumer)
    }

    fun created(consumer: Consumer<List<Place>>) {
        PlaceController.get(user.created, consumer)
    }

    fun update(location: MeetifyLocation, consumer: Consumer<in Any> = Consumer<Any> {}) {
        Log.d("UserController", "Update ${json(location)}")
        Task(Supplier { request(Method.POST, url(path = "/update"), body(location)) }, consumer).execute()
    }
}
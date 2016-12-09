package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.Task
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: Consumer<List<User>>) {
        Task(Supplier {
            json(request(Method.GET, url("", "/friends")).body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    fun allowed(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.allowed, consumer)
    }

    fun created(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.created, consumer)
    }

    fun places(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.created + user.allowed, consumer)
    }

    fun update(location: MeetifyLocation, consumer: Consumer<in Any> = Consumer<Any> {}) {
        Log.d("UserController", "Update ${json(location)}")
        Task(Supplier { request(Method.POST, url(path = "/update"), body(location)) }, consumer).execute()
    }
}
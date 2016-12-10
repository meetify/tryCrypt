package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.TaskKotlin
import okhttp3.Response

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: (List<User>) -> Unit) = TaskKotlin({
        json(request(Method.GET, url("", "/friends")).body().string(), array.javaClass).asList()
    }, consumer).execute()!!


    @Deprecated("Use UserController.places instead.", ReplaceWith("UserController.places(consumer)"))
    fun allowed(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.allowed, consumer)
    }

    @Deprecated("Use UserController.places instead.", ReplaceWith("UserController.places(consumer)"))
    fun created(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.created, consumer)
    }

    fun places(consumer: (List<Place>) -> Unit) {
        PlaceController.get(user.created + user.allowed, consumer)
    }

    fun update(location: MeetifyLocation, consumer: (Response) -> Unit = {}) {
        Log.d("UserController", "Update ${json(location)}")
        TaskKotlin({ request(Method.POST, url(path = "/update"), body(location)) }, consumer).execute()
    }
}
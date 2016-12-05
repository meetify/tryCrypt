package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request

object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: Consumer<List<User>>) {
        Task(Supplier {
            val url = url("/friends")
            Log.d("UserController", url)
            mapper.readValue(client.newCall(Request.Builder()
                    .url(url)
                    .get().build()).execute().body().string(), array.javaClass).asList()
        }, consumer).execute()
    }

    fun allowed(consumer: Consumer<List<Place>>) {
        PlaceController.get(user.allowed, consumer)
    }

    fun created(consumer: Consumer<List<Place>>) {
        PlaceController.get(user.created, consumer)
    }
}
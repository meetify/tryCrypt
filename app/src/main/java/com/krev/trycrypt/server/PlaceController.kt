package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read
import com.krev.trycrypt.util.JsonUtils.write
import java8.util.concurrent.CompletableFuture

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(location: MeetifyLocation): CompletableFuture<GooglePlace> = asyncThread {
        Log.d("PlaceController", "PlaceController.nearby.async")
        read(request(Method.GET, url("&location=${write(location)}", "/nearby")), GooglePlace::class.java)
    }

}
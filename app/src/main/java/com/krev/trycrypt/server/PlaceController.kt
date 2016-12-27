package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.AsyncUtils.asyncThread
import com.krev.trycrypt.utils.JsonUtils.read
import com.krev.trycrypt.utils.JsonUtils.write
import java8.util.concurrent.CompletableFuture

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(location: MeetifyLocation): CompletableFuture<GooglePlace> = asyncThread {
        Log.d("PlaceController", "PlaceController.nearby.async")
        read(request(Method.GET, url("&location=${write(location)}", "/nearby")).body().string(), GooglePlace::class.java)
    }
//            .thenApplyAsync {
//                Log.d("PlaceController", "PlaceController.nearby.thenApplyAsync")
//                read(it, GooglePlace::class.java)
//            }

}
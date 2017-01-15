package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.model.GooglePlace
import com.krev.trycrypt.model.GooglePlaceDetailed
import com.krev.trycrypt.model.entity.MeetifyLocation
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.read
import com.krev.trycrypt.util.JsonUtils.write
import java8.util.concurrent.CompletableFuture

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(location: MeetifyLocation): CompletableFuture<GooglePlace> = asyncThread {
        Log.d("PlaceController", "PlaceController.nearby.async")
        read(request(Method.GET, url("&location=${write(location)}", "/nearby")), GooglePlace::class.java)
    }

    fun textQuery(query: String, location: MeetifyLocation): CompletableFuture<GooglePlace> = asyncThread {
        Log.d("PlaceController", "PlaceController.nearby.async")
        read(request(Method.GET, url("&location=${write(location)}&query=$query", "/text")), GooglePlace::class.java)
    }

    fun details(placeId: String): CompletableFuture<GooglePlaceDetailed> = asyncThread {
        Log.d("PlaceController", "PlaceController.nearby.async")
        read(request(Method.GET, url("&placeId=$placeId", "/details")), GooglePlaceDetailed::class.java)
    }
}
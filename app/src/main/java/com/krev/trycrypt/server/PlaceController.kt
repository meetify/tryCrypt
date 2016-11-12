package com.krev.trycrypt.server

import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier
import com.krev.trycrypt.model.GooglePlace
import com.krev.trycrypt.model.entity.Location
import com.krev.trycrypt.model.entity.Place
import okhttp3.Request

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(consumer: Consumer<GooglePlace>,
               location: Location) {
        Task(Supplier {
            GooglePlace.mapper.readValue(client
                    .newCall(Request.Builder()
                            .url("$address/place/nearby?location=${asString(location)}")
                            .get().build()).execute().body().string(), GooglePlace::class.java)
        }, consumer).execute()
    }
}
package com.krev.trycrypt.server

import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.Location
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier
import okhttp3.Request

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(consumer: Consumer<GooglePlace>,
               location: Location) {
        Task(Supplier {
            GooglePlace.mapper.readValue(client
                    .newCall(Request.Builder()
                            .url("$address/place/nearby?location=${json(location)}")
                            .get().build()).execute().body().string(), GooglePlace::class.java)
        }, consumer).execute()
    }
}
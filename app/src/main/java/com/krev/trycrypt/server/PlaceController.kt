package com.krev.trycrypt.server

import android.util.Log
import com.krev.trycrypt.application.Config.address
import com.krev.trycrypt.application.Config.client
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.async.Consumer
import com.krev.trycrypt.utils.async.Supplier
import com.krev.trycrypt.utils.async.Task
import okhttp3.Request

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(consumer: Consumer<GooglePlace>,
               location: MeetifyLocation) {
        Task(Supplier {
            Log.d("GooglePlace", "supplier is starting")
            val readValue = client
                    .newCall(Request.Builder()
                            .url("$address/place/nearby?location=${json(location)}")
                            .get().build()).execute().body().string()
            Log.d("GooglePlace", "supplier request is ok")
            val readValue1 = mapper.readValue(readValue, GooglePlace::class.java)
            Log.d("GooglePlace", "supplier returned")
            readValue1
        }, consumer).execute()
    }
}
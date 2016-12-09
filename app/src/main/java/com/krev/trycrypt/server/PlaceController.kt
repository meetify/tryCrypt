package com.krev.trycrypt.server

import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.Task
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier

object PlaceController : BaseController<Place>(Array(1, { Place() })) {

    fun nearby(consumer: Consumer<GooglePlace>,
               location: MeetifyLocation) {
        Task(Supplier {
            json(request(Method.GET, url("&location=${json(location)}", "/nearby")).body().string(),
                    GooglePlace::class.java)
        }, consumer).execute()
    }
}
package com.krev.trycrypt.util.mapbox

import com.krev.trycrypt.server.model.GooglePlace
import com.mapbox.mapboxsdk.annotations.Marker

/**
 * Created by Dima on 06.12.2016.
 */
class CustomMarker(options: CustomMarkerOptions = CustomMarkerOptions()) : Marker(options) {
    var place = GooglePlace.Result()
}
package com.krev.trycrypt.utils.mapbox

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng

/**
 * Created by Dima on 07.12.2016.
 */
class CameraPositionJson(val bearing: Double = 0.0,
                         val target: TargetJson = TargetJson(),
                         val tilt: Double = 0.0,
                         val zoom: Double = 15.0) {
    fun build() = CameraPosition.Builder()
            .bearing(bearing).tilt(tilt).zoom(zoom)
            .target(LatLng(target.latitude, target.longitude, target.altitude)).build()!!
}
package com.krev.trycrypt.application

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.LoginActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.JsonAlias.Companion.json
import com.krev.trycrypt.utils.async.ImageTask
import com.krev.trycrypt.utils.mapbox.CameraPositionJson
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.vk.sdk.VKAccessToken
import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.util.*

/**
 * Created by Dima on 04.12.2016.
 */
object Config {
    val address = "http://192.168.1.40:8080"
    val client = OkHttpClient()
    val JSON = MediaType.parse("application/json; charset=utf-8")!!
    val mapper = jacksonObjectMapper()
    val token: VKAccessToken by lazy { VKAccessToken.currentToken()!! }

    val context: Context by lazy { MainApplication.context!! }
    val settings: SharedPreferences by lazy { context.getSharedPreferences("MeetifyProps", 0) }
    val user: User by lazy {
        settings.getString("user", "").let {
            User().apply {
                if (it != "") {
                    modify(mapper.readValue(it, User::class.java))
                }
                Log.d("Config", "User $it")
                if (photo.trim() != "" && LoginActivity.icon == null) {
                    Log.d("Config", "getting photo")
                    ImageTask(Consumer { LoginActivity.icon = it }, "user_${id.id}").execute(photo)
                }
            }
        }
    }
    val device: String by lazy {
        settings.getString("device", UUID.randomUUID().toString()).apply {
            settings.edit().putString("device", this).commit()
        }
    }
    val camera: CameraPosition by lazy {
        json(settings.getString("camera", context.getString(R.string.cameraJson)), CameraPositionJson::class.java).map()
    }

    fun modify(user: User) {
        Log.d("Config", "modifying with ${user.photo} ${LoginActivity.icon}")
        if (user.photo != "" && LoginActivity.icon == null) {
            ImageTask(Consumer { LoginActivity.icon = it }, "user_${user.id.id}").execute(user.photo)
        }
        this.user.modify(user)
        val uString = mapper.writeValueAsString(Config.user)
        Log.d("LoginActivity", uString)
        settings.edit().putString("user", uString).apply()
    }

    val locationManager by lazy { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    fun locationService() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 50F, object : LocationListener {
            override fun onProviderDisabled(p0: String?) {
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onLocationChanged(location: Location) {
                Log.d("LocationManager", "Current location is ${location.latitude} ${location.longitude}")
                UserController.update(MapActivity.convert(location))
            }
        })
        Log.d("Config", "Location manager initialized")
    }
}

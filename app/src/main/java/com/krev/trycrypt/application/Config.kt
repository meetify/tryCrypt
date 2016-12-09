package com.krev.trycrypt.application

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.LoginActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.ImageTask
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.mapbox.CameraPositionJson
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.vk.sdk.VKAccessToken
import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Dima on 04.12.2016.
 */
object Config {
    //    val address = "http://192.168.1.40:8080"
    val address = "http://159.224.206.172:49323"
    val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()!!
    val JSON = MediaType.parse("application/json; charset=utf-8")!!
    val mapper = jacksonObjectMapper()
    val token: VKAccessToken by lazy { VKAccessToken.currentToken()!! }

    val context: Context by lazy { MainApplication.context!! }
    val settings: SharedPreferences by lazy { context.getSharedPreferences("MeetifyProps", 0) }
    val device: String by lazy {
        settings.getString("device", UUID.randomUUID().toString()).apply {
            settings.edit().putString("device", this).commit()
        }
    }
    val locationManager by lazy { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    val camera: CameraPosition by lazy {
        json(settings.getString("camera", context.getString(R.string.cameraJson)), CameraPositionJson::class.java).map()
    }
    val bitmap: Bitmap by lazy { BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher) }
    val layoutInflater: LayoutInflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
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

    var friends: Set<User> = HashSet()
    var album: Long = -1
    var places: Set<Place> = HashSet()
    var markers: List<MarkerOptions> = makeMarkers()
    var activity: AppCompatActivity? = null

    fun modify(user: User) {
        Log.d("Config", "modifying with ${user.photo} ${LoginActivity.icon}")
        if (user.photo != "" && LoginActivity.icon == null) {
            ImageTask(Consumer { LoginActivity.icon = it }, "user_${user.id.id}").execute(user.photo)
        }
        Config.user.modify(user)
        val uString = json(Config.user)
        settings.edit().putString("user", uString).apply()
    }

    fun init() {
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

    fun makeMarkers() = places.map {
        MarkerOptions()
                .icon(IconFactory.getInstance(context).fromDrawable(context.getDrawable(R.drawable.ic_place_custom)))
                .position(MapActivity.convert(it.location))
                .title(it.name)
    }

    fun makeMarker(place: Place) = MarkerOptions()
            .icon(IconFactory.getInstance(context).fromDrawable(context.getDrawable(R.drawable.ic_place_custom)))
            .position(MapActivity.convert(place.location))
            .title(place.name)!!

    fun add(place: Place): Config {
        markers += makeMarker(place)
        places += place
        return this
    }

    fun findUser(id: Id): User = if (user.id == id) user
    else friends.filter { it.id == id }[0]
}

package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.flipboard.bottomsheet.BottomSheetLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.GooglePlaceAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.camera
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.GooglePlace.GoogleLocation
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.JsonUtils.write
import com.krev.trycrypt.utils.mapbox.CustomMarkerOptions
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.victor.loading.rotate.RotateLoading


@SuppressLint("InflateParams")
class MapActivity : AppCompatActivity() {

    private val mapView: MapView by lazy { findViewById(R.id.mapView) as MapView }
    private val rotate: RotateLoading by lazy { findViewById(R.id.rotateloading) as RotateLoading }
    private val bottomSheet by lazy { findViewById(R.id.bottomsheet) as BottomSheetLayout }

    private var lock = false


    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        MapboxAccountManager.start(this, getString(R.string.accessToken))
        setContentView(R.layout.activity_map)
        Config.init()
        bottomSheet.setShouldDimContentView(false)

        mapView.getMapAsync { map ->
            mapViewAsync(map)
            findViewById(R.id.button_location).setOnClickListener {
                map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                        .target(convert(Config.location)).zoom(16.0).build()))
            }
        }

        mapView.onCreate(savedInstanceState)
        DrawerUtils.getDrawer(this)
//        bundle = savedInstanceState

    }

    private fun init(savedInstanceState: Bundle?) {

    }

    private fun mapViewAsync(map: MapboxMap) {
        Config.markers = Config.makeMarkers()
        map.addMarkers(Config.markers)
        val view = LayoutInflater.from(this).inflate(R.layout.activity_google_places, bottomSheet, false)
        val listView = view.findViewById(R.id.listViewGPlaces) as ListView
        map.setOnMapClickListener {
            synchronized(lock, {
                if (lock) return@setOnMapClickListener
                lock = true
            })

            rotate.start()
            PlaceController.nearby(MeetifyLocation(it.latitude, it.longitude)).thenApplyAsync {
                if (it.results.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.no_place), LENGTH_SHORT).show()
                        rotate.stop()
                        lock = false
                    }
                    return@thenApplyAsync
                }
                val markers = it.results.map(::CustomMarkerOptions)
                val adapter = GooglePlaceAdapter(it)
                runOnUiThread {
                    map.clear()
                    map.addMarkers(markers + Config.markers)
                    if (bottomSheet.isSheetShowing) {
                        bottomSheet.dismissSheet()
                    }
                    bottomSheet.showWithSheetView(view)
                    listView.adapter = adapter
                    rotate.stop()
                    lock = false
                }
            }
        }


        map.cameraPosition = camera

        map.setOnCameraChangeListener { camera = it }

        map.setOnMapLongClickListener {
            startActivity(Intent(this@MapActivity, PlaceAddActivity::class.java)
                    .putExtra("location", write(convert(it))))
        }

        map.setOnMyLocationChangeListener {
            it?.let {
                Config.location = convert(it)
                UserController.update(Config.location)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        Config.settings.edit()
                .putString("camera", write(camera))
                .putString("user", write(Config.user)).apply()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        fun convert(location: GoogleLocation) = LatLng(location.lat, location.lng)

        fun convert(location: MeetifyLocation) = LatLng(location.latitude, location.longitude)

        fun convert(location: LatLng) = MeetifyLocation(location.latitude, location.longitude)

        fun convert(location: Location) = MeetifyLocation(location.latitude, location.longitude)
    }
}

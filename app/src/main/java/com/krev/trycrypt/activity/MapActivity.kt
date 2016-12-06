package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.GooglePlaceAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.GooglePlace.GoogleLocation
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.mapbox.CustomMarkerOptions
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mingle.sweetpick.CustomDelegate
import com.mingle.sweetpick.SweetSheet

@SuppressLint("InflateParams")
class MapActivity : AppCompatActivity() {

    private var mapView: MapView? = null
    private var lock = false
    var camera = Config.camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxAccountManager.start(this, getString(R.string.accessToken))
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.back)
        MapActivity.activity = this

        val group: ViewGroup = layoutInflater.inflate(R.layout.activity_map, null) as ViewGroup
        val sweetSheet: SweetSheet? = SweetSheet(group)
        val view: View = LayoutInflater.from(this).inflate(R.layout.activity_google_places, group, false)
        val customDelegate: CustomDelegate = CustomDelegate(true, CustomDelegate.AnimationType.DuangAnimation)
        val listView: ListView = view.findViewById(R.id.listViewGPlaces) as ListView
        customDelegate.setCustomView(view)
        sweetSheet!!.setDelegate(customDelegate)
        setContentView(group)

        mapView = findViewById(R.id.mapView) as MapView
        mapView!!.getMapAsync { map ->
            map.setOnMapClickListener {
                synchronized(lock, {
                    if (lock) return@setOnMapClickListener
                    lock = true
                })
                PlaceController.nearby(Consumer<GooglePlace> {
                    Log.d("GooglePlace", "consumer is starting")
                    val adapter = GooglePlaceAdapter(this@MapActivity, it)
                    val markers = it.results.map(::CustomMarkerOptions)
                    Log.d("GooglePlace", "consumer is on ui")
                    runOnUiThread {
                        Log.d("GooglePlace", "consumer is on ui")
                        map.clear()
                        map.addMarkers(markers)
                        if (sweetSheet.isShow) {
                            sweetSheet.dismiss()
                        }
                        listView.adapter = adapter
                        customDelegate.setCustomView(view)
                        sweetSheet.show()
                        lock = false
                        Log.d("GooglePlace", "consumer is well done")
                    }
                }, MeetifyLocation(it.latitude, it.longitude))
            }

            map.cameraPosition = camera

            map.setOnCameraChangeListener { camera = it }
        }
        mapView!!.onCreate(savedInstanceState)
        DrawerUtils.getDrawer(this)

    }

    public override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply()
        mapView!!.onPause()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
        Config.settings.edit().putString("camera", mapper.writeValueAsString(camera)).apply()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }


    companion object {
        var bitmap: Bitmap? = null
        var activity: AppCompatActivity? = null

        fun convert(location: GoogleLocation) = LatLng(location.lat, location.lng)

        fun convert(location: Location) = MeetifyLocation(location.latitude, location.longitude)
    }
}

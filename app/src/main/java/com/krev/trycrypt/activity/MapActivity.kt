package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.GooglePlaceAdapter
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.entity.Location
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.functional.Consumer
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mingle.sweetpick.CustomDelegate
import com.mingle.sweetpick.SweetSheet

@SuppressLint("InflateParams")
class MapActivity : AppCompatActivity() {

    private var mapView: MapView? = null
    private var lock = false

    @SuppressWarnings("ConstantConditions")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxAccountManager.start(this, getString(R.string.accessToken))
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.back)
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
                    val adapter = GooglePlaceAdapter(this@MapActivity, it)
                    val convert = { pos: GooglePlace.GoogleLocation -> LatLng(pos.lat, pos.lng) }
                    val markers = it.results.map { MarkerOptions().position(convert(it.geometry.location)) }
                    runOnUiThread {
                        map.clear()
                        map.addMarkers(markers)
                        if (sweetSheet.isShow) {
                            sweetSheet.dismiss()
                        }
                        listView.adapter = adapter
                        customDelegate.setCustomView(view)
                        sweetSheet.show()
                        lock = false
                    }
                }, Location(it.latitude, it.longitude))
            }

            map.cameraPosition = CameraPosition.Builder().bearing(0.0).tilt(0.0).zoom(15.0).target(LatLng(48.514308545, 35.0879165)).build()

            map.setOnMapLongClickListener { }
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    companion object {
        var profile: ProfileDrawerItem? = null
        var bitmap: Bitmap? = null
    }
}

package com.krev.trycrypt.activity

import android.annotation.SuppressLint
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
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.DrawerUtils
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mingle.sweetpick.CustomDelegate
import com.mingle.sweetpick.SweetSheet

class MapActivity : AppCompatActivity() {

    private var mapView: MapView? = null
    private var sweetSheet: SweetSheet? = null
    private var customDelegate: CustomDelegate? = null
    private var view: View? = null
    private var listView: ListView? = null

    @SuppressWarnings("ConstantConditions")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapboxAccountManager.start(this, getString(R.string.accessToken))
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.back)

        @SuppressLint("InflateParams")
        val group = layoutInflater.inflate(R.layout.activity_map, null) as ViewGroup
        sweetSheet = SweetSheet(group)

        customDelegate = CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation)
        view = LayoutInflater.from(this).inflate(R.layout.activity_google_places, group, false)
        customDelegate!!.setCustomView(view)
        sweetSheet!!.setDelegate(customDelegate)
        setContentView(group)

        listView = view!!.findViewById(R.id.listViewGPlaces) as ListView

        mapView = findViewById(R.id.mapView) as MapView
        mapView!!.getMapAsync { mapboxMap ->
            mapboxMap.setOnMapClickListener { point -> this@MapActivity.onMapClick(point) }

            mapboxMap.cameraPosition = CameraPosition.Builder().bearing(0.0).tilt(0.0).zoom(15.0).target(LatLng(48.514308545, 35.0879165)).build()

            mapboxMap.setOnMapLongClickListener { }
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

    fun onMapClick(point: LatLng) {
        PlaceController.nearby(Consumer<GooglePlace> {
            runOnUiThread {
                if (sweetSheet!!.isShow) {
                    sweetSheet!!.dismiss()
                }
                listView!!.adapter = GooglePlaceAdapter(this@MapActivity, it)
                customDelegate!!.setCustomView(view)
                sweetSheet!!.show()
            }
        }, Location(point.latitude, point.longitude))
    }

    companion object {

        private val TAG = MapActivity::class.java.toString()
        var profile: ProfileDrawerItem? = null
        var bitmap: Bitmap? = null
    }
}

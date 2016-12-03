package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcel
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.GooglePlaceAdapter
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.GooglePlace.GoogleLocation
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.TypeMapper
import com.krev.trycrypt.utils.functional.Consumer
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
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
    private var locationManager: LocationManager? = null

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
//                    if (it.results.size == 0) return@Consumer
//                    Log.d("GooglePlace", jacksonObjectMapper().writeValueAsString(it))
                    val adapter = GooglePlaceAdapter(this@MapActivity, it)
                    val markers = it.results.map(::CustomMarkerOptions)
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
                }, MeetifyLocation(it.latitude, it.longitude))
            }

            map.cameraPosition = CameraPosition.Builder().bearing(0.0).tilt(0.0).zoom(15.0).target(LatLng(48.514308545, 35.0879165)).build()

//            map.setOnMapLongClickListener { }
//            map.setOnMarkerClickListener {
//                val marker = it as CustomMarker
//                true
//            }
        }
        mapView!!.onCreate(savedInstanceState)
        DrawerUtils.getDrawer(this)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, object : LocationListener {
            override fun onProviderDisabled(p0: String?) {
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onLocationChanged(location: Location) {

            }

        })
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
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    private class CustomMarkerOptions() : BaseMarkerOptions<CustomMarker, CustomMarkerOptions>() {
        constructor(place: GooglePlace.Result) : this() {//, resources:Resources) : this() {
            position = convert(place.geometry.location)
            title = place.name
            marker.place = place
            icon = IconFactory.getInstance(activity!!.baseContext).fromDrawable(
                    activity!!.getDrawable(TypeMapper.drawable(place.types)))
//            var icon = BitmapFactory.decodeResource(resources, );
        }

        private var marker: CustomMarker = CustomMarker(this)

        override fun getThis(): CustomMarkerOptions = this

        override fun getMarker(): CustomMarker {
            marker.position = position
            marker.snippet = snippet
            marker.title = title
            marker.icon = icon
            return marker
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeParcelable(position, flags)
            out.writeString(snippet)
            out.writeString(title)
            val icon = icon
            out.writeByte((if (icon != null) 1 else 0).toByte())
            if (icon != null) {
                out.writeString(icon.id)
                out.writeParcelable(icon.bitmap, flags)
            }
        }

    }

    private class CustomMarker(options: CustomMarkerOptions) : Marker(options) {
        var place = GooglePlace.Result()
    }

    companion object {
        var profile: ProfileDrawerItem? = null
        var bitmap: Bitmap? = null
        var activity: AppCompatActivity? = null

        fun convert(location: GoogleLocation) = LatLng(location.lat, location.lng)
    }
}

package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.flipboard.bottomsheet.BottomSheetLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapter.GooglePlaceAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.Config.camera
import com.krev.trycrypt.model.Config.context
import com.krev.trycrypt.model.GooglePlace
import com.krev.trycrypt.model.GooglePlace.GoogleLocation
import com.krev.trycrypt.model.entity.MeetifyLocation
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.util.DrawerUtils
import com.krev.trycrypt.util.JsonUtils.write
import com.krev.trycrypt.util.mapbox.CustomMarker
import com.krev.trycrypt.util.mapbox.CustomMarkerOptions
import com.mapbox.mapboxsdk.MapboxAccountManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.victor.loading.rotate.RotateLoading


@SuppressLint("InflateParams")
class MapActivity : AppCompatActivity() {
    private val mapView by lazy { findViewById(R.id.mapView) as MapView }
    val rotate by lazy { findViewById(R.id.map_toolbar_rotateLoading) as RotateLoading }
    private val bottomSheet by lazy { findViewById(R.id.bottomsheet) as BottomSheetLayout }
    private val search by lazy { findViewById(R.id.map_toolbar_search_editText) as EditText }
    private val searchIcon by lazy { findViewById(R.id.map_toolbar_search_icon) as ImageView }
    private var mapbox: MapboxMap? = null
    private val view by lazy { LayoutInflater.from(this).inflate(R.layout.activity_google_places, bottomSheet, false) }
    private val listView by lazy { view.findViewById(R.id.listViewGPlaces) as ListView }

    //todo: map: google place comments              /done/
    //todo: map: google place onClick in listView   /done/
    //todo: map & vk: place comments & VKEvent
    //todo: map: edit place in dialog
    //todo: notification: chat in vk, or send messages, or using VKEvent send invitations

    val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(Config.context)
    var sendLocation: Boolean = sharedPref.getBoolean(SettingsActivity.KEY_LOCATION_TO_SERVER, true)


    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        MapboxAccountManager.start(this, getString(R.string.accessToken))
        setContentView(R.layout.activity_map)
        Config.init()
        bottomSheet.setShouldDimContentView(false)
        //PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
        search.setOnEditorActionListener { _, _, _ ->
            longTask {
                PlaceController.textQuery(search.text.toString(), convert(camera.target))
                        .thenApplyAsync(this::getGooglePlace)
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            true
        }
        searchIcon.setOnClickListener {
            longTask {
                PlaceController.textQuery(search.text.toString(), convert(camera.target))
                        .thenApplyAsync(this::getGooglePlace)
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

        mapView.getMapAsync { map ->
            mapbox = map
            Config.markers = Config.makeMarkers()
            map.addMarkers(Config.markers)

            Log.d("MapActivity", "mapView.getMapAsync")
            map.setOnMapClickListener {
                Log.d("MapActivity", "setOnMapClickListener")
                longTask {
                    Log.d("MapActivity", "setOnMapClickListener.longTask")
                    PlaceController.nearby(MeetifyLocation(it.latitude, it.longitude))
                            .thenApplyAsync(this::getGooglePlace)
                }
            }

            map.setOnMarkerClickListener {
                if (it !is CustomMarker) {
                    return@setOnMarkerClickListener true
                }
                Log.d("MapActivity", "setOnMarkerClickListener")
                it.tag.apply { showDialog(AlertDialog.Builder(this@MapActivity)) }
                true
            }

            map.cameraPosition = camera

            map.setOnCameraChangeListener { camera = it }

            map.setOnMapLongClickListener {
                startActivity(Intent(this@MapActivity, PlaceAddActivity::class.java)
                        .putExtra("location", write(convert(it))))
            }


            placesUpdate = { map.addMarkers(Config.makeMarkers()) }
            if (sendLocation) {
                map.setOnMyLocationChangeListener {
                    it?.let {
                        Config.location = convert(it)
                        UserController.update(Config.location)
                    }
                }
            } else map.setOnMyLocationChangeListener(null)


        }
        mapView.onCreate(savedInstanceState)
        DrawerUtils.getDrawer(this)
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

    private fun getGooglePlace(it: GooglePlace) {
        Log.d("MapActivity", "getGooglePlace")
        if (it.results.isEmpty()) {
            runOnUiThread {
                Toast.makeText(context, getString(R.string.no_place), LENGTH_SHORT).show()
                rotate.stop()
            }
            return
        }
        Log.d("MapActivity", "getGooglePlace.AfterIf")
        val markers = it.results.map(CustomMarkerOptions.Builder::from)
        val adapter = GooglePlaceAdapter(it, { it.apply { showDialog(AlertDialog.Builder(this@MapActivity)) } })
        runOnUiThread {
            Log.d("MapActivity", "getGooglePlace.onUi")
            mapbox!!.clear()
            mapbox!!.addMarkers(markers + Config.markers)
            if (bottomSheet.isSheetShowing) {
                bottomSheet.dismissSheet()
            }
            bottomSheet.showWithSheetView(view)
            listView.adapter = adapter
            rotate.stop()
        }
    }

    fun longTask(fn: () -> Any?) = runOnUiThread {
        synchronized(rotate, { if (rotate.isStart) return@runOnUiThread })
        rotate.start()
        fn()
    }


    companion object {
        fun convert(location: GoogleLocation) = LatLng(location.lat, location.lng)

        fun convert(location: MeetifyLocation) = LatLng(location.latitude, location.longitude)

        fun convert(location: LatLng) = MeetifyLocation(location.latitude, location.longitude)

        fun convert(location: Location) = MeetifyLocation(location.latitude, location.longitude)

        var placesUpdate = { _: Collection<Place> -> }
    }
}

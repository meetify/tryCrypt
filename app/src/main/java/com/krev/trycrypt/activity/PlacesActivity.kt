package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.baoyz.widget.PullRefreshLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.PlacesAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.utils.DrawerUtils

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
        val places = PlacesAdapter().add(Config.places)
        (findViewById(R.id.listViewPlaces) as ListView).adapter = places

        val layout = findViewById(R.id.places_swipeRefreshLayout) as PullRefreshLayout

        Log.d("PlacesActivity", "getting places")
        layout.setOnRefreshListener {
            UserController.places({
                runOnUiThread {
                    Log.d("PlacesActivity", "got places $it")
                    places.clear(it)
                    layout.setRefreshing(false)
                }
            })
        }
    }
}

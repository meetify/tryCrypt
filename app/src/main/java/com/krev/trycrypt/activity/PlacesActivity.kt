package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import com.baoyz.widget.PullRefreshLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapter.PlacesAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.util.DrawerUtils

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
        val places = PlacesAdapter().add(Config.places)
        (findViewById(R.id.listViewPlaces) as ListView).adapter = places

        val layout = findViewById(R.id.places_swipeRefreshLayout) as PullRefreshLayout

        layout.setOnRefreshListener {
            UserController.places().thenApplyAsync {
                runOnUiThread {
                    val given = it.toHashSet()
                    Log.d("PlacesActivity", "updating with delta ${places.count - given.count()})")
                    places.clear(given)
                    Config.places = given
                    layout.setRefreshing(false)
                }
            }
        }
    }
}

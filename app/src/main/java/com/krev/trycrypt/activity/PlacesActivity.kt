package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.baoyz.widget.PullRefreshLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.PlacesAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.functional.Consumer

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
        val places = PlacesAdapter().add(Config.places)
        (findViewById(R.id.listViewPlaces) as ListView).adapter = places

        val layout = findViewById(R.id.places_swipeRefreshLayout) as PullRefreshLayout

        layout.setOnRefreshListener {
            var count = 0
            UserController.allowed(Consumer {
                synchronized(count, {
                    runOnUiThread {
                        if (count == 0) places.clear(it)
                        else places.add(it)
                        count++
                        if (count == 2) layout.setRefreshing(false)
                    }
                })
            })
            UserController.allowed(Consumer {
                synchronized(count, {
                    runOnUiThread {
                        if (count == 0) places.clear(it)
                        else places.add(it)
                        count++
                        if (count == 2) layout.setRefreshing(false)
                    }
                })
            })

        }
    }
}

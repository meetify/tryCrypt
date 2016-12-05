package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.PlacesAdapter
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.async.Consumer

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
        val places = PlacesAdapter(this)
        UserController.created(Consumer { places.add(it) })
        UserController.allowed(Consumer { places.add(it) })
        (findViewById(R.id.listViewPlaces) as ListView).adapter = places
    }
}

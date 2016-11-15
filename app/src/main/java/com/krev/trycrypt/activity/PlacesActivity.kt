package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.PlacesAdapter
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.functional.Consumer
import java.util.*

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
        val places = PlacesAdapter(ArrayList<Place>(), this)
        UserController.created(Consumer { places.addAll(it) })
        UserController.allowed(Consumer { places.addAll(it) })
        (findViewById(R.id.listViewPlaces) as ListView).adapter = places
    }
}

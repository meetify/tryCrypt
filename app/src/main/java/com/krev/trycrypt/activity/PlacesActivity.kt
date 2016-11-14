package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krev.trycrypt.R
import com.krev.trycrypt.utils.DrawerUtils

class PlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        DrawerUtils.getDrawer(this)
    }
}

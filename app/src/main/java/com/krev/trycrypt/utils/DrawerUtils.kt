package com.krev.trycrypt.utils

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.FriendsActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.activity.PlacesActivity
import com.krev.trycrypt.server.BaseController
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import java.net.URL

/**
 * Created by Dima on 14.11.2016.
 */
object DrawerUtils {
    fun getDrawer(activity: AppCompatActivity): Drawer {
        val headerResult = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(activity.resources.getDrawable(R.mipmap.back))
                .addProfiles(MapActivity.profile)
                .withOnAccountHeaderListener({ view, profile, currentProfile -> false }).build()

        val map = PrimaryDrawerItem().withName("Map").withIdentifier(1)
        val places = PrimaryDrawerItem().withName("My places").withIdentifier(2)
        val friends = PrimaryDrawerItem().withName("My friends").withIdentifier(3)

        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(map, places, friends)
                .withOnDrawerListener(object : Drawer.OnDrawerListener {
                    override fun onDrawerOpened(drawerView: View) {
                    }

                    override fun onDrawerClosed(drawerView: View) {
                    }

                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    }
                }).withOnDrawerItemClickListener { view, position, drawerItem ->
            when (position) {
                1 -> activity.startActivity(Intent(activity, MapActivity::class.java))
                2 -> activity.startActivity(Intent(activity, MapActivity::class.java))
                3 -> activity.startActivity(Intent(activity, MapActivity::class.java))
            }
            activity.startActivity(Intent(activity,
                    when (position) {
                        1 -> MapActivity::class.java
                        2 -> PlacesActivity::class.java
                        3 -> FriendsActivity::class.java
                        else -> return@withOnDrawerItemClickListener true
                    }))
            true
        }.build()
    }

    fun getProfile(): ProfileDrawerItem = ProfileDrawerItem()
            .withName(BaseController.user.name)
            .withIcon(BitmapFactory.decodeStream(URL(BaseController.user.photo).openStream())).apply {
        MapActivity.profile = this
    }
}
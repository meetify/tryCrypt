package com.krev.trycrypt.utils

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.FriendsActivity
import com.krev.trycrypt.activity.LoginActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.activity.PlacesActivity
import com.krev.trycrypt.application.Config
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem

/**
 * Created by Dima on 14.11.2016.
 */
object DrawerUtils {
    fun getDrawer(activity: AppCompatActivity): Drawer {
        val headerResult = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.mipmap.back)
                .addProfiles(profile)
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

    val profile: ProfileDrawerItem by lazy { profile() }
    val icon: Bitmap by lazy { LoginActivity.icon!! }

    //todo: async? or something like this to solve network on main thread exception
    fun profile(): ProfileDrawerItem = ProfileDrawerItem()
            .withName(Config.user.name)
//            .withIcon(BitmapFactory.decodeStream(URL(Config.user.photo).openStream())).apply {
            .withIcon(icon
//                    run { val task = ImageTask(Consumer {}, "user_${Config.user.id.id}").execute(Config.user.photo) }
            ).apply {
//            .withIcon(MapActivity.bitmap).apply {
        PhotoCache.create("user_${Config.user.id.id}", icon.bitmap)
    }
}
package com.krev.trycrypt.util

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.FriendsActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.activity.PlacesActivity
import com.krev.trycrypt.application.Config.user
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem

object DrawerUtils {
    fun getDrawer(activity: AppCompatActivity): Drawer {
        val headerResult = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.mipmap.back)
                .addProfiles(profile)
                .build()

        val map = PrimaryDrawerItem().withName("Map").withIdentifier(1).withIcon(R.drawable.ic_map).withDescription(R.string.drawer_map_tip).withBadge(holder)
        val places = PrimaryDrawerItem().withName("Places").withIdentifier(2).withIcon(R.drawable.ic_place).withDescription(R.string.drawer_places_tip)
        val friends = PrimaryDrawerItem().withName("Friends").withIdentifier(3).withIcon(R.drawable.ic_person).withDescription(R.string.drawer_friends_tip)

        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(map, places, friends)
//                .withOnDrawerListener(object : Drawer.OnDrawerListener {
//                    override fun onDrawerOpened(drawerView: View) {
//                    }
//
//                    override fun onDrawerClosed(drawerView: View) {
//                    }
//
//                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//                    }
//                })
                .withOnDrawerItemClickListener { _, position, _ ->
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

    val profile: ProfileDrawerItem by lazy {
        ProfileDrawerItem()
                .withName(user.name)
                .withIcon(PhotoUtils.getAwait(user.photo, user.id))
    }
//
//    @Deprecated("", ReplaceWith("ProfileDrawerItem().withName(user.name).withIcon(PhotoUtils.getAwait(user.photo, user.id))", "com.mikepenz.materialdrawer.model.ProfileDrawerItem", "com.krev.trycrypt.application.Config.user", "com.krev.trycrypt.application.Config.user", "com.krev.trycrypt.application.Config.user"))
//    private fun profile(): ProfileDrawerItem = ProfileDrawerItem()
//            .withName(user.name)
//            .withIcon(PhotoUtils.getAwait(user.photo, user.id))

    val holder = StringHolder("")
}
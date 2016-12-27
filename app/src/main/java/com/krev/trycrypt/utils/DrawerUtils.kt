package com.krev.trycrypt.utils

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.FriendsActivity
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.activity.PlacesActivity
import com.krev.trycrypt.application.Config.user
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem

object DrawerUtils {
    fun getDrawer(activity: AppCompatActivity): Drawer {
        val headerResult = AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.mipmap.back)
                .addProfiles(profile).build()

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
                })
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

    val profile: ProfileDrawerItem by lazy { profile() }

    fun profile(): ProfileDrawerItem {
//        val view = ImageView(Config.context)
//        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
//            override fun set(imageView: ImageView?, uri: Uri?, placeholder: Drawable?, tag: String?) {
//                Log.d("DrawerUtils", "profile.DrawerImageLoader.set $uri $tag")
//                Picasso.with(Config.context).load(uri).placeholder(placeholder).tag(tag).
//            }
//        }).setImage(view, Uri.parse(user.photo), DrawerImageLoader.Tags.ACCOUNT_HEADER.name)
        return ProfileDrawerItem()
                .withName(user.name)
                .withIcon(PhotoUtils.getAwait(user.photo, user.id))

    }
}
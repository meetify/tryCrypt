package com.krev.trycrypt.adapters

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.PhotoUtils
import com.mapbox.mapboxsdk.camera.CameraPosition

class PlacesAdapter : CustomAdapter<Place>() {
    override var layout: Int = R.layout.listview_place

    override fun photo(item: Place, view: ImageView) = PhotoUtils.getAwait(item.photo, item.id, view)

    override fun View.holder() = this.apply {
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView),
                (findViewById(R.id.place_name) as TextView),
                (findViewById(R.id.place_creator_icon) as ImageView),
                (findViewById(R.id.place_creator_name) as TextView))
    }

    override fun View.data(item: Place) = this.apply {
        setOnClickListener {
            Config.camera = CameraPosition.Builder().target(MapActivity.convert(item.location)).zoom(16.0).build()
            Config.activity!!.startActivity(Intent(Config.activity!!, MapActivity::class.java))
            Config.activity!!.finish()
        }
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            Config.findUser(item.owner).apply { PhotoUtils.getAwait(item.photo, item.id, creatorIcon) }
            creatorName.text = Config.findUser(item.owner).name
        }
    }

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val creatorIcon: ImageView,
                          val creatorName: TextView)
}
package com.krev.trycrypt.adapters

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.BitmapUtils
import com.krev.trycrypt.utils.PhotoCache
import com.mapbox.mapboxsdk.camera.CameraPosition

/**
 * Created by Dima on 14.11.2016.
 */

class PlacesAdapter : CustomAdapter<Place>() {
    override fun layout(): Int = R.layout.listview_place

    override fun photo(item: Place) = item.photo

    override fun View.viewHolder(item: Place): ViewHolder {
        setOnClickListener {
            MapActivity.camera = CameraPosition.Builder().target(MapActivity.convert(item.location)).zoom(16.0).build()
            Config.activity!!.startActivity(Intent(Config.activity!!, MapActivity::class.java))
            Config.activity!!.finish()
        }
        return ViewHolder().apply {
            tag = ViewHolder(
                    (findViewById(R.id.place_icon) as ImageView)
                            .apply { setImageDrawable(BitmapUtils.getRoundedDrawable(photos[item]!!)) },
                    (findViewById(R.id.place_name) as TextView)
                            .apply { text = item.name },
                    (findViewById(R.id.place_creator_icon) as ImageView)
                            .apply { setImageDrawable(BitmapUtils.getRoundedDrawable((PhotoCache.get("user_${item.owner}"))!!)) },
                    (findViewById(R.id.place_creator_name) as TextView)
                            .apply { text = Config.findUser(item.owner).name })
        }
    }

    data class ViewHolder(var icon: ImageView? = null,
                          var name: TextView? = null,
                          var creatorIcon: ImageView? = null,
                          var creatorName: TextView? = null)
}



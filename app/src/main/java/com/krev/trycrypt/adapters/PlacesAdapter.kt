package com.krev.trycrypt.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.server.model.entity.Place

/**
 * Created by Dima on 14.11.2016.
 */

class PlacesAdapter : CustomAdapter<Place>() {
    override fun layout(): Int = R.layout.place

    override fun photo(item: Place) = item.photo

    override fun View.viewHolder(item: Place) = ViewHolder().apply {
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView)
                        .apply { setImageBitmap(photos[item]) },
                (findViewById(R.id.place_name) as TextView)
                        .apply { text = item.name })
    }

    data class ViewHolder(var icon: ImageView? = null,
                          var name: TextView? = null)
}

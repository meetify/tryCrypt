package com.krev.trycrypt.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.model.GooglePlace
import com.krev.trycrypt.util.PhotoUtils

class GooglePlaceAdapter(place: GooglePlace, val onViewClickListener: (GooglePlace.Result) -> Unit)
    : CustomAdapter<GooglePlace.Result>(place.results.sortedByDescending { it.rating }.toMutableList()) {

    override val layout: Int = R.layout.listview_google_place

    override fun photo(item: GooglePlace.Result, view: ImageView) =
            PhotoUtils.get(item.photos[0].photoReference, item.id, view, this::notifyDataSetChanged)

    override fun View.holder() = apply {
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView),
                (findViewById(R.id.place_name) as TextView),
                (findViewById(R.id.place_address) as TextView),
                (findViewById(R.id.place_types) as TextView))
    }

    override fun View.data(item: GooglePlace.Result) = apply {
        setOnClickListener { onViewClickListener(item) }
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            rating.text = item.vicinity
            address.text = if (item.rating > 1.0) item.rating.toString() else ""
        }
    }

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val address: TextView,
                          val rating: TextView)
}

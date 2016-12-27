package com.krev.trycrypt.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.GooglePlace.Result
import com.krev.trycrypt.utils.PhotoUtils

class GooglePlaceAdapter(place: GooglePlace) : CustomAdapter<Result>(place.results.toMutableList()) {

    override val layout: Int = R.layout.listview_google_place

    override fun photo(item: Result, view: ImageView) =
            PhotoUtils.getAwait(item.photos[0].photoReference, item.id, view)

    override fun View.holder() = apply {
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView),
                (findViewById(R.id.place_name) as TextView),
                (findViewById(R.id.place_address) as TextView),
                (findViewById(R.id.place_types) as TextView))
    }

    override fun View.data(item: Result) = apply {
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            address.text = item.vicinity
            rating.text = if (item.rating > 1.0) item.rating.toString() else ""
        }
    }

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val address: TextView,
                          val rating: TextView)
}

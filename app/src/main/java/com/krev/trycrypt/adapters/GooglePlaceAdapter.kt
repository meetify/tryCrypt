package com.krev.trycrypt.adapters

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.layoutInflater
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.GooglePlace.Result
import com.krev.trycrypt.utils.BitmapUtils
import com.krev.trycrypt.utils.async.ImageTask
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dima on 28.10.2016.
 */
class GooglePlaceAdapter private constructor(
        private var indices: List<Result>,
        private val photos: ConcurrentHashMap<Result, Bitmap> = ConcurrentHashMap<Result, Bitmap>())
    : BaseAdapter() {

    constructor(googlePlace: GooglePlace) : this(googlePlace.results) {
        indices = googlePlace.results.sortedBy { -it.rating }
        photos.apply {
            googlePlace.results.forEach {
                put(it, Config.bitmap)
                ImageTask(({ bitmap ->
                    put(it, bitmap)
                    notifyDataSetChanged()
                }), "place_${it.id}").execute(it.photos[0].photoReference)
            }
        }
    }

    override fun getCount(): Int = photos.size

    override fun getItem(i: Int): Result = indices[i]

    override fun getItemId(i: Int): Long = 0

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            layoutInflater.inflate(R.layout.listview_google_place, viewGroup, false)).apply {
        val place = indices[position]
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView)
                        .apply { setImageDrawable(BitmapUtils.getRoundedDrawable(photos[place]!!)) },
                (findViewById(R.id.place_name) as TextView)
                        .apply { text = place.name },
                (findViewById(R.id.place_address) as TextView)
                        .apply { text = place.vicinity },
                (findViewById(R.id.place_types) as TextView)
                        .apply { text = if (place.rating > 1.0) place.rating.toString() else "" })
    }!!

    private data class ViewHolder(
            internal var icon: ImageView? = null,
            internal var name: TextView? = null,
            internal var address: TextView? = null,
            internal var types: TextView? = null)
}
package com.krev.trycrypt.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.server.model.GooglePlace.Result
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.async.ImageTask
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dima on 28.10.2016.
 */
class GooglePlaceAdapter private constructor(
        private val googlePlace: GooglePlace,
        private val activity: Activity,
        private val layoutInflater: LayoutInflater = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
        private val places: ConcurrentHashMap<Result, Bitmap> = ConcurrentHashMap<Result, Bitmap>(),
        private val indices: List<Result> = googlePlace.results) : BaseAdapter() {

    constructor(activity: Activity, googlePlace: GooglePlace) : this(googlePlace, activity) {
        places.apply {
            googlePlace.results.forEach {
                put(it, MapActivity.bitmap!!)
                ImageTask(Consumer({ bitmap ->
                    put(it, bitmap)
                    notifyDataSetChanged()
                }), "place_${it.id}").execute(it.photos[0].photoReference)
            }
        }
    }

    override fun getCount(): Int = places.size

    override fun getItem(i: Int): Result = indices[i]

    override fun getItemId(i: Int): Long = 0

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            layoutInflater.inflate(R.layout.google_place, viewGroup, false)).apply {
        val place = indices[position]
        tag = ViewHolder(
                (findViewById(R.id.place_icon) as ImageView)
                        .apply { setImageBitmap(places[place]) },
                (findViewById(R.id.place_name) as TextView)
                        .apply { text = place.name },
                (findViewById(R.id.place_address) as TextView)
                        .apply { text = place.formattedAddress },
                (findViewById(R.id.place_types) as TextView)
                        .apply { text = place.types.toString() })
    }!!

    private data class ViewHolder(
            internal var icon: ImageView? = null,
            internal var name: TextView? = null,
            internal var address: TextView? = null,
            internal var types: TextView? = null)
}
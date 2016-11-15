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
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.DownloadImageTask
import com.krev.trycrypt.utils.functional.Consumer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dima on 28.10.2016.
 */
class FriendsAdapter(
        private val activity: Activity,
        private val layoutInflater: LayoutInflater = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
        private val photos: ConcurrentHashMap<User, Bitmap> = ConcurrentHashMap<User, Bitmap>(),
        private val users: MutableList<User> = ArrayList()) : BaseAdapter() {

    override fun getCount(): Int = photos.size

    override fun getItem(i: Int): User = users[i]

    fun add(list: Collection<User>) = list.forEach {
        users.add(it)
        photos.put(it, MapActivity.bitmap!!)
        DownloadImageTask(Consumer { bitmap ->
            photos.put(it, bitmap)
            notifyDataSetChanged()
        }, "user_${it.id}").execute(it.photo)
    }

    override fun getItemId(i: Int): Long = 0

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            layoutInflater.inflate(R.layout.user, viewGroup, false)).apply {
//        Log.d(TAG, "getView: " + position)
        val place = users[position]
        tag = ViewHolder(
                (findViewById(R.id.user_icon) as ImageView)
                        .apply { setImageBitmap(photos[place]) },
                (findViewById(R.id.user_name) as TextView)
                        .apply { text = place.name })
    }!!

    private data class ViewHolder(
            internal var icon: ImageView? = null,
            internal var name: TextView? = null)
}
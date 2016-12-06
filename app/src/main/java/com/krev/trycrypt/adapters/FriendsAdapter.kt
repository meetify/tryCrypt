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
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.async.ImageTask
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
        ImageTask(Consumer { bitmap ->
            photos.put(it, bitmap)
            notifyDataSetChanged()
        }, "user_${it.id}").execute(it.photo)
    }

    override fun getItemId(i: Int): Long = 0

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            layoutInflater.inflate(R.layout.user, viewGroup, false)).apply {
        val user = users[position]
        tag = ViewHolder().apply {
            icon = (findViewById(R.id.user_icon) as ImageView).apply { setImageBitmap(photos[user]) }
            name = (findViewById(R.id.user_name) as TextView).apply { text = user.name }
            online = (findViewById(R.id.user_online) as ImageView).apply {
                setImageResource(if (System.currentTimeMillis() - user.time <= 15 * 60 * 1000)
                    R.drawable.ic_online
                else
                    R.drawable.ic_offline
                )
            }
        }
    }!!

    private data class ViewHolder(
            internal var icon: ImageView? = null,
            internal var name: TextView? = null,
            internal var online: ImageView? = null)
}
package com.krev.trycrypt.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.BitmapUtils

/**
 * Created by Dima on 28.10.2016.
 */
class FriendsAdapter() : CustomAdapter<User>() {
    override fun layout(): Int = R.layout.user

    override fun photo(item: User) = item.photo

    override fun View.viewHolder(item: User) = ViewHolder().apply {
        icon = (findViewById(R.id.user_icon) as ImageView).apply {
            setImageDrawable(BitmapUtils.getRoundedDrawable(photos[item]!!))
        }
        name = (findViewById(R.id.user_name) as TextView).apply { text = item.name }
        online = (findViewById(R.id.user_online) as ImageView).apply {
            setImageResource(if (System.currentTimeMillis() - item.time <= 15 * 60 * 1000)
                R.drawable.ic_online
            else
                R.drawable.ic_offline
            )
        }
    }

    data class ViewHolder(var icon: ImageView? = null,
                          var name: TextView? = null,
                          var online: ImageView? = null)
}
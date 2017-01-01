package com.krev.trycrypt.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.util.PhotoUtils

class FriendsAdapter : CustomAdapter<User>() {
    override val layout: Int = R.layout.listview_user

    override fun photo(item: User, view: ImageView) = PhotoUtils.get(item.photo, item.id, view, this::notifyDataSetChanged)

    override fun View.holder() = apply {
        tag = ViewHolder((findViewById(R.id.user_icon) as ImageView),
                (findViewById(R.id.user_name) as TextView),
                (findViewById(R.id.user_online) as ImageView))
    }

    override fun View.data(item: User) = apply {
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            online.setImageResource(if (online(item)) R.drawable.ic_online else R.drawable.ic_offline)
        }
    }

    private fun online(item: User) = System.currentTimeMillis() - item.time <= 15 * 60 * 1000

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val online: ImageView)
}
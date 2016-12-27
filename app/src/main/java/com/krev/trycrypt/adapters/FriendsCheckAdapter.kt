package com.krev.trycrypt.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.PhotoUtils
import java.util.*

class FriendsCheckAdapter : CustomAdapter<User>() {
    override var layout: Int = R.layout.listview_user_checkbox

    override fun photo(item: User, view: ImageView) = PhotoUtils.getAwait(item.photo, item.id, view)

    override fun View.data(item: User) = apply {
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            check.setOnClickListener {
                if (check.isChecked) checked += item.id
                else checked -= item.id
            }
            setOnClickListener {
                check.isChecked = !check.isChecked
                check.callOnClick()
            }
        }
    }

    override fun View.holder() = apply {
        tag = ViewHolder((findViewById(R.id.user_check_icon) as ImageView),
                (findViewById(R.id.user_check_name) as TextView),
                (findViewById(R.id.user_check_checkbox) as CheckBox))
    }

    var checked = HashSet<Long>() + Config.friends.map(User::id)

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val check: CheckBox)
}
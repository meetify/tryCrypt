package com.krev.trycrypt.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.util.PhotoUtils
import java.util.*

class FriendsCheckAdapter : CustomAdapter<User>() {
    override var layout: Int = R.layout.listview_user_checkbox

    override fun photo(item: User, view: ImageView) = PhotoUtils.get(item.photo, item.id, view, this::notifyDataSetChanged)

    override fun View.data(item: User) = apply {
        (tag as ViewHolder).apply {
            photo(item, icon)
            name.text = item.name
            check.setOnClickListener {
                if (check.isChecked) checked.put(item.id, false)
                else checked.remove(item.id)
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

    var checked: HashMap<Long, Boolean> = HashMap<Long, Boolean>().apply {
        putAll(Config.friends.map(User::id).map { it.to(false) })
    }

    data class ViewHolder(val icon: ImageView,
                          val name: TextView,
                          val check: CheckBox)
}
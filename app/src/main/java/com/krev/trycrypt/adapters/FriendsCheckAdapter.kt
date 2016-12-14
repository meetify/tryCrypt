package com.krev.trycrypt.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.User
import java.util.*

/**
 * Created by Dima on 07.12.2016.
 */
class FriendsCheckAdapter : CustomAdapter<User>() {
    override fun layout(): Int = R.layout.listview_user_checkbox

    override fun photo(item: User) = item.photo

    override fun View.viewHolder(item: User): Any = ViewHolder().apply {
        icon = (findViewById(R.id.user_check_icon) as ImageView).apply { setImageBitmap(photos[item]) }
        name = (findViewById(R.id.user_check_name) as TextView).apply { text = item.name }
        check = (findViewById(R.id.user_check_checkbox) as CheckBox).apply {
            setOnClickListener {
                if (isChecked) checked += item.id
                else checked -= item.id
            }
        }
        setOnClickListener {
            check!!.isChecked = !check!!.isChecked
            check!!.callOnClick()
        }
    }

    var checked = HashSet<Long>() + Config.friends.map(User::id)

    data class ViewHolder(var icon: ImageView? = null,
                          var name: TextView? = null,
                          var check: CheckBox? = null)
}
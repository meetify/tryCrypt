package com.krev.trycrypt.model.entity

import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.adapter.ReplyAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.Viewable
import com.krev.trycrypt.util.PhotoUtils
import java.io.Serializable
import java.util.*

class Place(var name: String = "",
            var description: String = "",
            val owner: Long = -1,
            val photo: String = "",
            var location: MeetifyLocation = MeetifyLocation(),
            var allowed: MutableMap<Long, Boolean> = HashMap<Long, Boolean>(),
            override var id: Long = -1) : BaseEntity(id), Serializable, Viewable {
    override fun MapActivity.showDialog(builder: AlertDialog.Builder) {
        val view = layoutInflater.inflate(R.layout.dialog_information_place, null)

        val photo = view.findViewById(R.id.place_info_image) as ImageView
        val location = view.findViewById(R.id.place_info_location_text) as TextView
        val creator = view.findViewById(R.id.place_info_creator_text) as TextView
        val allowed = view.findViewById(R.id.place_info_friends_text) as TextView
        val time = view.findViewById(R.id.place_info_time_text) as TextView
        val name = view.findViewById(R.id.place_info_name_text) as TextView

        val listView = view.findViewById(R.id.place_info_time_listview) as ListView
        val adapter = ReplyAdapter()
        listView.adapter = adapter

        val stringBuilder = StringBuilder()
        this@Place.allowed.forEach { stringBuilder.append("${Config.findUser(it.key).name}, ") }
        allowed.text = stringBuilder.toString().trim(' ').trim(',')
        creator.text = Config.findUser(owner).name
        name.text = this@Place.name
        PhotoUtils.get(this@Place.photo, id, photo, round = false)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
    }

}


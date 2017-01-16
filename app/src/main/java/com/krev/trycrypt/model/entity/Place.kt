package com.krev.trycrypt.model.entity

import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.adapter.ReplyAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.Viewable
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.util.BitmapUtils
import com.krev.trycrypt.util.PhotoUtils
import com.krev.trycrypt.vk.VKEvent
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Place(var name: String = "",
            var description: String = "",
            val owner: Long = -1,
            var photo: String = "",
            var location: MeetifyLocation = MeetifyLocation(),
            var allowed: MutableMap<Long, Boolean> = HashMap<Long, Boolean>(),
            var date: Long = 0,
            var vkEvent: Long = 0,
            override var id: Long = -1) : BaseEntity(id), Serializable, Viewable {
    override fun MapActivity.showDialog(builder: AlertDialog.Builder) {
        Log.d("MapActivity", "Place.showDialog")
        val view = layoutInflater.inflate(R.layout.dialog_information_place, null)

        val photo = view.findViewById(R.id.place_info_image) as ImageView
        val location = view.findViewById(R.id.place_info_location_text) as TextView
        val creator = view.findViewById(R.id.place_info_creator_text) as TextView
        val allowed = view.findViewById(R.id.place_info_friends_text) as TextView
        val time = view.findViewById(R.id.place_info_time_text) as TextView
        val name = view.findViewById(R.id.place_info_name_text) as TextView

        allowed.isFocusable = false
        time.isFocusable = false
        if (Config.user.id != owner) {
            location.isFocusable = false
            creator.isFocusable = false
            name.isFocusable = false
            allowed.isClickable = false
            time.isClickable = false
        } else {
            allowed.isClickable = true
            time.isClickable = true
        }
        allowed.setOnClickListener {

        }
        time.setOnClickListener {
            //            PlaceAddActivity.Companion.apply { getTime() }
//            this@Place.date = PlaceAddActivity.date
        }

        val listView = view.findViewById(R.id.place_info_time_listview) as ListView
        val adapter = ReplyAdapter()
        listView.adapter = adapter

        longTask {
            VKEvent.Event.Builder.from(this@Place).thenApplyAsync {
                PlaceController.nearby(this@Place.location).thenApplyAsync { places ->
                    runOnUiThread {
                        Log.d("VKEvent.Event", "VKEvent.Event.Builder.from")
                        val items = it.comments
                        val pixels = BitmapUtils.toPixels(100f)
                        val size = when (items.count()) {
                            in 0..2 -> pixels * items.count()
                            else -> pixels * 2
                        }
                        adapter.add(items)
                        listView.layoutParams.height = size
                        listView.layoutParams.width = size

                        val stringBuilder = StringBuilder()
                        this@Place.allowed.forEach { stringBuilder.append("${Config.findUser(it.key).name}, ") }

                        allowed.text = stringBuilder.toString().trim(' ').trim(',')
                        creator.text = Config.findUser(owner).name
                        name.text = this@Place.name
                        time.text = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault())
                                .format(Date(this@Place.date))
                        location.text = places.results[0].vicinity

                        Log.d("VKEvent.Event", "showing")
                        PhotoUtils.get(this@Place.photo, id, photo, round = false)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        rotate.stop()
                    }
                }
            }
            true
        }
    }

}


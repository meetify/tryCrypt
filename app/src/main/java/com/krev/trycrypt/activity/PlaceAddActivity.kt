package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.FriendsCheckAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.JsonAlias.Companion.json

class PlaceAddActivity : AppCompatActivity() {

    val friends: ListView by lazy { findViewById(R.id.place_add_friends) as ListView }
    val name: EditText by lazy { findViewById(R.id.place_add_name) as EditText }
    val description: EditText by lazy { findViewById(R.id.place_add_description) as EditText }
    val button: Button by lazy { findViewById(R.id.place_add_button) as Button }
    val location: MeetifyLocation by lazy { json(intent.getStringExtra("location"), MeetifyLocation::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_add)
        friends.adapter = FriendsCheckAdapter().add(Config.friends)
        button.setOnClickListener {
            PlaceController.put(Place(name.text.toString(),
                    description.text.toString(),
                    Config.user.id,
                    "",
                    location,
                    (friends.adapter as FriendsCheckAdapter).checked))
            finish()
        }
    }
}

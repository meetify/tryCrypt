package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.FriendsAdapter
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.DrawerUtils

class FriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        DrawerUtils.getDrawer(this)
        val friends = FriendsAdapter(this)
        UserController.friends(Consumer { friends.add(it) })
        (findViewById(R.id.listViewFriends) as ListView).adapter = friends
    }
}

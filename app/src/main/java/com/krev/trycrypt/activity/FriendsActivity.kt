package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.FriendsAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.functional.Consumer

class FriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        DrawerUtils.getDrawer(this)
        val friends = FriendsAdapter()
        UserController.friends(Consumer {
            Config.friends += it
            friends.add(it)
        })
        (findViewById(R.id.listViewFriends) as ListView).adapter = friends
    }
}

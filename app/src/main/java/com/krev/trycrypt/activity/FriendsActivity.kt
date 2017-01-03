package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.baoyz.widget.PullRefreshLayout
import com.krev.trycrypt.R
import com.krev.trycrypt.adapter.FriendsAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.util.DrawerUtils

class FriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        DrawerUtils.getDrawer(this)
        val friends = FriendsAdapter().add(Config.friends)
        (findViewById(R.id.listViewFriends) as ListView).adapter = friends

        val layout = findViewById(R.id.friends_swipeRefreshLayout) as PullRefreshLayout

        layout.setOnRefreshListener {
            UserController.friends().thenApplyAsync {
                runOnUiThread {
                    val given = it.toHashSet()
                    friends.clear(given)
                    Config.friends = given
                    layout.setRefreshing(false)
                }
            }
        }
    }
}

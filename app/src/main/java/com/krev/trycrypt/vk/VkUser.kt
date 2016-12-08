package com.krev.trycrypt.vk

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.JsonUtils
import com.krev.trycrypt.utils.functional.Consumer
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import java.util.*

/**
 * Created by Dima on 07.12.2016.
 */
object VKUser {
    fun get(consumer: Consumer<User>) {
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val json = response!!.json.getJSONObject("response")
                val friends = parseFriends(json.getJSONArray("items").toString())
                val user = User(Id(json.getLong("id")),
                        MeetifyLocation(), friends, HashSet<Id>(), HashSet<Id>(),
                        "${json.getString("first_name")} ${json.getString("last_name")}",
                        json.getString("photo_50"))
                Log.d("LoginActivity", "register done with ${JsonUtils.json(user)}")
                Config.modify(user)
                consumer.accept(user)
            }
        }
        VKRequest("execute.info").executeWithListener(listener)
    }

    private fun parseFriends(friends: String): HashSet<Id> = HashSet<Id>().apply {
        friends.replace("[\\[\\]]".toRegex(), "")
                .split(",".toRegex())
                .forEach { add(Id(it.toLong())) }
    }
}
package com.krev.trycrypt.vk

import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.util.JsonUtils
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import java8.util.concurrent.CompletableFuture
import org.json.JSONObject
import java.util.*

object VKUser {

    fun get() = CompletableFuture<User>().apply {
        VKRequest("execute.userinfo").executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                Log.d("VKUser", "well done ${response!!.json}")
                val json = response.json.getJSONObject("response")

                val friends = parseFriends(json.getJSONArray("friends").toString())
                val album = json.getLong("album")
                val user = parseUser(json.getJSONObject("user"), friends, album)
                Log.d("VKUser", JsonUtils.write(user))
                Config.modify(user)
                Config.album = album
                Log.d("VKUser", "consumer?")
                complete(user)
            }

            override fun onError(error: VKError?) {
                Log.d("VKUser", "one $error")
            }
        })
        Log.d("VKUser", "execute.userinfo")
    }


    private fun parseFriends(friends: String): HashSet<Long> = HashSet<Long>().apply {
        friends.replace("[\\[\\]]".toRegex(), "")
                .split(",".toRegex())
                .forEach { add(it.toLong()) }
    }

    private fun parseUser(user: JSONObject, vkFriends: HashSet<Long>, album: Long) = User().apply {
        id = user.getLong("id")
        name = "${user["first_name"]} ${user["last_name"]}"
        photo = "${user["photo_100"]}"
        friends = vkFriends
        vkAlbum = album
        time = System.currentTimeMillis()
    }
}
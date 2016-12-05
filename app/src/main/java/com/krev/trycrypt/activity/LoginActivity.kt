package com.krev.trycrypt.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.LoginController
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.PhotoCache
import com.krev.trycrypt.utils.async.Consumer
import com.krev.trycrypt.utils.async.Supplier
import com.krev.trycrypt.utils.async.Task
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import java.util.*

/**
 * try
 * Created by Dima on 15.10.2016.
 */
class LoginActivity : AppCompatActivity() {
    private val finishConsumer = Consumer<ProfileDrawerItem> {
        UserController.get(ArrayList<Id>().apply { add(Id(Config.token.userId.toLong())) },
                Consumer { Config.user.modify(it[0]) })
        startActivity(Intent(this@LoginActivity, MapActivity::class.java))
    }
    private val loginConsumer = Consumer<Boolean> {
        Log.d(TAG, "loginConsumer with $it")
        if (!it) register()
        else loginFinished()
    }
    private val registerConsumer = Consumer<Boolean> {
        Log.d(TAG, "registerConsumer with $it")
        login()
    }
    private val checkConsumer = Consumer<Boolean> {
        Log.d(TAG, "checkingConsumer with $it")
        when {
            it -> loginFinished()
            else -> login()
        }
    }

    private fun check() {
        Log.d(TAG, "check")
        LoginController.check(checkConsumer)
    }

    private fun login() {
        Log.d(TAG, "login")
        LoginController.login(loginConsumer)
    }

    private fun register() {
        Log.d(TAG, "register")
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val json = response!!.json.getJSONObject("response")
                val friends = parseFriends(json.getJSONArray("items").toString())
                val user = User(Id(json.getLong("id")),
                        MeetifyLocation(), friends, HashSet<Id>(), HashSet<Id>(),
                        "${json.getString("first_name")} ${json.getString("last_name")}",
                        json.getString("photo_50"))
                Config.modify(user)
//                ImageTask(Consumer { icon = it }, "user_${Config.user.id.id}").execute(Config.user.photo)
                LoginController.register(registerConsumer, user)
            }
        }
        VKRequest("execute.info").executeWithListener(listener)
    }

    private fun loginFinished() {
        Log.d(TAG, "loginFinished")
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
//                val json = response!!.json.getJSONObject("response")
//                val friends = parseFriends(json.getJSONArray("items").toString())
//                val user = User(Id(json.getLong("id")),
//                        MeetifyLocation(), friends, HashSet<Id>(), HashSet<Id>(),
//                        "${json.getString("first_name")} ${json.getString("last_name")}",
//                        json.getString("photo_50"))
//                Config.user.modify(user)
//                startActivity(Intent(this@LoginActivity, MapActivity::class.java))
                Task(Supplier<ProfileDrawerItem> {
                    DrawerUtils.profile()
                }, finishConsumer).execute()
            }
        }
        if (Config.user.id == Id(0)) {
            VKRequest("execute.info").executeWithListener(listener)
        } else {
            Task(Supplier<ProfileDrawerItem> {
                DrawerUtils.profile
            }, finishConsumer).execute()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("LoginActivity", "onCreate" + mapper.writeValueAsString(user))

        PhotoCache.filesDir = this.filesDir
        Log.d("FilesDir", this.filesDir.absolutePath)
        MapActivity.bitmap = BitmapFactory.decodeResource(resources, R.mipmap.back)
        if (VKSdk.isLoggedIn()) check()
        else VKSdk.login(this, "friends", "photos")
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(request, result, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                check()
            }

            override fun onError(error: VKError) {
            }
        })) {
            super.onActivityResult(request, result, data)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy")
        Config.apply {
            val uString = mapper.writeValueAsString(user)
            Log.d("LoginActivity", uString)
            settings.edit().putString("user", uString).commit()
        }
    }

    companion object {
        private val TAG = LoginActivity::class.java.toString()
        var icon: Bitmap? = null
        private fun parseFriends(friends: String): HashSet<Id> = HashSet<Id>().apply {
            friends.replace("[\\[\\]]".toRegex(), "")
                    .split(",".toRegex())
                    .forEach { add(Id(it.toLong())) }
        }
    }
}

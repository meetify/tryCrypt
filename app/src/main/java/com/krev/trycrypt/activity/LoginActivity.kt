package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.krev.trycrypt.R
import com.krev.trycrypt.cache.PhotoCache
import com.krev.trycrypt.server.BaseController
import com.krev.trycrypt.server.LoginController
import com.krev.trycrypt.server.Task
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.DrawerUtils
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.utils.functional.Supplier
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
        UserController.get(ArrayList<Id>().apply { add(Id(token!!.userId.toLong())) },
                Consumer { BaseController.user = it[0] })
        MapActivity.profile = it
        startActivity(Intent(this@LoginActivity, MapActivity::class.java))
    }
    private val loginConsumer = Consumer<Boolean> {
        Log.d(TAG, "loginConsumer with $it")
        when {
            !it -> register()
            else -> loginFinished()
        }
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

    private fun parseFriends(friends: String): HashSet<Id> = HashSet<Id>().apply {
        friends.replace("[\\[\\]]".toRegex(), "")
                .split(",".toRegex())
                .forEach { add(Id(it.toLong())) }
    }

    private fun check() {
        Log.d(TAG, "check")
        LoginController.check(checkConsumer, mac!!, token!!)
    }

    private fun login() {
        Log.d(TAG, "login")
        LoginController.login(loginConsumer, mac!!, token!!)
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

                LoginController.register(registerConsumer, user)
            }
        }
        VKRequest("execute.info").executeWithListener(listener)
//        VKApiFriends().get(VKParameters()).executeWithListener(listener)
    }

    private fun loginFinished() {
        Log.d(TAG, "loginFinished")
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val json = response!!.json.getJSONObject("response")
                val friends = parseFriends(json.getJSONArray("items").toString())
                val user = User(Id(json.getLong("id")),
                        MeetifyLocation(), friends, HashSet<Id>(), HashSet<Id>(),
                        "${json.getString("first_name")} ${json.getString("last_name")}",
                        json.getString("photo_50"))
                BaseController.user = user
                Task(Supplier<ProfileDrawerItem> {
                    DrawerUtils.getProfile()
                }, finishConsumer).execute()
            }
        }
        if (BaseController.user.id == Id(0)) {
            VKRequest("execute.info").executeWithListener(listener)
        } else {
            Task(Supplier<ProfileDrawerItem> {
                DrawerUtils.getProfile()
            }, finishConsumer).execute()
        }
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        PhotoCache.activity = this
        mac = (getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo.macAddress
        BaseController.device = mac!!
        when {
            !VKSdk.isLoggedIn() -> VKSdk.login(this, "friends", "photos")
            else -> {
                token = VKAccessToken.currentToken()
                check()
            }
        }
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(request, result, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                Log.d(TAG, "everything is fine, going to talk with server")
                token = res
                check()
            }

            override fun onError(error: VKError) {
                Log.d(TAG, "shit happens")
            }
        })) {
            token = VKAccessToken.currentToken()
            super.onActivityResult(request, result, data)
        }
    }

    companion object {
        private val TAG = LoginActivity::class.java.toString()
        private var mac: String? = null
        private var token: VKAccessToken? = null
    }
}

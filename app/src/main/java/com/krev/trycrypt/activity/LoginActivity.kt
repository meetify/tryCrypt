package com.krev.trycrypt.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.krev.trycrypt.R
import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier
import com.krev.trycrypt.model.Id
import com.krev.trycrypt.model.entity.Location
import com.krev.trycrypt.model.entity.User
import com.krev.trycrypt.server.BaseController
import com.krev.trycrypt.server.LoginController
import com.krev.trycrypt.server.Task
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.methods.VKApiFriends
import com.vk.sdk.api.methods.VKApiUsers
import java.net.URL
import java.util.*

/**
 * try
 * Created by Dima on 15.10.2016.
 */
class LoginActivity : AppCompatActivity() {
    private val finishConsumer = Consumer<ProfileDrawerItem> {
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
                val friends = parseFriends(response!!.json
                        .getJSONObject("response").getJSONArray("items").toString())
                val user = User(
                        Id(token!!.userId.toLong()),
                        Location(), friends, HashSet<Id>(), HashSet<Id>())
                Log.d(TAG, "REGISTERING THIS SHIT")
                LoginController.register(registerConsumer, user)
            }
        }
        VKApiFriends().get(VKParameters()).executeWithListener(listener)
    }

    private fun loginFinished() {
        Log.d(TAG, "loginFinished")
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val obj = response!!.json.getJSONArray("response").getJSONObject(0)
                Log.d(TAG, "onComplete: " + obj.toString())

                val name = "${obj.getString("first_name")} ${obj.getString("last_name")}"
                val photo = obj.getString("photo_50")

                Task(Supplier<ProfileDrawerItem> {
                    ProfileDrawerItem()
                            .withName(name)
                            .withIcon(BitmapFactory.decodeStream(URL(photo).openStream()))
                }, finishConsumer).execute()
            }
        }
        val parameters = VKParameters()
        parameters.put("fields", "photo_50")
        VKApiUsers().get(parameters).executeWithListener(listener)
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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

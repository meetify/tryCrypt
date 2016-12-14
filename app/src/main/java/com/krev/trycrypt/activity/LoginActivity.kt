package com.krev.trycrypt.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.github.lzyzsd.circleprogress.DonutProgress
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.LoginController
import com.krev.trycrypt.utils.PhotoCache
import com.krev.trycrypt.vk.VKUser
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError

/**
 * try
 * Created by Dima on 15.10.2016.
 */
class LoginActivity : AppCompatActivity() {

    val progress: DonutProgress by lazy { findViewById(R.id.donut_progress) as DonutProgress }
    val login: TextView by lazy { findViewById(R.id.login_progress) as TextView }


    private fun autoLogin() {
        login.text = "Fetching information about user from VK"
        progress.progress = 30
        VKUser.get({
            runOnUiThread {
                login.text = "Sending something to Meetify's server"
                progress.progress = 60
            }
            LoginController.login({
                runOnUiThread {
                    login.text = "Well done, initiating new life!"
                    progress.progress = 100
                }
                Log.d(TAG, "autologin logged to vk $it")
                Config.friends = it.friends
                Config.places = it.created + it.allowed
                Config.user.created += it.created.map { it.id }
                Config.user.allowed += it.allowed.map { it.id }
                startActivity(Intent(this@LoginActivity, MapActivity::class.java))
                finish()
            })
        }, {
            progress.progress = (it * 100).toInt() / 75
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progress.progress = 0
        login.text = "Trying to login VK"
        Log.d(TAG, "onCreate" + mapper.writeValueAsString(user))

        PhotoCache.filesDir = this.filesDir
        Log.d(TAG, this.filesDir.absolutePath)
        if (VKSdk.isLoggedIn()) autoLogin()
        else VKSdk.login(this, "friends", "photos")
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(request, result, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                autoLogin()
            }

            override fun onError(error: VKError) {
            }
        })) {
            super.onActivityResult(request, result, data)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private val TAG = "LoginActivity"
        var icon: Bitmap? = null
    }
}

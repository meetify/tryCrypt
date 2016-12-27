package com.krev.trycrypt.activity

import android.content.Intent
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
import com.krev.trycrypt.utils.AsyncUtils.asyncThread
import com.krev.trycrypt.utils.DrawerUtils
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

    private val donutProgress: DonutProgress by lazy { findViewById(R.id.donut_progress) as DonutProgress }
    private val login: TextView by lazy { findViewById(R.id.login_progress) as TextView }
    private val TAG = "LoginActivity"

    private fun updateProgress(progress: Int) = runOnUiThread {
        val progressText = getString(when (progress) {
            0 -> R.string.login_progress_0
            30 -> R.string.login_progress_30
            60 -> R.string.login_progress_60
            100 -> R.string.login_progress_100
            else -> R.string.login_progress_unknown
        })
        Log.d(TAG, "Updating progress with $progress - $progressText")
        login.text = progressText
        donutProgress.progress = progress
    }

    private fun autoLogin() {
        updateProgress(30)
        VKUser.get().thenAccept {
            asyncThread { DrawerUtils.profile }
            updateProgress(60)
            LoginController.login {
                updateProgress(100)
                Log.d(TAG, "autologin logged to vk $it")
                Config.friends = it.friends
                Config.places = it.created + it.allowed
                Config.user.created += it.created.map { it.id }
                Config.user.allowed += it.allowed.map { it.id }
                startActivity(Intent(this@LoginActivity, MapActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        updateProgress(0)
        Log.d(TAG, "onCreate" + mapper.writeValueAsString(user))

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
}

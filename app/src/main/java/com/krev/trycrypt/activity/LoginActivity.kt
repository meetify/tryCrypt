package com.krev.trycrypt.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.application.Config.mapper
import com.krev.trycrypt.application.Config.user
import com.krev.trycrypt.server.LoginController
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.server.model.Id
import com.krev.trycrypt.server.model.entity.User
import com.krev.trycrypt.utils.PhotoCache
import com.krev.trycrypt.utils.functional.Consumer
import com.krev.trycrypt.vk.VKUser
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import java.util.*

/**
 * try
 * Created by Dima on 15.10.2016.
 */
class LoginActivity : AppCompatActivity() {

    private fun check() {
        Log.d(TAG, "check")
        //todo 6.12.2016: some better, than simple TextView
        (findViewById(R.id.textViewRegister) as TextView).text = getString(R.string.wait)
        LoginController.check(Consumer<User> {
            Log.d(TAG, "checkingConsumer with $it")
            if (it.id.id != -1.toLong()) {
                loginFinished()
                Config.modify(it)
            } else {
                VKUser.get(Consumer {
                    Log.d("userFromVK", "in consumer")
                    Config.modify(it)
                    login()
                })
            }
        })
    }

    private fun login() {
        Log.d(TAG, "login")
        LoginController.login(Consumer<Boolean> {
            Log.d(TAG, "loginConsumer with $it")
            if (!it) register()
            else loginFinished()

        })
    }

    private fun register() {
        Log.d(TAG, "register")
        LoginController.register(Consumer<Boolean> {
            Log.d(TAG, "registerConsumer with $it")
            login()
        }, user)
    }

    private fun loginFinished() {
        Log.d(TAG, "loginFinished")
        UserController.get(ArrayList<Id>().apply { add(Id(Config.token.userId.toLong())) },
                Consumer { user.modify(it[0]) })
        startActivity(Intent(this@LoginActivity, MapActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate" + mapper.writeValueAsString(user))

        PhotoCache.filesDir = this.filesDir
        Log.d(TAG, this.filesDir.absolutePath)
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
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private val TAG = LoginActivity::class.java.toString()
        var icon: Bitmap? = null
    }
}

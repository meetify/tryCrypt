package com.krev.trycrypt.application

import android.content.Context
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.krev.trycrypt.application.Config.settings
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk

class MainApplication : MultiDexApplication() {
    internal var vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if (newToken == null) {
                VKSdk.logout()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        MainApplication.context = applicationContext

        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)

        settings.all.forEach {
            Log.d("MainApplication", it.key + " | " + it.value)
        }
    }

    companion object {
        var context: Context? = null
    }
}

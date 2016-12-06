package com.krev.trycrypt.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.krev.trycrypt.application.Config.settings
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk

/**
 * Created by Dima on 07.11.2016.
 */

class MainApplication : Application() {
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
        Config.locationService()

        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)

        settings.all.forEach {
            Log.d("MainApplication", it.key + " | " + it.value)
        }
    }

    companion object {
        var context: Context? = null
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("MainApplication", "onTerminate")
    }
}

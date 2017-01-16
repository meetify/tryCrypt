package com.krev.trycrypt.application

import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.krev.trycrypt.model.Config.settings
import com.krev.trycrypt.service.UnvisitedService
import com.krev.trycrypt.service.UpdateService
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
        _context = applicationContext

        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)

        settings.all.forEach {
            Log.d("MainApplication", it.key + " | " + it.value)
        }

        startService(Intent(this, UnvisitedService::class.java))
        startService(Intent(this, UpdateService::class.java))
    }

    companion object {
        private var _context: Context? = null
        val context: Context
            get() = _context!!
    }
}

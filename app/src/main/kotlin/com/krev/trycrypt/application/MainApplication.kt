package com.krev.trycrypt.application

import android.app.Application
import com.vk.sdk.VKSdk

/**
 * Created by kr3v on 03.09.2016.
 */

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}
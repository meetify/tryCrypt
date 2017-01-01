package com.krev.trycrypt.service

import android.app.Service
import android.content.Intent
import android.util.Log
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import java.util.concurrent.TimeUnit

class UpdateService : Service() {
    private val TAG = this.javaClass.toString()
    private var isFinished = false

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        asyncThread {
            while (true) {
                Log.d(TAG, "onStartCommand.asyncThread")
                if (isFinished) {
                    return@asyncThread
                }
                //todo: update places & friends here with
                TimeUnit.MINUTES.sleep(2)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        isFinished = true
        super.onDestroy()
    }
}
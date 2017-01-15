package com.krev.trycrypt.service

import android.app.Service
import android.content.Intent
import android.util.Log
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.model.entity.User
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.JsonUtils.write
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
                UserController.update().thenAcceptAsync {
                    Log.d(TAG, "onStartCommand.asyncThread.update.thenAcceptAsync: ${write(it)}")
                    Config.places = (it.allowed + it.created).toHashSet()
                    Config.user.allowed = it.allowed.associate { it.id.to(false) }
                    Config.user.created = it.created.map { it.id }.toHashSet()
                    Config.user.friends = it.friends.map(User::id).toHashSet()
                    Config.friends = it.friends.toHashSet()
                }
                TimeUnit.MINUTES.sleep(2)
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        isFinished = true
        super.onDestroy()
    }
}
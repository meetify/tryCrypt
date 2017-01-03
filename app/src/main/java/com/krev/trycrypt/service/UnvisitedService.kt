package com.krev.trycrypt.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.krev.trycrypt.R
import com.krev.trycrypt.server.UserController
import com.krev.trycrypt.util.AsyncUtils.asyncThread
import com.krev.trycrypt.util.DrawerUtils
import java.util.concurrent.TimeUnit

class UnvisitedService : Service() {

    private val TAG = this.javaClass.toString()
    private val mId = 123
    private var isFinished = false
    private val mNotificationManager: NotificationManager
            by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        synchronized(Companion, {
            if (launchedCount >= 1) {
                Log.d(TAG, "onStartCommand.launchedCount >= 1 => exiting")
                return super.onStartCommand(intent, flags, startId)
            }
        })
        launchedCount++
        asyncThread {
            while (true) {
                if (isFinished) {
                    return@asyncThread
                }
                UserController.unvisited().thenAcceptAsync {
                    Log.d(TAG, "onStartCommand.asyncThread.unvisited.thenAcceptAsync: $it")
                    if (it.count() == 0) {
                        DrawerUtils.holder.text = ""
                        return@thenAcceptAsync
                    }
                    DrawerUtils.holder.text = "${it.count()} new places"
                    val mBuilder = NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("${it.count()} ${getString(R.string.notification)}")
                    mNotificationManager.notify(mId, mBuilder.build())
                }
                TimeUnit.SECONDS.sleep(sleep)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        isFinished = true
        launchedCount--
        super.onDestroy()
    }

    companion object {
        private var launchedCount = 0
        var sleep = 30L
    }
}

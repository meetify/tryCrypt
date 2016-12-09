package com.krev.trycrypt.utils.async

import android.os.AsyncTask
import android.util.Log

/**
 * Created by Dima on 14.11.2016.
 */
class TaskKotlin<V>(val supplier: () -> V,
                    val consumer: (V) -> Unit) : AsyncTask<V?, V?, V?>() {
    init {
        Log.d("TaskKotlin", "in TaskKotlin")
    }
    override fun doInBackground(vararg p0: V?) = supplier().apply { consumer(this) }
}
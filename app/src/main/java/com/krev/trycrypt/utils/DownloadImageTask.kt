package com.krev.trycrypt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.krev.trycrypt.cache.PhotoCache
import com.krev.trycrypt.utils.functional.Consumer
import java.net.URL

class DownloadImageTask(private val consumer: Consumer<Bitmap>?,
                        private val name: String,
                        private val update: Boolean = false) : AsyncTask<String, Void, Bitmap>() {

    private var check = false

    override fun doInBackground(vararg urls: String): Bitmap {
        check = PhotoCache.check(name.decode())
        return when (check && !update) {
            true -> PhotoCache.getChecked(name.decode())
            else -> BitmapFactory.decodeStream(URL(urls[0]).openStream())
        }
    }

    override fun onPostExecute(result: Bitmap) {
        if (!check || update) PhotoCache.create(name, result)
        consumer?.accept(result)
    }

    private fun String.decode(): String = when (this) {
        "" -> "____"
        else -> this
    }

}

package com.krev.trycrypt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.krev.trycrypt.cache.PhotoCache
import com.krev.trycrypt.utils.functional.Consumer
import java.net.URL

class DownloadImageTask(private val consumer: Consumer<Bitmap>?,
                        private val name: String) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap {
        return if (PhotoCache.check(name.decode())) PhotoCache.getChecked(name.decode()) else BitmapFactory
                .decodeStream(URL(urls[0]).openStream())
    }

    override fun onPostExecute(result: Bitmap) {
        PhotoCache.create(name, result)
        consumer?.accept(result)
    }

    private fun String.decode(): String = when (this) {
        "" -> "___"
        else -> this
    }

}

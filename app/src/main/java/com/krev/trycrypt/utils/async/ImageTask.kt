package com.krev.trycrypt.utils.async

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.krev.trycrypt.utils.PhotoCache
import java.net.URL

class ImageTask(private val consumer: (Bitmap) -> Unit = {},
                private val name: String) : AsyncTask<String, Void, Bitmap>() {

    private var check = false

    override fun doInBackground(vararg urls: String): Bitmap {

        check = PhotoCache.check(name.decode())
        Log.d("ImageTask", "doInBackground >${urls[0]}<|>${name.decode()}")
        return if (check) {
            try {
                PhotoCache.getChecked(name.decode()).apply {
                    Log.d("ImageTask", "got checked")
                }
            } catch(e: Exception) {
                BitmapFactory.decodeStream(URL(urls[0]).openStream()).apply {
                    Log.d("ImageTask", "got exception")
                }
            }
        } else BitmapFactory.decodeStream(URL(urls[0]).openStream()).apply {
            Log.d("ImageTask", "got else")
        }
    }

    override fun onPostExecute(result: Bitmap) {
        if (!check) {
            Log.d("ImageTask", "saving to cache $name")
            PhotoCache.create(name, result)
        }
        consumer(result)
    }


    private fun String.decode(): String = when (this) {
        "" -> "____"
        else -> this
    }
}

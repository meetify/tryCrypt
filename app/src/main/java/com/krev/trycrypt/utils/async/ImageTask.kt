package com.krev.trycrypt.utils.async

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.krev.trycrypt.utils.PhotoCache
import com.krev.trycrypt.utils.functional.Consumer
import java.net.URL

class ImageTask(private val consumer: Consumer<Bitmap>?,
                private val name: String,
                private val update: Boolean = false) : AsyncTask<String, Void, Bitmap>() {

    private var check = false

    override fun doInBackground(vararg urls: String): Bitmap {
//        Log.d("ImageTask", "doInBackground >${urls[0]}<")
        check = PhotoCache.check(name.decode())
        return if (check && !update) {
            try {
                PhotoCache.getChecked(name.decode())
            } catch(e: Exception) {
                BitmapFactory.decodeStream(URL(urls[0]).openStream())
            }
        }
        else BitmapFactory.decodeStream(URL(urls[0]).openStream())
    }

    override fun onPostExecute(result: Bitmap) {
//        Log.d("ImageTask", "onPostExecute")
        if (!check || update) PhotoCache.create(name, result)
        consumer?.accept(result)
    }


    private fun String.decode(): String = when (this) {
        "" -> "____"
        else -> this
    }
}

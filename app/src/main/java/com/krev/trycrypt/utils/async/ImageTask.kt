package com.krev.trycrypt.utils.async

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.krev.trycrypt.utils.PhotoCache
import com.krev.trycrypt.utils.functional.Consumer
import java.net.URL

class ImageTask(private val consumer: Consumer<Bitmap>?,
                private val name: String,
                private val update: Boolean = false) : AsyncTask<String, Void, Bitmap>() {

    private var check = false
    override fun onProgressUpdate(vararg values: Void?) {
        Log.d("ImageTask", "onProgressUpdate")
    }

    override fun onPreExecute() {
        Log.d("ImageTask", "onPreExecute")
        super.onPreExecute()
    }

    override fun onCancelled(result: Bitmap?) {
        Log.d("ImageTask", "onCancelled")
    }

    override fun onCancelled() {
        Log.d("ImageTask", "onCancelled")
    }

    override fun doInBackground(vararg urls: String): Bitmap {
        Log.d("ImageTask", "doInBackground >${urls[0]}<")
        check = PhotoCache.check(name.decode())
        return if (check && !update) PhotoCache.getChecked(name.decode())
        else BitmapFactory.decodeStream(URL(urls[0]).openStream())
    }

    override fun onPostExecute(result: Bitmap) {
        Log.d("ImageTask", "onPostExecute")
        if (!check || update) PhotoCache.create(name, result)
        consumer?.accept(result)
    }


    private fun String.decode(): String = when (this) {
        "" -> "____"
        else -> this
    }
}

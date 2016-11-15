package com.krev.trycrypt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.URL

class DownloadImageTask(val consumer: Consumer<Bitmap>?) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap = BitmapFactory
            .decodeStream(URL(urls[0]).openStream())

    override fun onPostExecute(result: Bitmap) {
        consumer?.accept(result)
    }
}
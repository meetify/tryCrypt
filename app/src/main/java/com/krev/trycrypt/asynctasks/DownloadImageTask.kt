package com.krev.trycrypt.asynctasks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView

class DownloadImageTask(private var bmImage: ImageView,
                        private var consumer: Consumer<ImageView>?) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap = BitmapFactory
            .decodeStream(java.net.URL(urls[0]).openStream())

    override fun onPostExecute(result: Bitmap) {
        bmImage.setImageBitmap(result)
        consumer?.accept(bmImage)
    }
}
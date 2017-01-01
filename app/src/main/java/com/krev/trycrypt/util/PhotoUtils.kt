package com.krev.trycrypt.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import com.krev.trycrypt.application.Config
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object PhotoUtils {
    fun get(url: String, key: Any, imageView: ImageView, consumer: () -> Unit = {}) = Picasso
            .with(Config.context)
            .load(url)
            .stableKey(key.toString())
            .into(object : Target {
                override fun onPrepareLoad(p0: Drawable?) {
                }

                override fun onBitmapFailed(p0: Drawable?) {
                    Toast.makeText(Config.context, "Photo failed ($url)", Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapLoaded(p0: Bitmap?, p1: Picasso.LoadedFrom?) {
                    p0?.let { imageView.setImageDrawable(BitmapUtils.getRoundedDrawable(it)) }
                    consumer()
                }
            })

    fun getAwait(url: String, key: Any) = Picasso
            .with(Config.context)
            .load(url)
            .stableKey(key.toString())
            .get()!!

}
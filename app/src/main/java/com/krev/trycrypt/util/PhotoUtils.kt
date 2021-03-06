package com.krev.trycrypt.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.krev.trycrypt.model.Config
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object PhotoUtils {
    fun get(url: String, key: Any, imageView: ImageView, consumer: () -> Unit = {}, round: Boolean = true)
            = Picasso.with(Config.context)
            .load(url)
            .stableKey(key.toString())
            .into(object : Target {
                override fun onPrepareLoad(p0: Drawable?) {
                }

                override fun onBitmapFailed(p0: Drawable?) {
                    Toast.makeText(Config.context, "Photo failed ($url)", Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapLoaded(p0: Bitmap?, p1: Picasso.LoadedFrom?) {
                    p0?.let {
                        if (round) imageView.setImageDrawable(BitmapUtils.getRoundedDrawable(it))
                        else imageView.setImageBitmap(it)
                    }
                    consumer()
                }
            })

    fun getConsumer(url: String, key: Any, consumer: (Bitmap) -> Unit = {}) {
        Log.d("VKEvent", "PhotoUtils.getConsumer >$url<")
        Picasso.with(Config.context)
                .load(url)
                .stableKey(key.toString())
                .into(object : Target {
                    override fun onPrepareLoad(p0: Drawable?) = Unit

                    override fun onBitmapFailed(p0: Drawable?) {
                        Log.d("VKEvent", "PhotoUtils.getConsumer failed")
                        Toast.makeText(Config.context, "Photo failed ($url)", Toast.LENGTH_SHORT).show()
                    }

                    override fun onBitmapLoaded(p0: Bitmap?, p1: Picasso.LoadedFrom?) {
                        Log.d("VKEvent", "PhotoUtils.getConsumer onBitmapLoaded")
                        p0?.let { consumer(it) }
                    }
                })
    }

    fun getAwait(url: String, key: Any) = Picasso
            .with(Config.context)
            .load(url)
            .stableKey(key.toString())
            .get()!!

}
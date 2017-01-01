package com.krev.trycrypt.util

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.create
import com.krev.trycrypt.application.Config

object BitmapUtils {
    fun getRoundedDrawable(bitmap: Bitmap) = create(Config.context.resources, bitmap).apply {
        cornerRadius = Math.max(bitmap.width, bitmap.height) / 2.0f
    }!!
}
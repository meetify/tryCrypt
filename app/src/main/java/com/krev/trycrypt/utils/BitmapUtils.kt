package com.krev.trycrypt.utils

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import com.krev.trycrypt.application.Config

/**
 * Created by Dima on 09.12.2016.
 */
object BitmapUtils {
    fun getRoundedDrawable(bitmap: Bitmap) = RoundedBitmapDrawableFactory.create(Config.context.resources, bitmap).apply {
        cornerRadius = Math.max(bitmap.width, bitmap.height) / 2.0f
    }
}
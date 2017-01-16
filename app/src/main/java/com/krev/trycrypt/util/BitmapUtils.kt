package com.krev.trycrypt.util

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.create
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import com.krev.trycrypt.model.Config.context

object BitmapUtils {
    fun getRoundedDrawable(bitmap: Bitmap) = create(context.resources, bitmap).apply {
        cornerRadius = Math.max(bitmap.width, bitmap.height) / 2.0f
    }!!

    fun toPixels(float: Float)
            = applyDimension(COMPLEX_UNIT_DIP, float, context.resources.displayMetrics).toInt()
}
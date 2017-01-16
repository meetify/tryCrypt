package com.krev.trycrypt.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dima on 15.01.2017.
 */
object DateUtils {
    fun format(date:Long) = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault()).format(Date(date))
}
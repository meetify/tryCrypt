package com.krev.trycrypt.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Dima on 15.11.2016.
 */
object PhotoCache {
    var filesDir: File? = null

    fun create(name: String, photo: Bitmap) = FileOutputStream(File(filesDir, "/$name")).use {
        photo.compress(Bitmap.CompressFormat.JPEG, 80, it)
    }

    fun get(name: String): Bitmap? = BitmapFactory.decodeFile("${filesDir!!.absolutePath}/$name")

    fun getChecked(name: String): Bitmap = get(name)!!

    fun check(name: String): Boolean = File(filesDir, "/$name").exists()
}
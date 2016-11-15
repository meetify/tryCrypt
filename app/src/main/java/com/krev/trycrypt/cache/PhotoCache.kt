package com.krev.trycrypt.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Dima on 15.11.2016.
 */
object PhotoCache {
    var activity: AppCompatActivity? = null

    fun create(name: String, photo: Bitmap) {
        FileOutputStream(File(activity!!.filesDir, "/$name")).use {
            photo.compress(Bitmap.CompressFormat.JPEG, 80, it)
        }
    }

    fun get(name: String): Bitmap? = BitmapFactory.decodeFile("${activity!!.filesDir.absolutePath}/$name")

    fun getChecked(name: String): Bitmap = get(name)!!

    fun check(name: String): Boolean = File(activity!!.filesDir, "/$name").exists()
}
package com.krev.trycrypt.vk

import android.graphics.Bitmap
import com.krev.trycrypt.application.Config
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.photo.VKImageParameters
import com.vk.sdk.api.photo.VKUploadImage

/**
 * Created by Dima on 07.12.2016.
 */
object VKPhoto {
    fun initAlbum(function: () -> Unit = {}) {
        val finish = { id: Long ->
            Config.album = id
        }
        val create = {
            VKRequest("photos.createAlbum").apply {
                addExtraParameter("title", "meetify")
                addExtraParameter("version", "5.60")
                addExtraParameter("privacy_view", "nobody")
                addExtraParameter("privacy_comment", "nobody")
            }.executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse?) {
                    function()
                    finish(response!!.json.getJSONObject("response").getLong("id"))
                }

                override fun onError(error: VKError?) {
                }
            })
        }
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val items = response!!.json.getJSONObject("response").getJSONArray("items")
                val obj = (0..items.length() - 1)
                        .map { items.getJSONObject(it) }
                        .find { it.getString("title").contains("meetify", true) }
                        ?.getLong("id")
                if (obj != null) finish(obj) else create()
            }
        }
        VKRequest("photos.getAlbums").executeWithListener(listener)
    }

    fun uploadPhoto(function: (String) -> Unit = {}, bitmap: Bitmap) {
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                response!!.json.getJSONArray("response").getJSONObject(0).apply {
                    var photo = optString("photo_807")
                    if (photo == "") photo = optString("photo_604")
                    function(photo)
                }
            }
        }
        VKApi.uploadAlbumPhotoRequest(VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), Config.album, 0).executeWithListener(listener)
    }
}

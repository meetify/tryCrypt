package com.krev.trycrypt.vk

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.utils.async.TaskKotlin
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.photo.VKImageParameters
import com.vk.sdk.api.photo.VKUploadImage
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


/**
 * Created by Dima on 07.12.2016.
 */
object VKPhoto {
    private val TAG = "VKPhoto"

    fun initAlbum(function: () -> Unit = {}) {
        Log.d(TAG, "initAlbum start")

        val finish = { id: Long ->
            Log.d(TAG, "initAlbum finish $id")
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
                    Log.d(TAG, "initAlbum done")
                    function()
                    finish(response!!.json.getJSONObject("response").getLong("id"))
                }

                override fun onError(error: VKError?) {
                    Log.d(TAG, "initAlbum error ${error?.errorMessage} | $error")
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
                Log.d(TAG, "initAlbum listener with $obj")
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
//        VKRequest("photos.getUploadServer").executeWithListener(listener)
//        Log.d(TAG, "uploadPhoto start")
        VKApi.uploadAlbumPhotoRequest(VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), Config.album, 0).executeWithListener(listener)
    }

    private fun photoFinish(response: UploadResponse, function: (String) -> Unit = {}) {
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                response!!.json.getJSONObject("response").apply {
                    var photo = optString("photo_807")
                    if (photo == "") photo = optString("photo_604")
                    Log.d(TAG, "")
                    Log.d(TAG, "photoFinish done")
                    Log.d(TAG, "uploadPhoto done")
                    function(photo)
                }
            }
        }
        VKRequest("photos.getUploadServer").apply {
            addExtraParameter("server", response.server)
            addExtraParameter("photos_list", response.photos_list)
            addExtraParameter("hash", response.hash)
            addExtraParameter("aid", response.aid)
        }.executeWithListener(listener)
        Log.d(TAG, "photoFinish start")
    }

    private fun multipartRequest(url: String, bitmap: Bitmap, consumer: (String) -> Unit) {
        TaskKotlin({
            ByteArrayOutputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                val encodedImage = Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
                val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file1", encodedImage)
                        .build()

                val request = Request.Builder()
                        .url(url)
                        .method("POST", RequestBody.create(null, ByteArray(0)))
                        .post(requestBody)
                        .build()
                Config.client.newCall(request).execute().body().string()
            }
        }, consumer).execute()
        Log.d(TAG, "multipartRequest start")
    }

    private data class UploadResponse(
            val server: String = "",
            val photos_list: String = "",
            val hash: String = "",
            val aid: String = "")
}

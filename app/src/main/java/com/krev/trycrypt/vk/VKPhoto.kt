package com.krev.trycrypt.vk

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.krev.trycrypt.model.Config
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.photo.VKImageParameters
import com.vk.sdk.api.photo.VKUploadImage
import java8.util.concurrent.CompletableFuture

object VKPhoto {
    fun uploadPhoto(bitmap: Bitmap) = CompletableFuture<String>().apply {
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                Log.d("VKEvent", "loading photo task finished")
                response!!.json.getJSONArray("response").getJSONObject(0).apply {
                    var photo = optString("photo_807")
                    if (photo == "") photo = optString("photo_604")
                    Log.d("VKEvent", "loading photo task func")
                    complete(photo)
                }
            }

            override fun onError(error: VKError?) {
                Toast.makeText(Config.context, "Place failed ($error)", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("VKEvent", "loading photo ${Config.album}")
        VKApi.uploadAlbumPhotoRequest(VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), Config.album, 0).executeWithListener(listener)
        Log.d("VKPhoto", "loading photo task started")
    }
}

package com.krev.trycrypt.vk

import android.graphics.Bitmap
import android.util.Log
import com.krev.trycrypt.model.Config
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.photo.VKImageParameters
import com.vk.sdk.api.photo.VKUploadImage

object VKPhoto {
    fun uploadPhoto(function: (String) -> Unit = {}, bitmap: Bitmap) {
        val listener = object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                Log.d("VKPhoto", "loading photo task finished")
                response!!.json.getJSONArray("response").getJSONObject(0).apply {
                    var photo = optString("photo_807")
                    if (photo == "") photo = optString("photo_604")
                    Log.d("VKPhoto", "loading photo task func")
                    function(photo)
                }
            }

            override fun onError(error: VKError?) {
                Log.d("VKPhoto", "error $error")
            }
        }
        Log.d("VKPhoto", "loading photo ${Config.album}")
        VKApi.uploadAlbumPhotoRequest(VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), Config.album, 0).executeWithListener(listener)
        Log.d("VKPhoto", "loading photo task started")
    }
}

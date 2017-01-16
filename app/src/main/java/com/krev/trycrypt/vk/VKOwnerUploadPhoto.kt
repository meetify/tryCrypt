package com.krev.trycrypt.vk

import android.util.Log
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.photo.VKUploadPhotoBase
import org.json.JSONObject
import java.io.File

class VKOwnerUploadPhoto(val id: Long, photo: File) : VKUploadPhotoBase() {

    init {
        mImages = arrayOf(photo)
    }

    override fun getSaveRequest(response: JSONObject?): VKRequest {
        Log.d("VKEvent", "getSaveRequest >$response<")
        return VKRequest("photos.saveOwnerPhoto", VKParameters(mapOf(
                Pair("server", response!!.getString("server")),
                Pair("hash", response.getString("hash")),
                Pair("photo", response.getString("photo")))))
    }

    override fun getServerRequest(): VKRequest {
        Log.d("VKEvent", "getServerRequest")
        return VKRequest("photos.getOwnerPhotoUploadServer", VKParameters(mapOf(
                Pair("owner_id", id))))
    }
}
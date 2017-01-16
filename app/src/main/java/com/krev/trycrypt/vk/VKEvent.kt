package com.krev.trycrypt.vk

import android.graphics.Bitmap
import android.util.Log
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.Reply
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.util.DateUtils
import com.vk.sdk.api.*
import java8.util.concurrent.CompletableFuture
import java.io.File

/**
 * Created by Dima on 15.01.2017.
 */
object VKEvent {

    data class Event(val comments: List<Reply>) {
        companion object Builder {
            fun from(place: Place) = CompletableFuture<Event>().apply {
                Log.d("VKEvent.Event", "Builder.from")
                VKApi.wall().getComments(VKParameters(mapOf(
                        Pair("owner_id", -place.vkEvent),
                        Pair("post_id", 1),
                        Pair("need_likes", 1),
                        Pair("extended", 1)
                ))).executeWithListener(object : VKRequest.VKRequestListener() {
                    override fun onComplete(response: VKResponse?) {
                        val json = response!!.json
                                .getJSONObject("response")
                                .getJSONArray("items")
                        Log.d("VKEvent.Event", "onComplete ${json.toString(2)}")
                        val comments = ArrayList<Reply>()
                        for (i in 0..json.length()-1) {
                            val obj = json.getJSONObject(i)
                            val user = Config.findUser(obj.getLong("from_id"))

                            comments.add(Reply(
                                    user.name,
                                    obj.getString("text"),
                                    user.photo,
                                    obj.getJSONObject("likes").getInt("count").toString(),
                                    Reply.Type.MEETIFY
                            ))
                        }
                        val event = Event(comments)
                        Log.d("VKEvent.Event", "done $event")
                        complete(event)
                    }

                    override fun onError(error: VKError?) {
                        Log.d("VKEvent.Event", "error $error")
                    }
                })
                Log.d("VKEvent", "Builder.from end")
            }
        }
    }

    fun create(place: Place, bitmap: Bitmap) = CompletableFuture<Long>().apply {
        Log.d("VKEvent", "groups.create")
        VKRequest("groups.create", VKParameters(mapOf(
                Pair("title", place.name),
                Pair("description", place.description),
                Pair("type", "event")
        ))).executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                Log.d("VKEvent", "groups.create.onComplete >${response!!.json}<")
                val id = response.json.getJSONObject("response").getLong("id")
                complete(id)
                VKRequest("groups.edit", VKParameters(mapOf(
                        Pair("group_id", id),
                        Pair("event_start_date", place.date / 1000)
                ))).executeWithListener(listener)

                val it = bitmap
                Log.d("VKEvent", "groups.create.onComplete.getConsumer")
                val file = File(Config.context.cacheDir, "$id.jpg")
                Log.d("VKEvent", "PhotoUtils.getConsumer, >${Config.context.cacheDir}< >$file<")
                val fos = file.outputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 85, fos)


                VKRequest("wall.post", VKParameters(mapOf(
                        Pair("owner_id", -id),
                        Pair("from_group", 1),
                        Pair("message", "New event on ${DateUtils.format(place.date)} called ${place.name}.\n${place.description}")
                ))).executeWithListener(listener)

                VKOwnerUploadPhoto(-id, file).executeWithListener(listener)
            }

            override fun onError(error: VKError?) {
                Log.d("VKEvent", error?.toString())
            }

            override fun attemptFailed(request: VKRequest?, attemptNumber: Int, totalAttempts: Int) {
                Log.d("VKEvent", "attemptFailed")
            }
        })
    }

    val listener = object : VKRequest.VKRequestListener() {
        override fun attemptFailed(request: VKRequest?, attemptNumber: Int, totalAttempts: Int) {
            Log.d("VKEvent", "listener: attemptFailed")
        }

        override fun onComplete(response: VKResponse?) {
            Log.d("VKEvent", "listener: ${response?.json}")
        }

        override fun onError(error: VKError?) {
            Log.d("VKEvent", "listener: $error")
        }
    }
}
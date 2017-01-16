package com.krev.trycrypt.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.krev.trycrypt.R
import com.krev.trycrypt.adapter.FriendsCheckAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.entity.MeetifyLocation
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.server.BaseController
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.util.BitmapUtils
import com.krev.trycrypt.util.JsonUtils.read
import com.krev.trycrypt.util.JsonUtils.write
import com.krev.trycrypt.vk.VKEvent
import com.krev.trycrypt.vk.VKPhoto
import java8.util.concurrent.CompletableFuture
import java.util.*


class PlaceAddActivity : Activity() {

    private val TAG = PlaceAddActivity::class.java.toString()

    val imageView: ImageView by lazy { findViewById(R.id.place_add_icon) as ImageView }
    val friends: ListView by lazy { findViewById(R.id.place_add_friends) as ListView }
    val name: EditText by lazy { findViewById(R.id.place_add_name) as EditText }
    val button: Button by lazy { findViewById(R.id.place_add_button) as Button }
    val description: EditText by lazy { findViewById(R.id.place_add_description) as EditText }
    val location: MeetifyLocation by lazy { read(intent.getStringExtra("location"), MeetifyLocation::class.java) }
    val RESULT_LOAD_IMAGE = 0
    var bitmap: Bitmap = Config.bitmap

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    val dateCompletable = CompletableFuture<Long>()


    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val storage = 256

    override fun onCreate(savedInstanceState: Bundle?) {
//        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_add)
        date = 0L
        friends.adapter = FriendsCheckAdapter().add(Config.friends)

        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_place_time, null, false)
        val datePicker = view.findViewById(R.id.dialog_place_datePicker) as DatePicker
        val timePicker = view.findViewById(R.id.dialog_place_timePicker) as TimePicker
        val complete = view.findViewById(R.id.dialog_place_complete) as Button
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            Log.d("PlaceAddActivity", "$year.$month.$day $hour:$minute")
            this@PlaceAddActivity.hour = hour
            this@PlaceAddActivity.minute = minute
            date = Calendar.getInstance().apply { set(year, month, day, hour, minute) }.time.time
        }
        datePicker.init(2017, 1, 16, { _, year, month, day ->
            this@PlaceAddActivity.year = year
            this@PlaceAddActivity.month = month
            this@PlaceAddActivity.day = day
            Log.d("PlaceAddActivity", "$year.$month.$day")
            datePicker.visibility = View.INVISIBLE
            timePicker.visibility = View.VISIBLE
        })

        complete.setOnClickListener {
            date = Calendar.getInstance().apply {
                set(datePicker.year, datePicker.month, datePicker.dayOfMonth, timePicker.hour, timePicker.minute)
            }.time.time
            var place = Place()
            VKPhoto.uploadPhoto(bitmap)
                    .thenApplyAsync {
                        Log.d("VKEvent", "VKPhoto.uploadPhoto.then $it")
                        place = place(it)
                        VKEvent.create(place, bitmap)
                    }
                    .thenApplyAsync {
                        Log.d("VKEvent", "VKEvent.create.then $it")
                        val id = it.get()
                        Log.d("VKEvent", "VKEvent.create.then $id")
                        place.vkEvent = id
                        PlaceController.request(BaseController.Method.PUT, place)
                    }
                    .thenApplyAsync {
                        place = read(it.get(), Place::class.java)
                        Log.d("VKEvent", "PlaceController.request then ${write(place)}")
                        Config.addPlace(place)
                        Config.user.created += place.id
                    }
            finish()
        }

        button.setOnClickListener {
            builder.setView(view)
            builder.create().show()
        }


        @SuppressLint("NewApi")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkSelfPermission $permission failed")
            requestPermissions(arrayOf(permission), storage)
        }
        imageView.setOnClickListener {
            photoFromGallery()
        }
    }

    fun place(photo: String): Place {
        return Place(name.text.toString(), description.text.toString(),
                Config.user.id, photo, location, (friends.adapter as FriendsCheckAdapter).checked, date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //todo: files permission
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            contentResolver.query(data.data, filePathColumn, null, null, null).use {
                it.moveToFirst()
                val picturePath = it.getString(it.getColumnIndex(filePathColumn[0]))
                bitmap = BitmapFactory.decodeFile(picturePath)
                imageView.setImageDrawable(BitmapUtils.getRoundedDrawable(bitmap))
            }
        }
    }

    private fun photoFromGallery() =
            startActivityForResult(Intent(ACTION_PICK, EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE)

    var date = 0L
}

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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapter.FriendsCheckAdapter
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.server.BaseController.Method
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.model.entity.MeetifyLocation
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.util.BitmapUtils
import com.krev.trycrypt.util.JsonUtils.read
import com.krev.trycrypt.vk.VKPhoto


class PlaceAddActivity : AppCompatActivity() {

    private val TAG = PlaceAddActivity::class.java.toString()

    val imageView: ImageView by lazy { findViewById(R.id.place_add_icon) as ImageView }
    val friends: ListView by lazy { findViewById(R.id.place_add_friends) as ListView }
    val name: EditText by lazy { findViewById(R.id.place_add_name) as EditText }
    val button: Button by lazy { findViewById(R.id.place_add_button) as Button }
    val description: EditText by lazy { findViewById(R.id.place_add_description) as EditText }
    val location: MeetifyLocation by lazy { read(intent.getStringExtra("location"), MeetifyLocation::class.java) }
    val RESULT_LOAD_IMAGE = 0
    var bitmap: Bitmap = Config.bitmap

    private val permission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val storage = 256

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_add)

        friends.adapter = FriendsCheckAdapter().add(Config.friends)
        button.setOnClickListener {
            VKPhoto.uploadPhoto({
                PlaceController.request(Method.PUT, place(it)).thenApplyAsync {
                    val place = read(it, Place::class.java)
                    Config.addPlace(place)
                    Config.user.created += place.id
//                    PhotoUtils.put("place", place.id, place.photo)
                }
            }, bitmap)
            finish()
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

    fun place(photo: String) = Place(name.text.toString(), description.text.toString(),
            Config.user.id, photo, location, (friends.adapter as FriendsCheckAdapter).checked)

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
}

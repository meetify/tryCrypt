package com.krev.trycrypt.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import com.krev.trycrypt.R
import com.krev.trycrypt.adapters.FriendsCheckAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.server.model.entity.MeetifyLocation
import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.utils.BitmapUtils
import com.krev.trycrypt.utils.JsonUtils.json
import com.krev.trycrypt.utils.async.ImageTask
import com.krev.trycrypt.vk.VKPhoto


class PlaceAddActivity : AppCompatActivity() {

    val imageView: ImageView by lazy { findViewById(R.id.place_add_icon) as ImageView }
    val friends: ListView by lazy { findViewById(R.id.place_add_friends) as ListView }
    val name: EditText by lazy { findViewById(R.id.place_add_name) as EditText }
    val button: Button by lazy { findViewById(R.id.place_add_button) as Button }
    val description: EditText by lazy { findViewById(R.id.place_add_description) as EditText }
    val location: MeetifyLocation by lazy { json(intent.getStringExtra("location"), MeetifyLocation::class.java) }
    val RESULT_LOAD_IMAGE = 0
    var bitmap: Bitmap = Config.bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        Config.activity = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_add)
        friends.adapter = FriendsCheckAdapter().add(Config.friends)
        button.setOnClickListener {
            Log.d("PlaceAddActivity", "start adding place")
            VKPhoto.uploadPhoto({
                Log.d("PlaceAddActivity", "photo done")
                PlaceController.put(Place(name.text.toString(), description.text.toString(), Config.user.id,
                        it, location, (friends.adapter as FriendsCheckAdapter).checked), {
                    val place = json(it.body().string(), Place::class.java)
                    Config.add(place)
                    Config.user.created += place.id
                    ImageTask({
                        Log.d("PlaceAddActivity", "got the photo")
                    }, "place_${place.id}").execute(place.photo)
                })
            }, bitmap)
            finish()
        }
        imageView.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
}

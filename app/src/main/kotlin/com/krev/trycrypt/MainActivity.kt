package com.krev.trycrypt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsername = findViewById(R.id.editTextUsername) as EditText
        val editTextPassword = findViewById(R.id.editTextPassword) as EditText
        val buttonLogin = findViewById(R.id.buttonLogin) as Button
        val textView = findViewById(R.id.textView) as TextView
        editTextUsername.setText("user")
        editTextPassword.setText("pass")
        textView.setOnClickListener {

        }

        buttonLogin.setOnClickListener {
//            Thread(ServerConnect(editTextUsername, editTextPassword)).start()
            if (!VKSdk.isLoggedIn())
                VKSdk.login(this, "friends")
            val request = VKApi.friends().getOnline(
                    VKParameters.from(VKApiConst.OWNER_ID))

            request.executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse) {
                    Log.d(Log.DEBUG.toString(), "${response.responseString}")
                }

                override fun onError(error: VKError) {
                    Log.d(Log.ERROR.toString(), "${error.errorMessage}")
                }

                override fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {
                    Log.d(Log.ERROR.toString(), "${request.toString()}")
                }
            })
        }
    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
//            override fun onResult(res: VKAccessToken) {
//            }
//
//            override fun onError(error: VKError) {
//            }
//        })) {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
}

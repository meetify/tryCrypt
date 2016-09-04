package com.krev.trycrypt.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.krev.trycrypt.R
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*

class MainActivity : AppCompatActivity() {

    val TAG = javaClass.canonicalName

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
            Log.d(TAG, "Logging in")
            if (!VKSdk.isLoggedIn())
                VKSdk.login(this, "friends")
            val request = VKApi.friends().getOnline(
                    VKParameters.from(VKApiConst.OWNER_ID))
            Log.d(TAG, "Executing request")
            request.executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse) {
                    Log.d(TAG, "${response.responseString}")
                }

                override fun onError(error: VKError) {
                    if (error.errorMessage.equals("Unable to resolve host \"api.vk.com\": No address associated with hostname")) {
                        Toast.makeText(applicationContext, "Check your network connection", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {
                    Log.e(TAG, "${request.toString()}")
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

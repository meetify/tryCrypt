package com.krev.trycrypt.activity

import android.content.Intent
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
import serverModule.Response

class LoginActivity : AppCompatActivity() {

    val TAG = javaClass.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUsername = findViewById(R.id.editTextUsername) as EditText
        val editTextPassword = findViewById(R.id.editTextPassword) as EditText
        val buttonLogin = findViewById(R.id.buttonLogin) as Button
        val textView = findViewById(R.id.textView) as TextView
        editTextUsername.setText("user")
        editTextPassword.setText("pass")
        textView.setOnClickListener {
            val i = Intent(applicationContext, RegisterActivity::class.java)
            i.putExtra("txt", "hw")
            startActivity(i)
        }

        buttonLogin.setOnClickListener {

        }
    }
}
package com.krev.trycrypt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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
            Thread(ServerConnect(editTextUsername, editTextPassword)).start()
        }
    }
}

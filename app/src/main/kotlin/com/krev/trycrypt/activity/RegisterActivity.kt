package com.krev.trycrypt.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.krev.trycrypt.R
import com.krev.trycrypt.server.ServerConnect

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        (findViewById(R.id.buttonRegister) as Button).setOnClickListener {
            val mail = ((findViewById(R.id.editTextMailRegister)) as EditText).text.toString()
            val pass = ((findViewById(R.id.editTextPasswordRegister)) as EditText).text.toString()
            var toastMessage = ""
            if (!mail.matches(Regex("/.+@.+\\..+/i"))){
                toastMessage += "Your email isn't valid\n"
            }
            if (pass.length < 8) {
                toastMessage += "Your password is too short\n"
            }
            if (toastMessage != "") {
                Toast.makeText(this, toastMessage.removeSurrounding("","\n"), Toast.LENGTH_LONG).show()
            } else {
//                if (ServerConnect.register(mail, pass))
            }
        }
    }
}

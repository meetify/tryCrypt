package com.krev.trycrypt

import android.util.Log
import android.widget.EditText
import java.io.DataInputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.net.Socket
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Created by kr3v on 03.09.2016.
 */
class ServerConnect(var editTextUsername: EditText,
                    var editTextPassword: EditText) : Runnable {

    override fun run() {
//        register(editTextUsername.text.toString(),
//                editTextPassword.text.toString())
//        Thread.sleep(5000)
        login(editTextUsername.text.toString(),
                editTextPassword.text.toString())
    }

    fun login(username: String, password: String) {
        val socket = Socket("192.168.0.101", 8080)
        socket.use {
            val inputStream = DataInputStream(socket.inputStream)
            val outputStream = DataOutputStream(socket.outputStream)

            outputStream.writeUTF("login")
            outputStream.writeUTF(username)
            val salt = inputStream.readUTF()
            outputStream.writeUTF(saltedHash(password, salt))
            Log.d(Log.DEBUG.toString(), inputStream.readUTF())
        }
    }

    fun register(username: String, password: String) {
        val socket = Socket("192.168.0.101", 8080)
        socket.use {
            val inputStream = DataInputStream(socket.inputStream)
            val outputStream = DataOutputStream(socket.outputStream)

            outputStream.writeUTF("register")
            val salt = inputStream.readUTF()
            val toSend = "${editTextUsername.text.toString()}#" +
                    "${saltedHash(editTextPassword.text.toString(), salt)}"
            Log.d(Log.DEBUG.toString(), toSend)
            outputStream.writeUTF(toSend)
            Log.d(Log.DEBUG.toString(), inputStream.readUTF())
        }
    }

    fun bin2hex(data: ByteArray): String {
        return String.format("%0" + data.size * 2 + "X", BigInteger(1, data))
    }

    fun hash(string: String): String {
        val sha512 = MessageDigest.getInstance("SHA-512")
        sha512.reset()
        return bin2hex(sha512.digest(string.toByteArray(Charset.forName("US-ASCII"))))
    }

    fun saltedHash(string: String, salt: String): String {
        return hash(salt + hash(salt))
    }
}
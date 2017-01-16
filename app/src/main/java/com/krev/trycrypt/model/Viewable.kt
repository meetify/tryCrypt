package com.krev.trycrypt.model

import android.support.v7.app.AlertDialog
import com.krev.trycrypt.activity.MapActivity

interface Viewable {
    fun MapActivity.showDialog(builder: AlertDialog.Builder)
}
package com.krev.trycrypt.activity

import android.os.Bundle
import android.preference.PreferenceActivity
import com.krev.trycrypt.R


class SettingsActivity : PreferenceActivity() {

    companion object {
        val KEY_LOCATION_TO_SERVER = "switch_preference_1"
        val KEY_NOTIFICATIONS = "switch_preference_2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Config.activity = this
        super.onCreate(savedInstanceState)
        //DrawerUtils.getDrawer(this)

        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}

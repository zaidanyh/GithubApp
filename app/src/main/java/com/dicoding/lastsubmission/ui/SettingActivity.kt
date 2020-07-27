package com.dicoding.lastsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.lastsubmission.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = resources.getString(R.string.settings)

        supportFragmentManager.beginTransaction().add(R.id.setting_activity, SettingsPreferenceFragment()).commit()
    }
}
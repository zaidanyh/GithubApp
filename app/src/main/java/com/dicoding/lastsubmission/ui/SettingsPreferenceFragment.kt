package com.dicoding.lastsubmission.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.helper.ReminderReceiver
import java.util.*

class SettingsPreferenceFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var switchPreference: SwitchPreference
    private lateinit var langPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
        init()
        setContent()

        langPreference.intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        switchPreference.setOnPreferenceClickListener {
            if (switchPreference.isChecked) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                val intent = Intent(preferenceManager.context, ReminderReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(preferenceManager.context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = preferenceManager.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

                Toast.makeText(preferenceManager.context, "Repeating Reminder set up", Toast.LENGTH_SHORT).show()
            } else {
                val alarmManager = preferenceManager.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(preferenceManager.context, ReminderReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(preferenceManager.context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                pendingIntent.cancel()
                alarmManager.cancel(pendingIntent)
                Toast.makeText(preferenceManager.context, "Repeating Reminder canceled", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    private fun init() {
        switchPreference = findPreference<SwitchPreference>(resources.getString(R.string.key_switch)) as SwitchPreference
        langPreference = findPreference<Preference>(resources.getString(R.string.key_language)) as Preference
    }

    private fun setContent() {
        val preference = preferenceManager.sharedPreferences
        switchPreference.isChecked = preference.getBoolean(resources.getString(R.string.key_switch), false)
        langPreference.summary = preference.getString(resources.getString(R.string.key_language), resources.getString(R.string.lang))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == resources.getString(R.string.key_switch)) {
            switchPreference.isChecked = sharedPreferences.getBoolean(resources.getString(R.string.key_switch), false)
        }
        if (key == resources.getString(R.string.key_language)) {
            langPreference.summary = sharedPreferences.getString(resources.getString(R.string.key_language), resources.getString(R.string.lang))
        }
    }
}
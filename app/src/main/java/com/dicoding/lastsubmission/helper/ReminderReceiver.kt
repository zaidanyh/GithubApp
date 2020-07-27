package com.dicoding.lastsubmission.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dicoding.lastsubmission.R
import com.dicoding.lastsubmission.ui.MainActivity

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val reminderIntent = Intent(context, MainActivity::class.java)
        reminderIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, 100, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, "channel")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_githubapplogo)
            .setContentTitle("Github App")
            .setContentText("Let's find user popular user on Github")
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel"
                , "channel_reminder",
                NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId("channel")
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(101, notification)
    }
}

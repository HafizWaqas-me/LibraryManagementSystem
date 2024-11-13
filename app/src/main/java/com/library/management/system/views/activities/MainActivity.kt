package com.library.management.system.views.activities

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.library.management.system.R
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.EXPIRING_CHANNEL
import com.library.management.system.utils.GENERAL_CHANNEL
import com.library.management.system.utils.NEW_BOOK_ADD_CHANNEL
import com.library.management.system.utils.OVERDUE_CHANNEL
import com.onesignal.OneSignal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (AUTH.currentUser != null) {
            OneSignal.User.addEmail(AUTH.currentUser!!.email.toString())

        }

        registerNotificaitonChannels()
    }

    fun registerNotificaitonChannels() {

        val notificationChannels = listOf(
            NotificationChannel(
                NEW_BOOK_ADD_CHANNEL,
                "New Books",
                NotificationManager.IMPORTANCE_HIGH,
            ),
            NotificationChannel(
                GENERAL_CHANNEL,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            NotificationChannel(
                EXPIRING_CHANNEL,
                "Expired Books",
                NotificationManager.IMPORTANCE_HIGH
            ),
            NotificationChannel(
                OVERDUE_CHANNEL,
                "OVERDUE Books",
                NotificationManager.IMPORTANCE_HIGH
            ),
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(NotificationChannelGroup("OFFLINE","OFFLINE"))
        notificationManager.createNotificationChannels(notificationChannels)
    }
}
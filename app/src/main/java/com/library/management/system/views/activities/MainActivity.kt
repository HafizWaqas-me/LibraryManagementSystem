package com.library.management.system.views.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import com.library.management.system.R
import com.library.management.system.utils.EXPIRING_CHANNEL
import com.library.management.system.utils.ISSUED_BOOK_CHANNEL
import com.library.management.system.utils.NEW_BOOK_ADD_CHANNEL
import com.library.management.system.utils.OVERDUE_CHANNEL
import com.library.management.system.utils.RETURN_CHANNEL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        registerNotificationChannels()

    }


    fun registerNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannels = listOf(

                NotificationChannel(
                    ISSUED_BOOK_CHANNEL,
                    "Issued Book Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifies User when New Book Issued to them"
                },
                NotificationChannel(
                    EXPIRING_CHANNEL,
                    "Expired Book Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Warns users 24 hours before a book is due for return"
                },
                NotificationChannel(
                    OVERDUE_CHANNEL,
                    "Overdue Fine Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Reminds users of overdue fines daily until the book is returned"
                },
                NotificationChannel(
                    RETURN_CHANNEL,
                    "Return Book Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description =
                        "Notifies User when New Book Issued to themNotifies User when New Book Issued to them"
                },

                NotificationChannel(
                    NEW_BOOK_ADD_CHANNEL,
                    "New Book Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description =
                        "Notifies users about new additions to the library"
                },

                )
            val notificationManager = NotificationManagerCompat.from(this)
            notificationChannels.forEach {
                notificationManager.createNotificationChannel(it)
            }

        }
    }

}
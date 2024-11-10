package com.library.management.system.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Patterns
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.library.management.system.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object AppUtils {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(pass: String): Boolean {
        return pass.length >= 8
    }

    fun showNotificationToSpecificUser(
        context: Context,
        userId: Int,
        channelId: String,
        title: String,
        contentText: String
    ) {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_book)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager = NotificationManagerCompat.from(context)


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        notificationManager.notify(userId, notificationBuilder.build())
    }

    fun showNotificationEveryOne(
        context: Context,
        title: String,
        bookId: String,
        imgUrl: String
    ) {
        val clientNewBook = OkHttpClient()

        val jsonObjectNewBook = JSONObject().apply {
            put("app_id", context.getString(R.string.ONESIGNAL_ID))
            put("headings", JSONObject().put("en", "New Book Added"))
            put(
                "contents",
                JSONObject().put("en", "New Book \"$title\" Book ID:$bookId added in library")
            )
            put("included_segments", JSONArray().put("All"))  // Targets all users
            put("priority", 10)
            put("small_icon", "ic_book")  // Ensure "ic_book" is a valid icon
            put("large_icon", imgUrl)
            put("big_picture", imgUrl)
        }

        val body = jsonObjectNewBook.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://onesignal.com/api/v1/notifications")
            .post(body)
            .addHeader(
                "Authorization",
                "Basic ${context.getString(R.string.ONESIGNAL_REST_API_KEY)}"
            )
            .addHeader("accept", "application/json")
            .addHeader("content-type", "application/json")
            .build()

        clientNewBook.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AppUtilss", "Notification request failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("AppUtilss", "Notification sent successfully: $jsonObjectNewBook")
                } else {
                    Log.e(
                        "AppUtilss",
                        "Notification failed with response: ${response.code}, ${response.body?.string()}"
                    )
                }
            }
        })
    }


}


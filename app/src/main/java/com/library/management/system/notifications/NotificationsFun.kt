package com.library.management.system.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.library.management.system.R
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.ISSUED_BOOK_ADD_CHANNEL_ONESIGNAL
import com.library.management.system.utils.ISSUED_BOOK_CHANNEL
import com.library.management.system.utils.NEW_BOOK_ADD_CHANNEL_ONESIGNAL
import com.onesignal.OneSignal
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

object NotificationsFun {

    fun sendNotificationToExternalUser(
        context: Context,
        headings: String,
        title: String,
        imgUrl: String,
        userId: Int,
        bookId: Int
    ) {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("app_id", context.getString(R.string.ONESIGNAL_ID))
            put("headings", JSONObject().put("en", headings))
            put("contents", JSONObject().put("en", title))
            put("priority", 10)
            put("small_icon", "ic_book")
            put("large_icon", imgUrl)
            put("big_picture", imgUrl)
            put("android_channel_id", ISSUED_BOOK_ADD_CHANNEL_ONESIGNAL)
            put("sound", "default")
            put("target_channel", "push")
            put(
                "include_aliases",
                JSONObject().put("external_id", JSONArray().put(userId.toString()))
            )
        }

        val body = jsonObject.toString()
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

        adminConfirmNotification(
            context = context,
            userId = userId,
            title = "Book Issued to user ID:$userId",
            contentText = "Book \"$bookId\" issued to User ID:$userId. Book ID: $bookId "
        )

        Log.d("RESPONSSEE", "jsonObject: $jsonObject")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RESPONSSEE", "Failed to send notification: ${e.message}")
                e.printStackTrace()


            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("RESPONSSEE", "Response code: ${response.code}")
                Log.d(
                    "RESPONSSEE",
                    "Response body: ${response.body?.string() ?: "Response Body Else"}"
                )
            }
        })
        AUTH.currentUser?.email?.let { OneSignal.User.addEmail(it) }

    }

    fun showNotificationEveryOne(
        context: Context,
        headings: String,
        title: String,
        imgUrl: String
    ) {
        val clientNewBook = OkHttpClient()

        val jsonObjectNewBook = JSONObject().apply {
            put("app_id", context.getString(R.string.ONESIGNAL_ID))
            put("headings", JSONObject().put("en", headings))
            put("contents", JSONObject().put("en", title))
            put("included_segments", JSONArray().put("All"))
            put("priority", 10)
            put("small_icon", "ic_book")
            put("large_icon", imgUrl)
            put("big_picture", imgUrl)
            put("android_channel_id", NEW_BOOK_ADD_CHANNEL_ONESIGNAL)
            put("sound", "default")
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
                val responseBody = response.body?.string()
                if (response.isSuccessful) {
                    Log.d("AppUtilss", "Notification sent successfully: $jsonObjectNewBook")
                } else {
                    Log.e(
                        "AppUtilss",
                        "Notification failed with response: ${response.code}, $responseBody"
                    )
                }
            }
        })
    }

    fun adminConfirmNotification(
        context: Context,
        userId: Int,
        title: String,
        contentText: String
    ) {
        val notificationBuilder = NotificationCompat.Builder(context, ISSUED_BOOK_CHANNEL)
            .setSmallIcon(R.drawable.ic_book)  // Ensure this drawable exists
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

}
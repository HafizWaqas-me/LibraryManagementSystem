package com.library.management.system.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.library.management.system.R
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.AppUtils
import com.library.management.system.utils.ISSUED_BOOK_ADD_CHANNEL_ONESIGNAL
import com.library.management.system.utils.NEW_BOOK_ADD_CHANNEL
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

class IssueBookVM : ViewModel() {
    var userId: Int = 0
    var bookId: Int = 0
    var expiryDate: String = ""
    var imgUrl: String = ""
    var title: String = ""
    var author: String = ""
    var selectedDate: String = ""
    var fcmToken: String = ""

    var userIdError = false
    var bookIdError = false
    var expiryDateError = false

    fun isErrorNotVisible(): Boolean {
        return userId.toString().isNotEmpty()
                && bookId.toString().isNotEmpty()
                && expiryDate.isNotEmpty()
    }

    fun checkValues() {
        if (userIdError) {
            userIdError = true
        }
        if (bookIdError) {
            bookIdError = true
        }
        if (expiryDateError) {
            expiryDateError = true
        }

    }

    //Sending Notification with the help of One Signal to specific user
    fun sendNotificationToExternalUser(context: Context) {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("app_id", "${context.getString(R.string.ONESIGNAL_ID)}")
            put("headings", JSONObject().put("en", "New Book Issued"))
            put("contents", JSONObject().put("en", "Book \"$title\" Issued to you"))
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

        AppUtils.showNotificationToSpecificUser(
            context = context,
            userId = userId,
            channelId = NEW_BOOK_ADD_CHANNEL,
            title = "Book Issued to user ID:$userId",
            contentText = " Book  \"$title\" issued to User ID:$userId. Book ID: $bookId "
        )

        Log.d("RESPONSSEE", "jsonObject: $jsonObject")

        // Send the request
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


}
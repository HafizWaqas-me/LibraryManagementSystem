package com.library.management.system.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefUtil(application: Application) {
    init {
        Companion.application = application
    }

    companion object {
        lateinit var application: Application


        fun setUserId(key: String, value: Int) {
          //  sharePref.edit().remove(key).apply()
            sharePref.edit().putInt(key, value).apply()
        }

        fun getUserId(key: String): Int {
            return try {
                sharePref.getInt(key, -1)
            } catch (e: ClassCastException) {
                // Log the error and clear the incorrect entry
                Log.e("SharedPrefUtil", "Expected Int for key '$key' but found String. Clearing value.")
                sharePref.edit().remove(key).apply()
                -1 // Return default value
            }
        }

        fun setIntValue(key: String, value: Int) {
            sharePref.edit().putInt(key, value).apply()
        }

        fun getIntValue(key: String): Int {
            return try {
                sharePref.getInt(key, -1)
            } catch (e: ClassCastException) {
                // Log and remove the incorrect entry, then return default value
                Log.e(
                    "SharedPrefUtil",
                    "Expected Int for key '$key' but found String. Clearing value."
                )
                sharePref.edit().remove(key).apply()
                -1
            }
        }


        fun setLoginValue(key: String, value: String?) {
            sharePref.edit().putString(key, value).apply()

        }

        fun getLoginValue(key: String?): String? {
            return sharePref.getString(key, "No Value Found")
        }

        fun setLoginBoolean(key: String, value: Boolean) {
            sharePref.edit().putBoolean(key, value).apply()
        }

        fun getLoginBoolean(key: String): Boolean {
            return sharePref.getBoolean(key, false)
        }

        private val sharePref: SharedPreferences
            get() {
                return application.getSharedPreferences("lms_table", Context.MODE_PRIVATE)
            }
    }
}
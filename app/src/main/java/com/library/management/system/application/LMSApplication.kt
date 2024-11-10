package com.library.management.system.application

import android.app.Application
import com.library.management.system.R
import com.library.management.system.utils.SharedPrefUtil
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LMSApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPrefUtil(this)

        oneSignalInitialization()
    }


    fun oneSignalInitialization() {
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, this.getString(R.string.ONESIGNAL_ID))

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
        }
    }
}

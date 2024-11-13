package com.library.management.system.utils

import android.util.Patterns

object AppUtils {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(pass: String): Boolean {
        return pass.length >= 8
    }

}


package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel
import com.library.management.system.model.SignUpUsersData
import com.library.management.system.utils.AppUtils

class SignUpVM : ViewModel() {

    var fullName: String = ""
    var userId: Int = 0
    var email: String = ""
    var pass: String = ""
    var cPass: String = ""
    var fcmToken: String = ""

    var fullNameError = false
    var userIdError = false
    var emailError = false
    var passError = false
    var cPassError = false

    fun isErrorNotVisible(): Boolean {
        return fullName.isNotEmpty() && email.isNotEmpty()
                && AppUtils.isValidEmail(email) && userId.toString().isNotEmpty()
                 && pass.isNotEmpty()
                && AppUtils.isValidPassword(pass) && cPass.isNotEmpty() && pass == cPass
    }


    fun checkValues() {

        if (fullName.isEmpty()) {
            fullNameError = true
        }
        if (userId <= 0 || userId.toString().isEmpty()) {
            userIdError = true
        }
        if (email.isEmpty()) {
            emailError = true
        }
        if (pass.isEmpty()) {
            passError = true
        }

        if (cPass.isEmpty()) {
            cPassError = true
        }
    }

    fun uploadSignupData(): SignUpUsersData {
        return SignUpUsersData(
            fullName = fullName,
            userId = userId,
            email = email,
            pass = pass,
            cPass = cPass,
            fcmToken = fcmToken,
            imgUrl = ""
        )
    }
}
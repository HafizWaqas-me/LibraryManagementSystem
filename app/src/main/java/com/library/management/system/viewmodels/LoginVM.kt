package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel
import com.library.management.system.utils.AppUtils

class LoginVM : ViewModel() {

    var email: String = ""
    var pass: String = ""
    var userId:Int = 0

    var emailError = false
    var passError = false
    var userIdError = false

    fun isErrorNotVisible(): Boolean {
        return email.isNotEmpty() && AppUtils.isValidEmail(email) && pass.isNotEmpty()
                && AppUtils.isValidPassword(pass) && userId.toString().isNotEmpty()
    }


    fun checkValues() {
        if (email.isEmpty()) {
            emailError = true
        }
        if (pass.isEmpty()) {
            passError = true
        }
        if (userId.toString().isEmpty()){
            userIdError = true
        }
    }

}
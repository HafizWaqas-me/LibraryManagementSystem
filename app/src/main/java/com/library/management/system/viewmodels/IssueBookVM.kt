package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel

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

}
package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel

class FineVM : ViewModel() {

    var userId: Int = 0
    var bookId: Int = 0

    var userIdError = false
    var bookIdError = false

    fun isErrorNotVisible(): Boolean {
        return userId > 0 && bookId > 0 && userId.toString().isNotEmpty()
                && userId.toString().isNotEmpty()
    }

    fun checkValues() {
        if (userId.toString().isEmpty()) {
            userIdError = true
        }
        if (bookId.toString().isEmpty()) {
            bookIdError = true
        }
    }


}
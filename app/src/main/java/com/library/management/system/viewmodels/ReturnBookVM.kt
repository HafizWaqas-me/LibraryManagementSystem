package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel

class ReturnBookVM:ViewModel() {

    var userId:Int = 0
    var bookId :Int = 0
    var userIdError= false
    var bookIdError = false

    fun isErrorNotVisible():Boolean{
        return userId>0 && bookId > 0
    }

    fun checkValue(){
        if (userIdError){
            userIdError = true
        }
        if (bookIdError ){
            bookIdError = true
        }
    }
}
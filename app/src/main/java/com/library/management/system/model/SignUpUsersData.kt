package com.library.management.system.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpUsersData(
    val fullName: String = "",
    val email: String = "",
    val userId: Int = 0,
    val cardId: Int = 0,
    val pass: String = "",
    val cPass: String = "",
    val fcmToken:String = "",
    val imgUrl:String? = "",
    val mobile:String?=""

) : Parcelable

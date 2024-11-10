package com.library.management.system.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IssueBooks(
    val bookId: Int = 0,
    val userId: Int? = 0,
    val title: String? = null,
    val author: String? = null,
    val imgUrl: String? = null,
    val expiryDate:String = ""
) : Parcelable

package com.library.management.system.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Books(
    val bookId: Int = 0,
    val title: String? = null,
    val author: String? = null,
    val category: String? = null,
    val total: Int? = null,
    val imgUrl: String? = null,
    val desc: String = "",
    val status: Boolean = true,
) : Parcelable

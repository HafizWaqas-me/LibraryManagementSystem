package com.library.management.system.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

val AUTH = FirebaseAuth.getInstance()
val USER_SIGNUP_REF = FirebaseDatabase.getInstance().getReference("users_signUp")
val ALL_BOOK_REF = FirebaseDatabase.getInstance().getReference("all_books")
val ISSUED_BOOK_REF = FirebaseDatabase.getInstance().getReference("issued_books")
val ADMIN_EMAIL_LIST = FirebaseDatabase.getInstance().getReference("admin_email_list")
val FCM_TOKEN = FirebaseMessaging.getInstance().token


//Notification Channels ID's
const val ISSUED_BOOK_CHANNEL = "ISSUED_BOOK_CHANNEL"
const val NEW_BOOK_ADD_CHANNEL = "NEW_BOOK_ADD_CHANNE"
const val EXPIRING_CHANNEL = "EXPIRY_BOOK_CHANNEL"
const val GENERAL_CHANNEL = "EXPIRY_BOOK_CHANNEL"

const val RETURN_CHANNEL = "RETURN_BOOK_CHANNEL"
const val OVERDUE_CHANNEL = "0VERDUE_BOOK_CHANNEL"

const val STATUS = "status"

val BOOKS_CATEGORIES = listOf(
    "Select Category",
    "CSE",
    "Mathematics",
    "Novel",
    "ECE",
    "Medical",
    "Islamic",
    "History",
    "Politic"
)

//Shared Preferences

// Login
const val LOGIN_EMAIL = "loginEmail"
const val IS_ADMIN = "isAdmin"
const val LOGIN_EMAIL_BOOLEAN = "loginEmailBoolean"

const val USER_ID = ""

//Onesignal Notification Channel
const val NEW_BOOK_ADD_CHANNEL_ONESIGNAL = "22c46a10-71cc-4480-8e32-510076f92149"
const val ISSUED_BOOK_ADD_CHANNEL_ONESIGNAL = "f0dd7cb2-bac3-45e3-831a-e2c4ceff5431"

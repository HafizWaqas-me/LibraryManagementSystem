package com.library.management.system.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.library.management.system.model.Books
import com.library.management.system.model.IssueBooks
import com.library.management.system.utils.ALL_BOOK_REF
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.ISSUED_BOOK_REF
import com.library.management.system.utils.STATUS
import com.library.management.system.utils.SharedPrefUtil
import com.library.management.system.utils.USER_ID
import com.library.management.system.utils.USER_SIGNUP_REF
import com.onesignal.OneSignal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserDashBoardVM : ViewModel() {

    private var _issuedBooksListLiveData = MutableLiveData<List<IssueBooks>>()
    val issuedBooksListLiveData: LiveData<List<IssueBooks>> get() = _issuedBooksListLiveData

    private var _allBooksListLiveData = MutableLiveData<List<Books>>()
    val allBooksListLiveData: LiveData<List<Books>> get() = _allBooksListLiveData

    var _totalFine = MutableLiveData<Int>()
    val totalFine: LiveData<Int> get() = _totalFine
    var _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> get() = _userId
    var _expiredBooks = MutableLiveData<Int>()
    val expiredBooks: LiveData<Int> get() = _expiredBooks
    // var issuedBooksFetch = mutableListOf<IssueBooks>()

    fun getAllBooks() {
        ALL_BOOK_REF.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedAllBooks = mutableListOf<Books>()
                for (snap in snapshot.children) {
                    snap.getValue(Books::class.java)?.let { fetchedAllBooks.add(it) }
                }
                _allBooksListLiveData.value = fetchedAllBooks
                Log.d("UserDashBoardVM", "all books list size ${allBooksListLiveData.value?.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserDashBoardVM", "Error fetching all books: ${error.message}")
            }
        })
    }

    fun getUserId() {
        val email = AUTH.currentUser?.email
        if (email == null) {
            Log.e("UserDashBoardVM", "User email is null, cannot fetch issued books.")
            return
        }

        USER_SIGNUP_REF.orderByChild("email").equalTo(email).get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.exists() && userDocument.children.iterator().hasNext()) {
                    val userSnapshot = userDocument.children.iterator().next()
                    var userId = userSnapshot.child("userId").getValue(Long::class.java)
                    _userId.value = userId?.toInt()
                    // Save and login with user ID
                    userId?.let {
                        OneSignal.login(it.toString())
                        fetchUserIssuedBooks(userId.toString())
                        SharedPrefUtil.setUserId(USER_ID, userId.toInt())
                        Log.e("UserDashBoardVM", "Pref: ${SharedPrefUtil.getUserId(USER_ID)}")

                    }
                } else {
                    Log.e("UserDashBoardVM", "No user found with email: $email")
                }
            }.addOnFailureListener {
                Log.e("UserDashBoardVM", "Failed to fetch user document: ${it.message}")
            }
    }

    private fun fetchUserIssuedBooks(userId: String) {

        ISSUED_BOOK_REF.child(userId)
            .orderByChild(STATUS).equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var issuedBooksFetch = mutableListOf<IssueBooks>()
                        for (bookSnapshot in snapshot.children) {
                            bookSnapshot.getValue(IssueBooks::class.java)
                                ?.let { issuedBooksFetch.add(it) }
                        }
                        _issuedBooksListLiveData.value = issuedBooksFetch
                        Log.d(
                            "UserDashBoardVM",
                            "issed books size${issuedBooksListLiveData.value?.size}"
                        )
                        calculateTotals(issuedBooksFetch)
                    } else {
                        Log.d("UserDashBoardVM", "No issued books found for user ID: $userId")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserDashBoardVM", "Error fetching issued books: ${error.message}")
                }
            })
    }

    fun clearData() {
        _issuedBooksListLiveData = MutableLiveData()
        _allBooksListLiveData = MutableLiveData()
        _userId = MutableLiveData()
        _expiredBooks = MutableLiveData()
        _totalFine = MutableLiveData()
    }

    fun calculateTotals(bookList: List<IssueBooks>) {
        var totalFine = 0
        var expiredBookCount = 0
        val finePerDay = 50

        for (book in bookList) {
            val differenceInDays = calculateDifferenceInDays(book.expiryDate)
            if (differenceInDays > 0) {
                expiredBookCount++
                totalFine += differenceInDays * finePerDay
            }
        }

        _totalFine.value = totalFine
        _expiredBooks.value = expiredBookCount
    }

    // Helper function to calculate days difference
    private fun calculateDifferenceInDays(expiryDate: String): Int {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val returnDate: Date? = dateFormat.parse(expiryDate)
        val currentDate = Date()
        val differenceInTime = currentDate.time - (returnDate?.time ?: currentDate.time)
        return (differenceInTime / (1000 * 60 * 60 * 24)).toInt()
    }

}

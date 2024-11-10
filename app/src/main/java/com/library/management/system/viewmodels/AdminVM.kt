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
import com.library.management.system.utils.ISSUED_BOOK_REF

class AdminVM : ViewModel() {

    var _issuedList = MutableLiveData<List<IssueBooks>>()
    val issuedList: LiveData<List<IssueBooks>> get() = _issuedList
    val _allBooksList = MutableLiveData<List<Books>>()
    val allBooksList: LiveData<List<Books>> get() = _allBooksList


    var fcmToken: String? = null
    var userId: Int? = null

    fun getAllBooks() {
        ALL_BOOK_REF.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allBooksListfetch = mutableListOf<Books>()

                for (bookSnapshot in snapshot.children) {
                    val bookDetails = bookSnapshot.getValue(Books::class.java)
                    if (bookDetails != null) {
                        allBooksListfetch.add(bookDetails)
                        Log.d("FRAG_BOOLEAN", "all book id: ${bookDetails.bookId}")
                    }
                }
                _allBooksList.value = allBooksListfetch
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Database error: ${error.message}")
            }
        })
    }

    fun getIssuedBooks() {
        ISSUED_BOOK_REF.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val issuedBooksListFetch = mutableListOf<IssueBooks>()
                for (userSnapshot in snapshot.children) {
                    for (bookSnapshot in userSnapshot.children) {
                        val bookDetails = bookSnapshot.getValue(IssueBooks::class.java)
                        if (bookDetails != null) {
                            issuedBooksListFetch.add(bookDetails)
                            Log.d(
                                "FRAG_BOOLEAN",
                                "issued bookId details: ${bookDetails.bookId}, UserId: ${bookDetails.userId}, ExpiryDate${bookDetails.expiryDate}"
                            )
                        }
                    }
                }
                _issuedList.value = issuedBooksListFetch
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

}
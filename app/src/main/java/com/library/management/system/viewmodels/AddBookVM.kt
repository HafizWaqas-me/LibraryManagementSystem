package com.library.management.system.viewmodels

import androidx.lifecycle.ViewModel
import com.library.management.system.model.Books

class AddBookVM : ViewModel() {
    var bookId = 0
    var title = ""
    var author = ""
    var category = ""
    var total = 0
    var desc = ""
    var imgUrl = ""
    var status = true

    fun addNewBook(): Books {
       return Books(
            bookId = bookId,
            title = title,
            author = author,
            category = category,
            total = total,
            desc = desc,
            imgUrl = imgUrl,
            status = true

        )
    }

}
package com.library.management.system.views.fragments.admin

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.library.management.system.R
import com.library.management.system.databinding.FragmentIssueBookToUserBinding
import com.library.management.system.notifications.NotificationsFun
import com.library.management.system.utils.ALL_BOOK_REF
import com.library.management.system.utils.ISSUED_BOOK_REF
import com.library.management.system.viewmodels.IssueBookVM
import java.util.Calendar

class IssueBookToUserFragment : Fragment(), View.OnClickListener {
    var _binding: FragmentIssueBookToUserBinding? = null
    val binding get() = _binding
    val viewModel: IssueBookVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIssueBookToUserBinding.inflate(inflater, container, false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding?.issueBookBtn?.setOnClickListener(this)
        binding?.etDatePicker?.setOnClickListener(this)

    }

    private fun initt() {

        binding?.issueBookTopbar?.screenName?.text = "Issue Book"
        binding?.issueBookTopbar?.settingIcon?.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.issue_book_btn -> {
                showProgress()
                viewModel.userId = binding?.etUserId?.text.toString().toInt()
                viewModel.bookId = binding?.etBookId?.text.toString().toInt()
                viewModel.expiryDate = binding?.etDatePicker?.text.toString()
                if (viewModel.isErrorNotVisible()) {

                    fetchBooksById()
                } else {
                    viewModel.checkValues()
                    if (viewModel.userIdError) {
                        binding?.etUserId?.error = "Enter User ID:"
                    }
                    if (viewModel.bookIdError) {
                        binding?.etBookId?.error = "Enter Book ID:"
                    }
                    if (viewModel.expiryDateError) {
                        binding?.etDatePicker?.error = "Select Expiry Date:"
                    }

                }
            }

            R.id.et_date_picker -> {
                binding?.etDatePicker?.let { showDatePickerDialoge(it) }

            }
        }

    }

    private fun showDatePickerDialoge(etDate: EditText) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                calendar.set(selectedYear, selectedMonth, selectedDay)

                viewModel.selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                etDate.setText(viewModel.selectedDate)

            }, year, month, day
        )

        datePickerDialog.show()

        Log.d("BOOKIDD", "DATE: ${viewModel.selectedDate}")
    }

    fun showProgress() {
        binding?.issueBookProgress?.visibility = View.VISIBLE
        binding?.issueBookBtn?.isEnabled = false
    }

    fun hideProgress() {
        binding?.issueBookProgress?.visibility = View.GONE
        binding?.issueBookBtn?.isEnabled = true
    }

    fun fetchBooksById() {
        ALL_BOOK_REF.child("${viewModel.bookId}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModel.imgUrl = snapshot.child("imgUrl")
                        .getValue(String::class.java)
                        .toString()
                    viewModel.title =
                        snapshot.child("title").getValue(String::class.java)
                            .toString()
                    viewModel.author = snapshot.child("author")
                        .getValue(String::class.java)
                        .toString()
                    issuedToSpecificUser(requireContext())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "Firebase",
                        "Error fetching data: ${error.message}"
                    )
                }

            })
    }

    fun issuedToSpecificUser(context: Context) {
        ISSUED_BOOK_REF.child("${viewModel.userId}")
            .child("${viewModel.bookId}")
            .get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(
                        context,
                        "This book is already issued to you",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgress()
                } else {

                    val issuedBookData = mapOf(
                        "userId" to viewModel.userId,
                        "bookId" to viewModel.bookId,
                        "expiryDate" to viewModel.selectedDate,
                        "imgUrl" to viewModel.imgUrl,
                        "title" to viewModel.title,
                        "author" to viewModel.author,
                        "status" to true

                    )
                    Log.d(
                        "BOOKIDD",
                        " uploaded: ${viewModel.imgUrl}, ${viewModel.title} ${viewModel.author}"
                    )

                    ISSUED_BOOK_REF.child("${viewModel.userId}")
                        .child("${viewModel.bookId}")
                        .setValue(issuedBookData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Book issued successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            hideProgress()

                            NotificationsFun.sendNotificationToExternalUser(
                                context = context,
                                headings = "Book Issued to User: ${viewModel.userId}",
                                userId = viewModel.userId,
                                bookId = viewModel.bookId,
                                title = "Book \"${viewModel.title}\" Issued to you",
                                imgUrl = viewModel.imgUrl
                                )

                        }
                        .addOnFailureListener { e ->
                            hideProgress()
                            Toast.makeText(
                                context,
                                "Error issuing book: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }.addOnFailureListener { e ->
                hideProgress()
                Toast.makeText(
                    context,
                    "Error checking book: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}
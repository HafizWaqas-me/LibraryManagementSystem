package com.library.management.system.views.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.library.management.system.R
import com.library.management.system.databinding.FragmentReturnBookFromUserBinding
import com.library.management.system.utils.ISSUED_BOOK_REF
import com.library.management.system.viewmodels.ReturnBookVM


class ReturnBookFromUserFragment : Fragment(), View.OnClickListener {

    var _binding: FragmentReturnBookFromUserBinding? = null
    val binding get() = _binding!!

    val viewModel: ReturnBookVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReturnBookFromUserBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.returnBookBtn.setOnClickListener(this)

    }

    private fun initt() {
        binding.topbarReturnBook.screenName.text = "Return Book"
        binding.topbarReturnBook.settingIcon.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.return_book_btn -> {
                viewModel.userId = binding.etUserId.text.toString().toInt()
                viewModel.bookId = binding.etBookId.text.toString().toInt()
                if (viewModel.isErrorNotVisible()) {
                    progressShow()
                    ISSUED_BOOK_REF.child("${viewModel.userId}")
                        .child("${viewModel.bookId}")
                        .get()
                        .addOnSuccessListener {
                            val bookId = it.child("bookId").getValue(Long::class.java)
                            Log.d("ReturnBookFragg", "Fetched Book Data: ${bookId}")
                            if (bookId?.toInt() == viewModel.bookId) {
                                ISSUED_BOOK_REF.child("${viewModel.userId}")
                                    .child("${viewModel.bookId}")
                                    .removeValue()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Returned Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            progressHide()
                                        } else {
                                            Log.e(
                                                "ReturnBookFragg",
                                                "Error Deleting Book: ${task.exception?.message}"
                                            )
                                            Toast.makeText(
                                                requireContext(),
                                                "Failed to Return Book",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            progressHide()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Book Not Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressHide()
                            }

                        }
                        .addOnFailureListener {
                            Log.e("ReturnBookFragg", "Fetch Error: ${it.message}")
                            Toast.makeText(
                                requireContext(),
                                "Error: ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progressHide()
                        }
                } else {
                    viewModel.checkValue()
                    if (viewModel.bookIdError) {
                        binding.etBookId.error = "Enter Correct Book ID:"
                    }
                    if (viewModel.userIdError) {
                        binding.etUserId.error = "Enter Correct User ID:"
                    }
                }
            }
        }
    }

    fun progressShow() {
        binding.returnBookBtn.isEnabled = false
        binding.progressReturn.visibility = View.VISIBLE
    }

    fun progressHide() {
        binding.returnBookBtn.isEnabled = true
        binding.progressReturn.visibility = View.GONE
    }

}
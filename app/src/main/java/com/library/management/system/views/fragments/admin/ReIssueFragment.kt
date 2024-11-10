package com.library.management.system.views.fragments.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.library.management.system.R
import com.library.management.system.databinding.FragmentReIssueBinding
import com.library.management.system.utils.ISSUED_BOOK_REF
import com.library.management.system.viewmodels.IssueBookVM
import java.util.Calendar

class ReIssueFragment : Fragment(), View.OnClickListener {
    var _binding: FragmentReIssueBinding? = null
    val binding get() = _binding!!
    var imgUrl: String = ""
    var title: String = ""
    var author: String = ""

    val viewModel: IssueBookVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReIssueBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.reIssueBtn.setOnClickListener(this)
        binding.reIssueDate.setOnClickListener(this)
    }

    private fun initt() {
        binding.reIssueTopbar.screenName.text = "Re-Issue Book"
        binding.reIssueTopbar.settingIcon.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.re_issue_btn -> {
                if (binding.etUserId.text?.isNotEmpty()==true &&
                    binding.etBookId.text?.isNotEmpty()==true) {
                    viewModel.userId = binding.etUserId.text.toString().toInt()
                    viewModel.bookId = binding.etBookId.text.toString().toInt()
                }
                viewModel.expiryDate = binding.reIssueDate.text.toString()
                if (viewModel.isErrorNotVisible()) {
                    showProgress()
                    ISSUED_BOOK_REF.child("${viewModel.userId}")
                        .child("${viewModel.bookId}")
                        .child("expiryDate").setValue(viewModel.expiryDate)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Book reissued successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            hideProgress()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Book not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                            hideProgress()
                        }

                } else {
                    viewModel.checkValues()
                    if (viewModel.userIdError) {
                        binding.etUserId.error = "Enter User ID:"
                    }
                    if (viewModel.bookIdError) {
                        binding.etBookId.error = "Enter Book ID:"
                    }
                    if (viewModel.expiryDateError) {
                        binding.reIssueDate.error = "Select Expiry Date:"
                    }
                }
            }

            R.id.re_issue_date -> {
                showDatePickerDialoge(binding.reIssueDate)
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
        binding.reIssueProgress.visibility = View.VISIBLE
        binding.reIssueBtn.isEnabled = false
    }

    fun hideProgress() {
        binding.reIssueProgress.visibility = View.GONE
        binding.reIssueBtn.isEnabled = true
    }

}

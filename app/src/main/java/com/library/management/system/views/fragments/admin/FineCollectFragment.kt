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
import com.library.management.system.databinding.FragmentFineCollectBinding
import com.library.management.system.utils.ISSUED_BOOK_REF
import com.library.management.system.viewmodels.FineVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FineCollectFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFineCollectBinding? = null
    private val binding get() = _binding

    val viewModel: FineVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFineCollectBinding.inflate(inflater, container, false)



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding?.fineBtn?.setOnClickListener(this)

    }

    private fun initt() {
        binding?.fineTopbar?.screenName?.text = "Check Fine"
        binding?.fineTopbar?.settingIcon?.visibility = View.GONE


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fine_btn -> {
                if (binding?.etUserIdFine?.text.toString().isNotEmpty()
                    && binding?.etBookId?.text.toString().isNotEmpty()
                ) {
                    viewModel.bookId = binding?.etBookId?.text.toString().toInt()
                    viewModel.userId = binding?.etUserIdFine?.text.toString().toInt()
                    if (viewModel.isErrorNotVisible()) {
                        binding?.fineProgress?.visibility = View.VISIBLE
                        binding?.fineBtn?.isEnabled = false

                        getTotalFine()
                    } else {
                        viewModel.checkValues()
                        if (viewModel.userIdError) {
                            binding?.etUserIdFine?.error = "Enter Correct User ID:"
                        }
                        if (viewModel.bookIdError) {
                            binding?.etBookId?.error = "Enter Correct Book ID:"
                        }
                        Toast.makeText(requireContext(), "Else ", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    if (binding?.etUserIdFine?.text.toString().isEmpty()) {
                        binding?.etUserIdFine?.error = "Enter Correct User ID:"

                    }
                    if (binding?.etBookId?.text.toString().isEmpty()) {
                        binding?.etBookId?.error = "Enter Correct User ID:"

                    }
                }


            }
        }
    }

    fun getTotalFine() {
        ISSUED_BOOK_REF.child("${viewModel.userId}").child("${viewModel.bookId}")
            .get().addOnSuccessListener {
                val returnDateString = it.child("expiryDate").getValue(String::class.java)
                Log.d("Finee", "$returnDateString")
                if (returnDateString != null) {
                    if (returnDateString.isNotEmpty()) {

                        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        val returnDate: Date? = dateFormat.parse(returnDateString)

                        val currentDate = Date()

                        val differenceInTime =
                            currentDate.time - (returnDate?.time ?: currentDate.time)
                        val differenceInDays =
                            (differenceInTime / (1000 * 60 * 60 * 24)).toInt()

                        if (differenceInDays > 0) {
                            val fine = differenceInDays * 50
                            binding?.fineLayout?.visibility = View.VISIBLE
                            binding?.tvSetFine?.text = "$fine"

                            binding?.fineProgress?.visibility = View.GONE
                            binding?.fineBtn?.isEnabled = true
                            Log.d(
                                "Finee",
                                "User is ${differenceInDays} days late. Fine: Rs. $fine"
                            )
                        } else {
                            Log.d("Finee", "No fine. The book is returned on time.")
                            binding?.fineLayout?.visibility = View.VISIBLE
                            binding?.tvSetFine?.text = "0"
                            binding?.fineProgress?.visibility = View.GONE
                            binding?.fineBtn?.isEnabled = true


                        }
                    }
                } else {
                    Log.d("Finee", "Book Not Found")
                    binding?.fineLayout?.visibility = View.VISIBLE
                    binding?.tvFineBook?.text = "Book Not Found"
                    binding?.tvSetFine?.visibility = View.GONE
                    binding?.fineProgress?.visibility = View.GONE
                    binding?.fineBtn?.isEnabled = true
                }


            }.addOnFailureListener {
                binding?.fineProgress?.visibility = View.GONE
                binding?.fineBtn?.isEnabled = true
            }
    }

}
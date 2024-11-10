package com.library.management.system.views.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.library.management.system.R
import com.library.management.system.databinding.FragmentDetailBinding
import com.library.management.system.utils.ALL_BOOK_REF
import com.library.management.system.utils.IS_ADMIN
import com.library.management.system.utils.SharedPrefUtil

class DetailFragment : Fragment(), View.OnClickListener {
    var _binding: FragmentDetailBinding? = null
    val binding get() = _binding

    var bookId: Int? = null
    val arg: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding?.updateBookBtn?.setOnClickListener(this)
        binding?.removeBookBtn?.setOnClickListener(this)


    }

    private fun initt() {

        //  Log.d("GETBOOKID","${getBookId()}")

        binding?.topbarDetail?.screenName?.text = "Book Detail"
        binding?.topbarDetail?.settingIcon?.visibility = View.GONE

        if (SharedPrefUtil.getLoginBoolean(IS_ADMIN)) {

        } else {
            binding?.updateBookBtn?.visibility = View.GONE
            binding?.removeBookBtn?.visibility = View.GONE
        }

        ALL_BOOK_REF.child("${arg.id}").get()
            .addOnSuccessListener {
                Log.d("Docc", "${it}")

                binding?.imgUpload?.let { it1 ->
                    Glide
                        .with(requireContext())
                        .load(it.child("imgUrl").getValue(String::class.java))
                        .centerCrop()
                        .placeholder(R.drawable.loading_img)
                        .into(it1)
                }

                binding?.etBookId?.text = "ID: " + arg.id.toString()
                binding?.tvAuthorDetail?.text =
                    "Author: " + it.child("author").getValue(String::class.java)
                binding?.etBookTitle?.text =
                    "Title: " + it.child("title").getValue(String::class.java)
                binding?.bookCategory?.text =
                    "Category: " + it.child("category").getValue(String::class.java)
                binding?.desc?.text = it.child("desc").getValue(String::class.java)
            }.addOnFailureListener {

            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.update_book_btn -> {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToUpdateBookFragment(
                        arg.id
                    )
                )
            }

            R.id.remove_book_btn -> {
                ALL_BOOK_REF.child("${arg.id}").removeValue(
                ).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Book Removed Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    findNavController().popBackStack()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}
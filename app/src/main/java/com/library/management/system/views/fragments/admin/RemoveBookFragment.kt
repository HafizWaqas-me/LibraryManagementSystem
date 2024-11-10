package com.library.management.system.views.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.library.management.system.R
import com.library.management.system.databinding.FragmentRemoveBookBinding
import com.library.management.system.utils.ALL_BOOK_REF


class RemoveBookFragment : Fragment(), View.OnClickListener {

    var _binding: FragmentRemoveBookBinding? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRemoveBookBinding.inflate(inflater, container, false)



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding?.removeBtn?.setOnClickListener(this)


    }

    private fun initt() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.remove_btn -> {
                removeBook()

            }
        }

    }

    fun removeBook() {
        binding?.removeProgress?.visibility = View.VISIBLE
        binding?.removeBtn?.isEnabled = false
        ALL_BOOK_REF.child("${binding?.etBookId?.text.toString().toInt()}").updateChildren(
            mapOf(
                "status" to false
            )
        ).addOnSuccessListener {
            Toast.makeText(requireContext(), "Book Removed", Toast.LENGTH_SHORT).show()
            binding?.removeProgress?.visibility = View.GONE
            binding?.removeBtn?.isEnabled = true
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            binding?.removeProgress?.visibility = View.GONE
            binding?.removeBtn?.isEnabled = true
        }
    }

}
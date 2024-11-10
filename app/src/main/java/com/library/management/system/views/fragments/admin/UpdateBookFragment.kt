package com.library.management.system.views.fragments.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.library.management.system.R
import com.library.management.system.databinding.FragmentUpdateBookBinding
import com.library.management.system.utils.ALL_BOOK_REF
import com.library.management.system.utils.BOOKS_CATEGORIES
import java.util.UUID

class UpdateBookFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentUpdateBookBinding? = null
    val binding get() = _binding!!

    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    var imgUrl: String? = null
    var selectedCategory: String? = null

    val args: UpdateBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBookBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.updateBookBtn.setOnClickListener(this)
        binding.imgUploadUpdate.setOnClickListener(this)

    }

    private fun initt() {
        loadSnipperValues()
        binding.topbarUpdate.screenName.text = "Update Book"
        binding.topbarUpdate.settingIcon.visibility = View.GONE

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageUri = result.data?.data
                    imageUri?.let {
                        // Load image into ImageView using Glide
                        binding.let { it1 -> Glide.with(this).load(it).into(it1.imgUploadUpdate) }
                    }
                }
            }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.update_book_btn -> {
                uploadImageGetUrl()
            }

            R.id.img_upload_update -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            }
        }
    }

    fun updateBook() {
        progressShow()
        val updatedData = mapOf(
            "bookId" to args.id,
            "author" to binding.bookAuthorUpdateBook.text.toString(),
            "category" to binding.spinnerUpdateBook.selectedItem.toString(),
            "imgUrl" to imgUrl,
            "title" to binding.etBookTitle.text.toString(),
            "total" to binding.etBookTotal.text.toString().toInt()
        )
        ALL_BOOK_REF.child("${args.id}").updateChildren(
            updatedData
        ).addOnSuccessListener {
            progressHide()
            Toast.makeText(requireContext(), "Book Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            progressHide()
        }
    }

    private fun uploadImageGetUrl() {

        imageUri?.let {
            Log.d("URII", "$imageUri")
            val storageRef =
                FirebaseStorage.getInstance().reference.child("bookImages/${UUID.randomUUID()}")

            storageRef.putFile(it).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    imgUrl = downloadUrl.toString()
                    Log.d("URLL", "$downloadUrl")
                    updateBook()

                }
            }
        } ?: run {
            // Handle case where no image was selected
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadSnipperValues() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, BOOKS_CATEGORIES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUpdateBook.adapter = adapter
        binding.spinnerUpdateBook.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = BOOKS_CATEGORIES[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
    }

    fun progressShow() {
        binding.updateBookBtn.isEnabled = false
        binding.updateBookProgress.visibility = View.VISIBLE
    }

    fun progressHide() {
        binding.updateBookBtn.isEnabled = true
        binding.updateBookProgress.visibility = View.GONE
    }
}
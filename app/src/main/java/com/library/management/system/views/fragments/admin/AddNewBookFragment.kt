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
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.library.management.system.R
import com.library.management.system.databinding.FragmentAddNewBookBinding
import com.library.management.system.model.Books
import com.library.management.system.utils.ALL_BOOK_REF
import com.library.management.system.utils.AppUtils
import com.library.management.system.utils.BOOKS_CATEGORIES
import com.library.management.system.viewmodels.AddBookVM
import java.util.UUID


class AddNewBookFragment : Fragment(), View.OnClickListener {

    var _binding: FragmentAddNewBookBinding? = null
    val binding get() = _binding

    var selectedCategory: String? = null
    val viewModel: AddBookVM by viewModels()

    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()

    }

    private fun registerClicks() {
        binding?.addBookBtn?.setOnClickListener(this)
        binding?.imgUpload?.setOnClickListener(this)

    }

    private fun initt() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, BOOKS_CATEGORIES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.addBookSpinner?.adapter = adapter
        binding?.addBookSpinner?.onItemSelectedListener =
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

        binding?.addBookTopbar?.screenName?.text = "Add Book"
        binding?.addBookTopbar?.settingIcon?.visibility = View.GONE

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageUri = result.data?.data
                    imageUri?.let {
                        // Load image into ImageView using Glide
                        binding?.let { it1 -> Glide.with(this).load(it).into(it1.imgUpload) }
                    }
                }
            }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_book_btn -> {
                addNewBook(Books())
            }

            R.id.img_upload -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)

            }
        }
    }


    private fun addNewBook(book: Books) {
        Log.d("BOOKSDATA", "$book")
        viewModel.bookId = binding?.etBookId?.text.toString().toInt()
        viewModel.title = binding?.etBookTitle?.text.toString()
        viewModel.author = binding?.etBookAuthor?.text.toString()
        viewModel.category = binding?.addBookSpinner?.selectedItem.toString()
        viewModel.total = binding?.etBookTotal?.text.toString().toInt()
        viewModel.desc = binding?.desc?.text.toString()
        progressEnable()

        ALL_BOOK_REF.child("${viewModel.bookId}")
            .get().addOnSuccessListener {
                if (it.exists()) {
                    Log.d("BOOKSDATA", "$it")

                    Toast.makeText(
                        requireContext(),
                        "Book ID already available",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    progressDisable()
                } else {

                    uploadImageGetUrl()

                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressDisable()
            }

    }

    private fun uploadImageGetUrl() {

        imageUri?.let {
            Log.d("URII", "$imageUri")
            val storageRef =
                FirebaseStorage.getInstance().reference.child("bookImages/${UUID.randomUUID()}")

            storageRef.putFile(it).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    viewModel.imgUrl = downloadUrl.toString()
                    Log.d("URLL", "$downloadUrl")
                   addNewBook()
                }
            }
        } ?: run {
            // Handle case where no image was selected
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    fun addNewBook() {
        ALL_BOOK_REF.child("${viewModel.bookId}").setValue(
            viewModel.addNewBook()
        ).addOnSuccessListener {
            Toast.makeText(
                requireContext(),
                "Book Added",
                Toast.LENGTH_SHORT
            ).show()
            AppUtils.showNotificationEveryOne(
                context = requireContext(),
                title = viewModel.title,
                bookId = viewModel.bookId.toString(),
                imgUrl = viewModel.imgUrl
            )

            progressDisable()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "${it.message}",
                Toast.LENGTH_SHORT
            ).show()
            progressDisable()
        }
    }

    private fun progressEnable() {
        binding?.addBookProgress?.visibility = View.VISIBLE
        binding?.addBookBtn?.isEnabled = false
    }

    private fun progressDisable() {
        binding?.addBookProgress?.visibility = View.GONE
        binding?.addBookBtn?.isEnabled = true
    }
}
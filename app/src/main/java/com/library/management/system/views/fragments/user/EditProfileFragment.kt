package com.library.management.system.views.fragments.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.library.management.system.R
import com.library.management.system.databinding.FragmentEditProfileBinding
import com.library.management.system.utils.USER_SIGNUP_REF
import java.util.UUID

class EditProfileFragment : Fragment(), View.OnClickListener {
    var _binding: FragmentEditProfileBinding? = null
    val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    val args: EditProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()


    }

    fun initt() {
        Log.d("EditProfileFragg", "$args")
        Glide.with(requireContext())
            .load(args.user.imgUrl)
            .placeholder(R.drawable.loading_img)
            .into(binding.imgProfile)
        binding.etName.setText(args.user.fullName)
        binding.etEmail.setText(args.user.email)
        binding.etPhone.setText(args.user.mobile.toString())


        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageUri = result.data?.data
                    imageUri?.let {
                        // Load image into ImageView using Glide
                        binding.let { it1 -> Glide.with(this).load(it).into(it1.imgProfile) }
                    }
                }
            }
    }


    fun registerClicks() {
        binding.btnSaveChanges.setOnClickListener(this)
        binding.imgProfile.setOnClickListener(this)
        Log.d("EditProfileFragg","Image Clicked")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSaveChanges -> {
                binding.btnSaveChanges.isEnabled = false
                binding.progressUpdateProfile.visibility = View.VISIBLE
                uploadImageGetUrl()
            }
            R.id.imgProfile->{
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            }
        }
    }

    fun updateUserProfile(imgUrl:String) {
        USER_SIGNUP_REF.child("${args.user.userId}").updateChildren(
            mapOf(
                "fullName" to binding.etName.text.toString(),
                "email" to binding.etEmail.text.toString(),
                "mobile" to binding.etPhone.text.toString(),
                "imgUrl" to imgUrl
            )
        ).addOnSuccessListener {
            Toast.makeText(requireContext(),"Updated", Toast.LENGTH_SHORT).show()
            binding.btnSaveChanges.isEnabled = true
            binding.progressUpdateProfile.visibility = View.GONE
        }
    }

    private fun uploadImageGetUrl() {

        imageUri?.let {
            Log.d("URII", "$imageUri")
            val storageRef =
                FirebaseStorage.getInstance().reference.child("bookImages/${UUID.randomUUID()}")

            storageRef.putFile(it).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

//                    imgUrl = downloadUrl.toString()
//                    Log.d("URLL", "$downloadUrl")
                    updateUserProfile(downloadUrl.toString())

                }
            }
        } ?: run {
            // Handle case where no image was selected
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

}
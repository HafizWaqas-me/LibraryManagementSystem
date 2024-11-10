package com.library.management.system.views.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.library.management.system.R
import com.library.management.system.databinding.FragmentSignUpBinding
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.FCM_TOKEN
import com.library.management.system.utils.USER_SIGNUP_REF
import com.library.management.system.viewmodels.SignUpVM

class SignUpFragment : Fragment(), View.OnClickListener {
    var _binding: FragmentSignUpBinding? = null
    val binding get() = _binding!!

    val viewModel: SignUpVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.signupBtn.setOnClickListener(this)

    }

    private fun initt() {
        binding.signupTopbar.screenName.text = "Sign Up"
        binding.signupTopbar.settingIcon.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signup_btn -> {

                viewModel.fullName = binding.etFullName.text.toString()
                viewModel.email = binding.etEmailSignup.text.toString()
                viewModel.userId = binding.userIdSignup.text.toString().toInt()
                viewModel.pass = binding.etPassSignup.text.toString()
                viewModel.cPass = binding.etCPassSignup.text.toString()

                if (viewModel.isErrorNotVisible()) {
                    showProgress()
                    AUTH.createUserWithEmailAndPassword(viewModel.email, viewModel.pass)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Signup Successfully", Toast.LENGTH_SHORT
                            ).show()

                            FCM_TOKEN.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.isSuccessful)
                                        viewModel.fcmToken = task.result
                                    Log.d("FCM_TOKEN", viewModel.fcmToken)

                                    USER_SIGNUP_REF.child("${viewModel.userId}")
                                        .setValue(
                                            viewModel.uploadSignupData()
                                        )
                                        .addOnSuccessListener {

                                            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragments())

                                        }.addOnFailureListener {
                                            hideProgress()
                                            Toast.makeText(
                                                requireContext(),
                                                "${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                } else {

                                    Log.d("FCM_TOKEN", "Task failed to complete")
                                }
                            }.addOnFailureListener {
                                Log.d("FCM_TOKEN", "FCM-Token not uploaded: ${it.message}")
                            }

                        }.addOnFailureListener {
                            hideProgress()
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT)
                                .show()

                        }

                } else {
                    hideProgress()
                    viewModel.checkValues()
                    if (viewModel.fullNameError) {
                        binding.etFullName.error = "Enter Full Name"
                    }
                    if (viewModel.userIdError) {
                        binding.userIdSignup.error = "Enter Enrollment ID"
                    }

                    if (viewModel.emailError) {
                        binding.etEmailSignup.error = "Enter Full Name"
                    }
                    if (viewModel.passError) {
                        binding.etPassSignup.error = "Enter Full Name"
                    }
                    if (viewModel.cPassError) {
                        binding.etCPassSignup.error = "Enter Full Name"
                    }

                }
            }

        }
    }

    fun showProgress() {
        binding.signupProgress.visibility = View.VISIBLE
        binding.signupBtn.isEnabled = false
    }

    fun hideProgress() {
        binding.signupProgress.visibility = View.GONE
        binding.signupBtn.isEnabled = true
    }
}
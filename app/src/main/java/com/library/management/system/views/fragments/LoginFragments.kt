package com.library.management.system.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.library.management.system.R
import com.library.management.system.databinding.FragmentLoginFragmentsBinding
import com.library.management.system.utils.ADMIN_EMAIL_LIST
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.IS_ADMIN
import com.library.management.system.utils.LOGIN_EMAIL
import com.library.management.system.utils.LOGIN_EMAIL_BOOLEAN
import com.library.management.system.utils.SharedPrefUtil
import com.library.management.system.viewmodels.LoginVM

class LoginFragments : Fragment(), View.OnClickListener {
    var _binding: FragmentLoginFragmentsBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginVM by viewModels()
    var allEmails = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginFragmentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initt()
        registerClicks()
        userSession()

    }

    private fun userSession() {
        if (SharedPrefUtil.getLoginBoolean(LOGIN_EMAIL_BOOLEAN)) {
            if (SharedPrefUtil.getLoginBoolean(IS_ADMIN)) {
                Log.d(
                    "LoginFragg",
                    "IsAdmin: ${SharedPrefUtil.getLoginBoolean(IS_ADMIN)}, " +
                            "LogInEmailBoolean: ${SharedPrefUtil.getLoginBoolean(LOGIN_EMAIL_BOOLEAN)}"
                )
                findNavController().navigate(LoginFragmentsDirections.actionLoginFragmentsToAdminDashContainer())
            } else {
                findNavController().navigate(LoginFragmentsDirections.actionLoginFragmentsToUserFragContainer())

            }
            Log.d(
                "EMAILL", "${SharedPrefUtil.getLoginBoolean(LOGIN_EMAIL_BOOLEAN)} ${
                    SharedPrefUtil.getLoginBoolean(
                        IS_ADMIN
                    )
                }"
            )
        }

    }

    private fun registerClicks() {
        binding.loginBtn.setOnClickListener(this)
        binding.noAccountSignup.setOnClickListener(this)

    }

    private fun initt() {
        binding.topbarLogin.screenName.text = "Login"
        binding.topbarLogin.settingIcon.visibility = View.GONE

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn -> {

                viewModel.email = binding.etEmail.text.toString()
                viewModel.pass = binding.etPass.text.toString()
                if (viewModel.isErrorNotVisible()) {
                    binding.loginProgress.visibility = View.VISIBLE
                    binding.loginBtn.isEnabled = false

                    AUTH.signInWithEmailAndPassword(viewModel.email, viewModel.pass)
                        .addOnSuccessListener {

                            Toast.makeText(
                                requireContext(),
                                "Log in Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            //saved
                            SharedPrefUtil.setLoginValue(LOGIN_EMAIL, viewModel.email)
                            SharedPrefUtil.setLoginBoolean(LOGIN_EMAIL_BOOLEAN, true)
                            ADMIN_EMAIL_LIST.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snaps in snapshot.children) {
                                        val emails =
                                            snaps.child("email").getValue(String::class.java)
                                        // val adminEmails = snapshot.map { it.getString("email") }
                                        Log.d("LoginFragg", "All Emails: $emails")
                                        if (emails != null) {
                                            allEmails.add(emails)
                                        }

                                    }
                                    if (allEmails != null) {
                                        if (allEmails.contains(viewModel.email)) {
                                            SharedPrefUtil.setLoginBoolean(IS_ADMIN, true)

                                            findNavController().navigate(
                                                LoginFragmentsDirections.actionLoginFragmentsToAdminDashContainer()
                                            )
                                        } else {
                                            Log.d("LoginFragg", "User Fragment ->")
                                            findNavController().navigate(
                                                LoginFragmentsDirections.actionLoginFragmentsToUserFragContainer()
                                            )
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        requireContext(),
                                        error.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.loginProgress.visibility = View.INVISIBLE
                                    binding.loginBtn.isEnabled = true
                                }

                            })

                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding.loginProgress.visibility = View.INVISIBLE
                            binding.loginBtn.isEnabled = true
                        }

                } else {
                    viewModel.checkValues()
                    if (viewModel.emailError) {
                        binding.etEmail.error = "Enter Valid Email"
                    }
                    if (viewModel.passError) {
                        binding.etPass.error = "Enter Correct Password"
                    }
                }
            }

            R.id.no_account_signup -> {
                findNavController().navigate(LoginFragmentsDirections.actionLoginFragmentsToSignUpFragment())
            }
        }
    }

}
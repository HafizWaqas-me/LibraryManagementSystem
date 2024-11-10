package com.library.management.system.views.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.library.management.system.R
import com.library.management.system.databinding.FragmentAdminSettingBinding
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.IS_ADMIN
import com.library.management.system.utils.LOGIN_EMAIL
import com.library.management.system.utils.LOGIN_EMAIL_BOOLEAN
import com.library.management.system.utils.SharedPrefUtil

class AdminSetting : Fragment(), View.OnClickListener {

    var _binding: FragmentAdminSettingBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.addBookConstraint.setOnClickListener(this)

        binding.issueBookConstraint.setOnClickListener(this)
        binding.reissueBookConstraint.setOnClickListener(this)
        binding.returnBookConstraint.setOnClickListener(this)
        binding.collectFineConstraint.setOnClickListener(this)
        binding.logOutAdmin.setOnClickListener(this)

    }

    private fun initt() {
        binding.topbarAdminSetting.screenName.text = "Admin Controle"
        binding.topbarAdminSetting.settingIcon.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_book_constraint -> {
                findNavController().navigate(AdminSettingDirections.actionAdminSettingToAddNewBookFragment())
            }

            R.id.issue_book_constraint -> {
                findNavController().navigate(AdminSettingDirections.actionAdminSettingToIssueBookToUserFragment())

            }

            R.id.reissue_book_constraint -> {
                findNavController().navigate(AdminSettingDirections.actionAdminSettingToReIssueFragment())

            }

            R.id.return_book_constraint -> {
                findNavController().navigate(AdminSettingDirections.actionAdminSettingToReturnBookFromUserFragment())

            }

            R.id.collect_fine_constraint -> {
                findNavController().navigate(AdminSettingDirections.actionAdminSettingToFineCollectFragment())

            }

            R.id.log_out_admin -> {
                AUTH.signOut()
                if (AUTH.currentUser == null) {
                    SharedPrefUtil.setLoginValue(LOGIN_EMAIL, null)
                    SharedPrefUtil.setLoginBoolean(LOGIN_EMAIL_BOOLEAN, false)
                    SharedPrefUtil.setLoginBoolean(IS_ADMIN, false)
                    Toast.makeText(requireContext(), "Sign Out Success", Toast.LENGTH_SHORT).show()
                    requireActivity().recreate()
                    findNavController().navigate(AdminSettingDirections.actionAdminSettingToLoginFragments())

                }
            }
        }
    }
}



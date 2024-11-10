package com.library.management.system.views.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.library.management.system.R
import com.library.management.system.databinding.FragmentAdminDashContainerBinding
import com.library.management.system.model.CardClick
import com.library.management.system.views.adapters.AdminFramentStateAdapter

class AdminDashContainer : Fragment(), View.OnClickListener, CardClick {
    var _binding: FragmentAdminDashContainerBinding? = null
    val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminDashContainerBinding.inflate(inflater, container, false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding?.topBarUser?.settingIcon?.setOnClickListener(this)


    }

    private fun initt() {

        binding?.topBarUser?.screenName?.text = "Admin DashBoard"

        val adapter = AdminFramentStateAdapter(this, false, this)
        binding?.viewPager?.adapter = adapter

        binding?.tabLayout?.let {
            TabLayoutMediator(it, binding!!.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Issued Books"
                    else -> "All Books"
                }
            }.attach()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_icon -> {
               findNavController().navigate(AdminDashContainerDirections.actionAdminDashContainerToAdminSetting())
            }
        }
    }

    override fun onCardClick(id: Int) {
        try {
            Log.d("ADMINDASHH", "${id}")

            findNavController().navigate(AdminDashContainerDirections.actionAdminDashContainerToDetailFragment(id))
        } catch (e: Exception) {
            Log.d("ADMINDASHH", "${e.message}")
        }
    }


}
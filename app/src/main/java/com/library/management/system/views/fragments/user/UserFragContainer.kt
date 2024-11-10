package com.library.management.system.views.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.library.management.system.R
import com.library.management.system.databinding.FragmentUserFragContainerBinding
import com.library.management.system.model.CardClick
import com.library.management.system.views.adapters.FragmentAdapter

class UserFragContainer : Fragment(), View.OnClickListener, CardClick {
    var _binding: FragmentUserFragContainerBinding? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserFragContainerBinding.inflate(inflater, container, false)



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

        binding?.topBarUser?.screenName?.text = "User DashBoard"

        val adapter = FragmentAdapter(this, false, this)
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
                findNavController().navigate(UserFragContainerDirections.actionUserFragContainerToUserSettingFragment2())
            }
        }
    }

    override fun onCardClick(id: Int) {
        findNavController().navigate(
            UserFragContainerDirections.actionUserFragContainerToDetailFragment(
                id
            )
        )

    }


}



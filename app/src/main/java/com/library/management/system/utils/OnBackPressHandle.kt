package com.library.management.system.utils

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

object OnBackPressHandle {
    var backPressedTime: Long = 0

    fun Fragment.handleBackPress() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (requireActivity().supportFragmentManager.backStackEntryCount > 1) {
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        // Check for double press
                        if (backPressedTime + 2000 > System.currentTimeMillis()) {
                            requireActivity().finish() // Close the app
                        } else {
                            backPressedTime =
                                System.currentTimeMillis() // Update the time of the last press
                            Toast.makeText(
                                requireContext(),
                                "Double Back Press to exit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )
    }
}
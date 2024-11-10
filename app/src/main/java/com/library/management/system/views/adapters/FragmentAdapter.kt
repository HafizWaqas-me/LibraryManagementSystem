package com.library.management.system.views.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.library.management.system.model.CardClick
import com.library.management.system.views.fragments.user.UserDashboardFragment

class FragmentAdapter(
    fragment: Fragment,
    var condition: Boolean,
    var cardClick: CardClick
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserDashboardFragment().apply {
                arguments = Bundle().apply {
                    condition.let {
                        putBoolean("condition", it)
                    }

                    condition = true
                }
                this.cardClick = this@FragmentAdapter.cardClick
            }

            1 -> UserDashboardFragment().apply {
                arguments = Bundle().apply {
                    condition.let {
                        putBoolean("condition", it)

                    }
                }
                this.cardClick = this@FragmentAdapter.cardClick

            }

            else -> throw IllegalStateException("Unexpected position: $position")

        }
    }
}
package com.library.management.system.views.fragments.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.management.system.R
import com.library.management.system.databinding.FragmentUserDashboardFragmenttBinding
import com.library.management.system.model.CardClick
import com.library.management.system.utils.OnBackPressHandle.handleBackPress
import com.library.management.system.viewmodels.UserDashBoardVM
import com.library.management.system.views.adapters.AllBooksAdapter
import com.library.management.system.views.adapters.UserAdapter

class UserDashboardFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUserDashboardFragmenttBinding? = null
    private val binding get() = _binding!!
    val viewModel: UserDashBoardVM by activityViewModels()
    var userAdapter: UserAdapter? = null
    var allBooksAdapter: AllBooksAdapter? = null
    var isSearchExpanded = false
    var cardClick: CardClick? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDashboardFragmenttBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserId()
        viewModel.getAllBooks()


        setupRecyclerView()
        registerClicks()
        setupObservers()
        handleBackPress()
    }

    private fun setupRecyclerView() {

        binding.userRecycler.layoutManager = LinearLayoutManager(requireContext())

        val condition = arguments?.getBoolean("condition", false) ?: false
        if (condition) {
            allBooksAdapter = AllBooksAdapter(requireContext(), listOf(), cardClick)
            binding.userRecycler.adapter = allBooksAdapter
            viewModel.getAllBooks()
        } else {
            userAdapter = UserAdapter(requireContext(), listOf(), viewModel)
            binding.userRecycler.adapter = userAdapter
            viewModel.getUserId()
        }
    }

    private fun setupObservers() {
        viewModel.issuedBooksListLiveData.observe(viewLifecycleOwner) { issuedBooks ->
            binding.progressUserRecycler.visibility = View.GONE
            if (issuedBooks.isEmpty()) {
                binding.userRecycler.visibility = View.GONE
                binding.emptyViewUser.visibility = View.VISIBLE
            } else {
                binding.emptyViewUser.visibility = View.GONE
                binding.userRecycler.visibility = View.VISIBLE
                userAdapter?.updateList(issuedBooks)
            }
        }


        viewModel.allBooksListLiveData.observe(viewLifecycleOwner) { allBooks ->
            binding.progressUserRecycler.visibility = View.GONE
            if (allBooks.isEmpty()) {
                binding.userRecycler.visibility = View.GONE
                binding.emptyViewUser.visibility = View.VISIBLE
            } else {
                binding.emptyViewUser.visibility = View.GONE
                binding.userRecycler.visibility = View.VISIBLE
                allBooksAdapter?.updateList(allBooks)
            }
        }
    }

    private fun registerClicks() {
        binding.userDashboardMainLayout.setOnClickListener(this)
        binding.searchIconUser.setOnClickListener(this)
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (arguments?.getBoolean("condition", false) == true) {
                    filterAllBooks(text)
                } else {
                    filterIssuedBook(text)
                }
            }
        })

    }

    private fun filterIssuedBook(text: String) {
        val filteredBooks = viewModel.issuedBooksListLiveData.value.orEmpty().filter {
            it.title?.contains(text, ignoreCase = true) == true ||
                    it.author?.contains(text, ignoreCase = true) == true
        }
        userAdapter?.updateList(filteredBooks)
    }

    private fun filterAllBooks(text: String) {
        val filteredBooks = viewModel.allBooksListLiveData.value.orEmpty().filter {
            it.title?.contains(text, ignoreCase = true) == true ||
                    it.author?.contains(text, ignoreCase = true) == true
        }
        allBooksAdapter?.updateList(filteredBooks)
    }

    private fun toggleSearchBar() {
        val constraintLayout: ConstraintLayout = binding.searchContainerr
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        if (isSearchExpanded) {
            // Collapse the search bar
            val widthAnimator = constraintLayout.width.let { ValueAnimator.ofInt(it, 0) }
            widthAnimator?.addUpdateListener { valueAnimator ->
                val layoutParams = binding.searchEditText.layoutParams
                layoutParams?.width = valueAnimator.animatedValue as Int
                binding.searchEditText.layoutParams = layoutParams
            }

            widthAnimator?.duration = 600 // Adjust this to balance speed
            widthAnimator?.start()

            widthAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    binding.searchEditText.visibility = View.GONE
                    binding.searchEditText.setText("")
                }
            })

            binding.searchIconUser.setImageResource(R.drawable.ic_search_24)
            isSearchExpanded = false
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

        } else {
            // Expand the search bar
            binding.searchEditText.visibility = View.VISIBLE
            binding.searchEditText.hint = "Search"
            binding.searchEditText.requestFocus()

            val widthAnimator = ValueAnimator.ofInt(
                0,
                (constraintLayout.parent as View).width * 5 / 10
            ) // 60% of parent width
            widthAnimator.addUpdateListener { valueAnimator ->
                val layoutParams = binding.searchEditText.layoutParams
                layoutParams?.width = valueAnimator.animatedValue as Int
                binding.searchEditText.layoutParams = layoutParams
            }

            widthAnimator.duration = 600 // Keep the same duration for balance
            widthAnimator.start()

            binding.searchIconUser.setImageResource(R.drawable.ic_close_24)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

            isSearchExpanded = true
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_icon_user -> {
                toggleSearchBar()
            }

            R.id.user_dashboard_main_layout -> {
                binding.userDashboardMainLayout.setOnTouchListener { _, event ->
                    if (isSearchExpanded && event.action == MotionEvent.ACTION_DOWN) {
                        toggleSearchBar()
                    }
                    false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

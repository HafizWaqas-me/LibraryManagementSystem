package com.library.management.system.views.fragments.admin

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.management.system.R
import com.library.management.system.databinding.FragmentAdminDashboardBinding
import com.library.management.system.model.CardClick
import com.library.management.system.model.Books
import com.library.management.system.model.IssueBooks
import com.library.management.system.utils.OnBackPressHandle.handleBackPress
import com.library.management.system.viewmodels.AdminVM
import com.library.management.system.views.adapters.AllBooksAdapter
import com.library.management.system.views.adapters.IssuedBookAdapter

class AdminDashboardFragment : Fragment(), View.OnClickListener {

    var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding
    var isSearchExpanded = false

    val viewModel: AdminVM by viewModels()

    var issuedBooksAdapter: IssuedBookAdapter? = null
    var allBooksAdapter: AllBooksAdapter? = null

    var cardClick: CardClick? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)

        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initt()
        registerClicks()
        handleBackPress()
    }

    private fun registerClicks() {
        binding?.searchIconUser?.setOnClickListener(this)
        binding?.adminDashBoardLayout?.setOnClickListener(this)
    }

    private fun initt() {
        viewModel.getAllBooks()
        viewModel.getIssuedBooks()
        binding?.adminRecycler?.layoutManager = LinearLayoutManager(requireContext())
        allBooksAdapter = AllBooksAdapter(requireContext(), emptyList(), cardClick)
        binding?.adminRecycler?.adapter = allBooksAdapter

        val condition = arguments?.getBoolean("condition", false) ?: false
        Log.d("FRAG_BOOLEAN", "conditionValueinitt $condition")

        if (!condition) {
            viewModel.issuedList.observe(viewLifecycleOwner) {
                binding?.progressAdminRecycler?.visibility = View.GONE

                if (it.isEmpty()) {
                    binding?.adminRecycler?.visibility = View.GONE
                    binding?.emptyViewAdmin?.visibility = View.VISIBLE

                } else {
                    binding?.emptyViewAdmin?.visibility = View.GONE
                    issuedBooksAdapter?.updateList(it)

                }
                binding?.adminRecycler?.layoutManager = LinearLayoutManager(requireContext())
                issuedBooksAdapter = IssuedBookAdapter(requireContext(), it)
                binding?.adminRecycler?.adapter = issuedBooksAdapter
                Log.d(
                    "FRAG_BOOLEAN",
                    "all issued bookId: ${viewModel.issuedList}"
                )
            }
        } else {
            viewModel.allBooksList.observe(viewLifecycleOwner) {

                // Hide progress bar after data is loaded
                binding?.progressAdminRecycler?.visibility = View.GONE

                // Check if list is empty after loading all items
                if (it.isEmpty()) {
                    binding?.adminRecycler?.visibility = View.GONE
                    binding?.emptyViewAdmin?.visibility = View.VISIBLE
                } else {
                    binding?.adminRecycler?.visibility = View.VISIBLE
                    binding?.emptyViewAdmin?.visibility = View.GONE
                    allBooksAdapter?.updateList(it)
                }

                // Set up RecyclerView and adapter
                binding?.adminRecycler?.layoutManager = LinearLayoutManager(requireContext())
                allBooksAdapter = AllBooksAdapter(
                    requireContext(),
                    it, cardClick
                )
                binding?.adminRecycler?.adapter = allBooksAdapter
                Log.d("FRAG_BOOLEAN", "all books: ${viewModel.allBooksList}")
            }
        }



        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!condition) {
                    filterIssuedList(s.toString())
                } else {
                    filterAllBooksList(s.toString())
                }
            }

        })
        binding?.progressAdminRecycler?.visibility = View.VISIBLE

        binding?.adminDashBoardLayout?.setOnTouchListener { _, event ->
            if (isSearchExpanded && event.action == MotionEvent.ACTION_DOWN) {
                toggleSearchBar()
            }
            false
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
                 R.id.search_icon_user -> {
                toggleSearchBar()
            }
        }
    }

    fun filterIssuedList(text: String) {

        val filteredCourseAry = mutableListOf<IssueBooks>()

        val courseAry = viewModel.issuedList.value

        if (courseAry != null) {
            for (eachCourse in courseAry) {
                if (eachCourse.title?.lowercase()?.contains(text.lowercase()) == true
                    || eachCourse.author?.lowercase()?.contains(text.lowercase()) == true
                ) {

                    filteredCourseAry.add(eachCourse)
                }
            }
        }

        issuedBooksAdapter?.filterList(filteredCourseAry)
    }

    fun filterAllBooksList(text: String) {

        val filteredCourseAry = mutableListOf<Books>()

        val courseAry = viewModel.allBooksList.value

        if (courseAry != null) {
            for (eachCourse in courseAry) {
                if (eachCourse.title?.lowercase()?.contains(text.lowercase()) == true
                    || eachCourse.author?.lowercase()?.contains(text.lowercase()) == true
                ) {

                    filteredCourseAry.add(eachCourse)
                }
            }
        }

        allBooksAdapter?.filterList(filteredCourseAry)
    }

    private fun toggleSearchBar() {
        val constraintLayout: ConstraintLayout? = binding?.searchContainerr
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        if (isSearchExpanded) {
            val widthAnimator = constraintLayout?.width?.let { ValueAnimator.ofInt(it, 0) }
            widthAnimator?.addUpdateListener { valueAnimator ->
                val layoutParams = binding?.searchEditText?.layoutParams
                layoutParams?.width = valueAnimator.animatedValue as Int
                binding?.searchEditText?.layoutParams = layoutParams
            }

            widthAnimator?.duration = 600
            widthAnimator?.start()

            widthAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    binding?.searchEditText?.visibility = View.GONE
                    binding?.searchEditText?.setText("")
                }
            })

            binding?.searchIconUser?.setImageResource(R.drawable.ic_search_24)
            isSearchExpanded = false
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding?.searchEditText?.windowToken, 0)

        } else {
            // Expand the search bar
            binding?.searchEditText?.visibility = View.VISIBLE
            binding?.searchEditText?.hint = "Search"
            binding?.searchEditText?.requestFocus()

            val widthAnimator = ValueAnimator.ofInt(
                0,
                (constraintLayout?.parent as View).width * 5 / 10
            )
            widthAnimator.addUpdateListener { valueAnimator ->
                val layoutParams = binding?.searchEditText?.layoutParams
                layoutParams?.width = valueAnimator.animatedValue as Int
                binding?.searchEditText?.layoutParams = layoutParams
            }

            widthAnimator.duration = 600
            widthAnimator.start()

            binding?.searchIconUser?.setImageResource(R.drawable.ic_close_24)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding?.searchEditText, InputMethodManager.SHOW_IMPLICIT)

            isSearchExpanded = true
        }

    }

}
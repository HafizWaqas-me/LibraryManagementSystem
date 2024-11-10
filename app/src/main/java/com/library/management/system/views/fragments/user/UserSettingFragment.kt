package com.library.management.system.views.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.library.management.system.R
import com.library.management.system.databinding.FragmentUserSettingBinding
import com.library.management.system.model.SignUpUsersData
import com.library.management.system.utils.AUTH
import com.library.management.system.utils.LOGIN_EMAIL
import com.library.management.system.utils.LOGIN_EMAIL_BOOLEAN
import com.library.management.system.utils.SharedPrefUtil
import com.library.management.system.utils.USER_SIGNUP_REF
import com.library.management.system.viewmodels.UserDashBoardVM

class UserSettingFragment : Fragment(), View.OnClickListener {

    var _binding: FragmentUserSettingBinding? = null
    val binding get() = _binding!!
    var userId: Int? = null
    val viewModel: UserDashBoardVM by activityViewModels()
    var userData: SignUpUsersData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initt()
        registerClicks()
    }

    private fun registerClicks() {
        binding.logOutUser.setOnClickListener(this)
        binding.editImage.setOnClickListener(this)

    }

    private fun initt() {
        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            USER_SIGNUP_REF.child(userId.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    userData = snapshot.getValue(SignUpUsersData::class.java)
                    Log.d("UserSettingFragg", "userData $userData")

                    Glide.with(requireContext()).load(userData?.imgUrl)
                        .placeholder(R.drawable.loading_img).into(binding.profileImage)
                    binding.userNameProfile.text = userData?.fullName
                    binding.userEmailProfile.text = userData?.email
                    binding.userIdProfile.text = userData?.userId.toString()
                    binding.editImage.visibility = View.VISIBLE

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            viewModel.issuedBooksListLiveData.observe(viewLifecycleOwner) { issuedBooks ->
                binding.totalIssuedBooks.text = "Issued Books: ${issuedBooks.size}"

            }
//            binding.totalIssuedBooks.text =
//                "Issued Books: ${viewModel.issuedBooksListLiveData.value?.size}"
            viewModel.totalFine.observe(viewLifecycleOwner) { totalFine ->
                binding.totalFine.text = "Total Fine: Rs.$totalFine"

            }
            viewModel.expiredBooks.observe(viewLifecycleOwner) { expiredBooks ->
                binding.totalExpiredBooks.text = "Expired Books: $expiredBooks"

            }

            binding.userTopbar.screenName.text = "User Setting"
            binding.userTopbar.settingIcon.visibility = View.GONE


        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.log_out_user -> {
                AUTH.signOut()

                if (AUTH.currentUser == null) {
                    SharedPrefUtil.setLoginBoolean(LOGIN_EMAIL_BOOLEAN, false)
                    SharedPrefUtil.setLoginValue(LOGIN_EMAIL, null)
                    Toast.makeText(requireContext(), "Log out success", Toast.LENGTH_SHORT).show()
                    viewModel.clearData()
                    requireActivity().recreate()
                    findNavController().navigate(UserSettingFragmentDirections.actionUserSettingFragmentToLoginFragments())

                }
            }

            R.id.editImage -> {

                val user: SignUpUsersData = userData!!

                findNavController().navigate(
                    UserSettingFragmentDirections
                        .actionUserSettingFragmentToEditProfileFragment(user)
                )

                Log.d("UserSettingFragg", "onClick $user")


            }
        }
    }

}
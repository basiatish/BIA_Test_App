package com.basiatish.biatestapp.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.ProfileFragmentBinding
import com.basiatish.biatestapp.ui.login.LoginActivity
import com.basiatish.biatestapp.utils.GlideApp
import com.basiatish.domain.entities.Profile
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.profileComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadProfile()
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.logOutContainer.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.sickListContainer.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToSickListFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        viewModel.profile.observe(this.viewLifecycleOwner) {
            if (it != null) {
                bind(it)
            }
        }
    }

    private fun bind(profile: Profile) {
        binding.apply {
            profileName.text = profile.userName
            profileType.text = profile.userType
            numberInCompany.text = profile.compNumber
            phoneNumber.text = profile.phone
            citizenship.text = profile.citizenship
            carType.text = profile.carType
            carNumber.text = profile.carNumber
        }
        loadPhoto(profile.photoUrl)
    }

    private fun loadPhoto(url: String) {
        val storage = Firebase.storage
        val usersRef = storage.reference.child("users/employees")
        val photoRef = usersRef.child(url)
        GlideApp.with(requireContext()).load(photoRef)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(true)
            .error(R.drawable.bia_logo)
            .fitCenter()
            .circleCrop()
            .into(binding.profilePhoto)
    }
}
package com.basiatish.biatestapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.databinding.LoginFragmentBinding
import com.basiatish.biatestapp.utils.PhoneTextWatcher
import javax.inject.Inject


class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var phoneWatcher: PhoneTextWatcher

    @Inject lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.loginComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneWatcher = PhoneTextWatcher(binding.input, binding.hint, binding.confirmButton)
        binding.input.addTextChangedListener(phoneWatcher)
        binding.input.append("+7")
        binding.input.requestFocus()
        setupListeners()
    }


    private fun setupListeners() {
        binding.confirmButton.setOnClickListener {
            val action = LoginFragmentDirections
                .actionLoginFragmentToLoginSmsFragment(viewModel.parsePhone(binding.hint.text.toString()))
            findNavController().navigate(action)
        }
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (binding.confirmButton.isEnabled) {
                    val action = LoginFragmentDirections
                        .actionLoginFragmentToLoginSmsFragment(viewModel.parsePhone(binding.hint.text.toString()))
                    findNavController().navigate(action)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}
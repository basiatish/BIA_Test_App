package com.basiatish.biatestapp.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.basiatish.biatestapp.databinding.LoginSmsFragmentBinding
import androidx.navigation.fragment.navArgs
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.ui.MainActivity
import com.basiatish.biatestapp.utils.CodeTextWatcher
import javax.inject.Inject

class LoginSmsFragment : Fragment() {

    private var _binding: LoginSmsFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var viewModel: LoginViewModel

    private val navArgs: LoginSmsFragmentArgs by navArgs()
    private var countDownTimer: CountDownTimer? = null
    private var counterFlag = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.loginComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginSmsFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val codeTextWatcher = CodeTextWatcher(binding.input, binding.confirmButton)
        binding.input.addTextChangedListener(codeTextWatcher)
        binding.input.requestFocus()
        phoneTextSetUp()
        startCountDown()
        setupListeners()
        setupObservers()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    private fun setupListeners() {
        binding.confirmButton.setOnClickListener {
            println("kek")
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (binding.confirmButton.isEnabled) {
                    println("kek")

                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.backBtn.setOnClickListener {
            NavigationUI.navigateUp(findNavController(), null)
        }
    }

    private fun setupObservers() {

    }

    private fun phoneTextSetUp() {
        val phone = navArgs.phone
//        val screenWidth = requireContext().resources.configuration
//        if (binding.titleHelper.text.length + phone.length > screenWidth) {
//            binding.titleHelper.append("\n$phone")
//        } else {
            binding.titleHelper.append(phone)
        //}
    }

    private fun timerTextSetUp() {
        val grayColor = resources.getColor(R.color.middle_gray_blue, context?.theme)
        val redColor = resources.getColor(R.color.red, context?.theme)
        if (counterFlag) {
            binding.counter.paintFlags = 0
            binding.counter.text = resources.getString(R.string.resend_code_timer)
            binding.counter.setTextColor(grayColor)
            binding.counter.isClickable = false
        } else {
            binding.counter.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.counter.text = resources.getString(R.string.resend_code)
            binding.counter.setTextColor(redColor)
            binding.counter.isClickable = true
            binding.counter.setOnClickListener {
                viewModel.sendCode(navArgs.phone)
                startCountDown()
            }
        }
    }



    private fun startCountDown() {
        counterFlag = true
        timerTextSetUp()
        countDownTimer = object: CountDownTimer(60000,1000){
            override fun onFinish() {
                counterFlag = false
                timerTextSetUp()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 < 10) {
                    binding.counter.text = resources
                        .getString(R.string.resend_code_timer, "0${millisUntilFinished / 1000}")
                } else {
                    binding.counter.text = resources
                        .getString(R.string.resend_code_timer, "${millisUntilFinished / 1000}")
                }

            }

        }
        countDownTimer?.start()
    }
}
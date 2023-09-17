package com.basiatish.biatestapp.ui.login

import androidx.lifecycle.ViewModel
import com.basiatish.biatestapp.di.FragmentScope
import javax.inject.Inject

@FragmentScope
class LoginViewModel @Inject constructor() : ViewModel() {

    fun parsePhone(phone: String): String {
        return phone
            .replace("(" , "")
            .replace(")", "")
    }

    fun sendCode(phone: String) {

    }

    fun checkCode(code: String) {

    }

}
package com.basiatish.biatestapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Profile
import com.basiatish.domain.usecases.LoadProfileUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val loadProfileUseCase: LoadProfileUseCase
) : ViewModel() {

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    fun loadProfile() {
        viewModelScope.launch {
            when (val response = loadProfileUseCase.invoke()) {
                is Result.Success -> {
                    _profile.value = response.resultData
                }
                is Result.Error -> {
                    Log.e("Profile", response.exception.message.toString())
                }
            }
        }
    }

}
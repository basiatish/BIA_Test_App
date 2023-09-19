package com.basiatish.biatestapp.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.usecases.GetRemoteTasksUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class TasksListViewModel @Inject constructor(
    private val getRemoteTasksUseCase: GetRemoteTasksUseCase
): ViewModel() {


    private val _taskList = MutableLiveData<List<TaskList>?>()
    val taskList: LiveData<List<TaskList>?> = _taskList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getTaskList() {
        viewModelScope.launch {
            _taskList.value = listOf()
            when (val response = getRemoteTasksUseCase.invoke()) {
                is Result.Success -> {
                    _taskList.value = response.resultData
                }
                is Result.Error -> {
                    _error.postValue(response.exception.message)
                }
            }
        }
    }
}
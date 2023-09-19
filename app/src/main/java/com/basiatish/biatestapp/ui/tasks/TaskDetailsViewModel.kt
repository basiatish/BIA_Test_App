package com.basiatish.biatestapp.ui.tasks

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.workers.UploadWorker
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Incident
import com.basiatish.domain.entities.Task
import com.basiatish.domain.usecases.GetTaskIncidentUseCase
import com.basiatish.domain.usecases.GetTaskUseCase
import com.basiatish.domain.usecases.UpdateTaskStatusUseCase
import com.basiatish.domain.usecases.UploadTaskIncidentUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScope
class TaskDetailsViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val uploadTaskIncidentUseCase: UploadTaskIncidentUseCase,
    private val getTaskIncidentUseCase: GetTaskIncidentUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> = _task

    private val _incident = MutableLiveData<Incident?>()
    val incident: LiveData<Incident?> = _incident

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val documentsList = mutableListOf<String>()
    private val _documents = MutableLiveData<List<String>>()
    val documents: LiveData<List<String>> = _documents
    private val filesList = mutableMapOf<String, String>()

    fun getTask(taskID: Int) {
        viewModelScope.launch {
            _task.value = null
            when (val response = getTaskUseCase.invoke(taskID)) {
                is Result.Success -> {
                    _task.value = response.resultData
                }
                is Result.Error -> {
                    _error.postValue(response.exception.message)
                }
            }
        }
    }

    fun saveIncident(taskID: Int, text: String) {
        viewModelScope.launch {
            when (val upload = uploadTaskIncidentUseCase.invoke(taskID, text)) {
                is Result.Success -> {
                    Log.i("Upload", upload.resultData.text)
                }
                is Result.Error -> {
                    Log.e("Upload", upload.exception.message.toString())
                }
            }
        }
    }

    fun getIncident(taskID: Int) {
        viewModelScope.launch {
            when (val response = getTaskIncidentUseCase.invoke(taskID)) {
                is Result.Success -> {
                    _incident.value = response.resultData
                }
                is Result.Error -> {
                    Log.e("getIncident", response.exception.message.toString())
                }
            }
        }
    }

    fun updateTaskStatus(taskID: Int, status: String) {
        viewModelScope.launch {
            when (val upload = updateTaskStatusUseCase.invoke(taskID, status)) {
                is Result.Success -> {
                    Log.i("Upload", upload.resultData.status)
                }
                is Result.Error -> {
                    Log.e("Upload", upload.exception.message.toString())
                }
            }
        }
    }

    fun addDocument(fileName: String?, data: Uri?) {
        viewModelScope.launch {
            if (fileName != null && data != null) {
                documentsList.add(fileName)
                _documents.value = documentsList.toList()
                filesList[fileName] = data.toString()
            }
        }
    }

    fun removeDocument(name: String) {
        try {
            viewModelScope.launch {
                documentsList.remove(name)
                _documents.value = documentsList.toList()
                filesList.remove(name)
            }
        } catch (e: Exception) {
            Log.e("removeFile", e.message.toString())
        }
    }

    fun upload(owner: LifecycleOwner, name: String, taskID: Int) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val data = Data.Builder()
            .putString("user", name)
            .putString("taskID", taskID.toString())
            .putStringArray("files", filesList.values.toTypedArray())
            .build()
        val request = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(owner) {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                _status.value = "Success"
            }
        }
    }
}
package com.basiatish.datamodule.repositories.tasksrepository

import com.basiatish.datamodule.api.BiaApi
import com.basiatish.datamodule.mappers.IncidentMapper
import com.basiatish.datamodule.mappers.TaskMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Incident
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.entities.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TaskRemoteSourceImpl(
    private val service: BiaApi,
    private val taskMapper: TaskMapper,
    private val incidentMapper: IncidentMapper
) : TaskRemoteSource {

    override suspend fun getTasks(): Result<List<TaskList>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getTaskList()
                if (response.isSuccessful) {
                    return@withContext Result.Success(taskMapper.toTaskList(response.body()!!))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun getTask(id: Int): Result<Task> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getTask(id)
                if (response.isSuccessful) {
                    return@withContext Result.Success(taskMapper.toTask(response.body()!!))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun uploadTaskIncident(taskID: Int, text: String): Result<Incident> =
        withContext(Dispatchers.IO) {
            try {
                val body = incidentMapper.toUploadIncident(taskID, text)
                val upload = service.uploadTaskIncident(taskID, body)
                if (upload.isSuccessful) {
                    return@withContext Result.Success(incidentMapper.toIncident(upload.body()!!))
                } else {
                    return@withContext Result.Error(Exception(upload.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun getTaskIncident(taskID: Int): Result<Incident> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getTaskIncident(taskID)
                if (response.isSuccessful) {
                    return@withContext Result.Success(incidentMapper.toIncident(response.body()!!))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun updateTaskStatus(taskID: Int, status: String): Result<TaskStatus> =
        withContext(Dispatchers.IO) {
            try {
                val upload = service.updateTaskStatus(taskID, taskMapper.toTaskStatusRemote(status))
                if (upload.isSuccessful) {
                    return@withContext Result.Success(taskMapper.toTaskStatus(upload.body()!!))
                } else {
                    return@withContext Result.Error(Exception(upload.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
}
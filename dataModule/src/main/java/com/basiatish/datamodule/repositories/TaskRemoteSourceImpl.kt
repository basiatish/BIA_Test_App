package com.basiatish.datamodule.repositories

import com.basiatish.datamodule.api.BiaApi
import com.basiatish.datamodule.mappers.TaskMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TaskRemoteSourceImpl(
    private val service: BiaApi,
    private val mapper: TaskMapper
) : TaskRemoteSource {

    override suspend fun getTasks(): Result<List<TaskList>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getTaskList()
                if (response.isSuccessful) {
                    return@withContext Result.Success(mapper.toTaskList(response.body()!!))
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
                    return@withContext Result.Success(mapper.toTask(response.body()!!))
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
}
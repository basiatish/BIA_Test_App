package com.basiatish.datamodule.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Incident
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.entities.TaskStatus
import com.basiatish.domain.repositories.TasksRepository

class TaskRepositoryImpl(private val remoteSource: TaskRemoteSource) : TasksRepository {
    override suspend fun getRemoteTasks(): Result<List<TaskList>> {
        return remoteSource.getTasks()
    }

    override suspend fun getTask(id: Int): Result<Task> {
        return remoteSource.getTask(id)
    }

    override suspend fun uploadTaskIncident(taskID: Int, text: String): Result<Incident> {
        return remoteSource.uploadTaskIncident(taskID, text)
    }

    override suspend fun getTaskIncident(taskID: Int): Result<Incident> {
        return remoteSource.getTaskIncident(taskID)
    }

    override suspend fun updateTaskStatus(taskID: Int, status: String): Result<TaskStatus> {
        return remoteSource.updateTaskStatus(taskID, status)
    }
}
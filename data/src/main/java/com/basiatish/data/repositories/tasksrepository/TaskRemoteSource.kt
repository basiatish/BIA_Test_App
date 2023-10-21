package com.basiatish.data.repositories.tasksrepository

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Incident
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.entities.TaskStatus

interface TaskRemoteSource {

    suspend fun getTasks(): Result<List<TaskList>>

    suspend fun getTask(id: Int): Result<Task>

    suspend fun uploadTaskIncident(taskID: Int, text: String): Result<Incident>

    suspend fun getTaskIncident(taskID: Int): Result<Incident>

    suspend fun updateTaskStatus(taskID: Int, status: String): Result<TaskStatus>

}
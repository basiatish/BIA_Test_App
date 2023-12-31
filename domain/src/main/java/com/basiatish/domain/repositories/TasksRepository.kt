package com.basiatish.domain.repositories

import com.basiatish.domain.entities.Task
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Incident
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.entities.TaskStatus

interface TasksRepository {

    suspend fun getRemoteTasks(): Result<List<TaskList>>

    suspend fun getTask(id: Int): Result<Task>

    suspend fun uploadTaskIncident(taskID: Int, text: String): Result<Incident>

    suspend fun getTaskIncident(taskID: Int): Result<Incident>

    suspend fun updateTaskStatus(taskID: Int, status: String): Result<TaskStatus>

}
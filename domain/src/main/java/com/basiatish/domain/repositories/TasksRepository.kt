package com.basiatish.domain.repositories

import com.basiatish.domain.entities.Task
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.TaskList

interface TasksRepository {

    suspend fun getRemoteTasks(): Result<List<TaskList>>

    suspend fun getTask(id: Int): Result<Task>

}
package com.basiatish.datamodule.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList

interface TaskRemoteSource {

    suspend fun getTasks(): Result<List<TaskList>>

    suspend fun getTask(id: Int): Result<Task>

}
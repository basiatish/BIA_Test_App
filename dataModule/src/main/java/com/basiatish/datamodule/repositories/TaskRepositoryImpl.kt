package com.basiatish.datamodule.repositories

import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.Task
import com.basiatish.domain.entities.TaskList
import com.basiatish.domain.repositories.TasksRepository

class TaskRepositoryImpl(private val remoteSource: TaskRemoteSource) : TasksRepository {
    override suspend fun getRemoteTasks(): Result<List<TaskList>> {
        return remoteSource.getTasks()
    }

    override suspend fun getTask(id: Int): Result<Task> {
        return remoteSource.getTask(id)
    }
}
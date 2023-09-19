package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.TasksRepository

class UpdateTaskStatusUseCase(private val tasksRepository: TasksRepository) {

    suspend fun invoke(taskID: Int, status: String) = tasksRepository.updateTaskStatus(taskID, status)
}
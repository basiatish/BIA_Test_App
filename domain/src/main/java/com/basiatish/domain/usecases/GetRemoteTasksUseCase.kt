package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.TasksRepository

class GetRemoteTasksUseCase(private val tasksRepository: TasksRepository) {

    suspend fun invoke() = tasksRepository.getRemoteTasks()

}
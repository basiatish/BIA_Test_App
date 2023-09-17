package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.TasksRepository

class GetTaskUseCase(private val tasksRepository: TasksRepository) {

    suspend fun invoke(id: Int) = tasksRepository.getTask(id)

}
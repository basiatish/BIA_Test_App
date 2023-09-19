package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.TasksRepository

class GetTaskIncidentUseCase(private val tasksRepository: TasksRepository) {

    suspend fun invoke(taskID: Int) = tasksRepository.getTaskIncident(taskID)

}
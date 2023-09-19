package com.basiatish.domain.usecases

import com.basiatish.domain.repositories.TasksRepository

class UploadTaskIncidentUseCase(private val tasksRepository: TasksRepository) {

    suspend fun invoke(taskID: Int, text: String) = tasksRepository.uploadTaskIncident(taskID, text)

}
package com.basiatish.biatestapp.ui.tasks.di

import com.basiatish.data.repositories.tasksrepository.TaskRepositoryImpl
import com.basiatish.domain.usecases.GetRemoteTasksUseCase
import com.basiatish.domain.usecases.GetTaskIncidentUseCase
import com.basiatish.domain.usecases.GetTaskUseCase
import com.basiatish.domain.usecases.UpdateTaskStatusUseCase
import com.basiatish.domain.usecases.UploadTaskIncidentUseCase
import dagger.Module
import dagger.Provides

@Module
class TaskUseCaseModule {

    @Provides
    fun provideGetRemoteTasksUseCase(taskRepository: TaskRepositoryImpl): GetRemoteTasksUseCase {
        return GetRemoteTasksUseCase(taskRepository)
    }

    @Provides
    fun provideGetTaskUseCase(taskRepository: TaskRepositoryImpl): GetTaskUseCase {
        return GetTaskUseCase(taskRepository)
    }

    @Provides
    fun provideUploadTaskIncidentUseCase(taskRepository: TaskRepositoryImpl): UploadTaskIncidentUseCase {
        return UploadTaskIncidentUseCase(taskRepository)
    }

    @Provides
    fun provideGetTaskIncidentUseCase(taskRepository: TaskRepositoryImpl): GetTaskIncidentUseCase {
        return GetTaskIncidentUseCase(taskRepository)
    }

    @Provides
    fun provideUpdateTaskStatusUseCase(taskRepository: TaskRepositoryImpl): UpdateTaskStatusUseCase {
        return UpdateTaskStatusUseCase(taskRepository)
    }
}
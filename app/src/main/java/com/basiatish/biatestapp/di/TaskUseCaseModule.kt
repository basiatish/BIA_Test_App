package com.basiatish.biatestapp.di

import com.basiatish.datamodule.repositories.TaskRepositoryImpl
import com.basiatish.domain.usecases.GetRemoteTasksUseCase
import com.basiatish.domain.usecases.GetTaskUseCase
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

}
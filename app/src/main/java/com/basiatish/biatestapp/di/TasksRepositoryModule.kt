package com.basiatish.biatestapp.di

import com.basiatish.datamodule.api.NetworkModule
import com.basiatish.datamodule.mappers.TaskMapper
import com.basiatish.datamodule.repositories.TaskRemoteSourceImpl
import com.basiatish.datamodule.repositories.TaskRepositoryImpl
import com.basiatish.biatestapp.BuildConfig
import com.basiatish.datamodule.mappers.IncidentMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TasksRepositoryModule {

    private val tasksMapper by lazy {
        TaskMapper()
    }

    private val incidentMapper by lazy {
        IncidentMapper()
    }

    @Singleton
    private val networkModule by lazy {
        NetworkModule()
    }

    @Volatile
    @Singleton
    var tasksRepository: TaskRepositoryImpl? = null

    @Singleton
    @Provides
    fun provideTaskRepository(): TaskRepositoryImpl {
        return tasksRepository ?: createTaskRepository()
    }

    @Singleton
    fun createTaskRepository(): TaskRepositoryImpl {
        val repository = TaskRepositoryImpl(
            TaskRemoteSourceImpl(networkModule.createApi(BuildConfig.BASE_API), tasksMapper, incidentMapper)
        )
        tasksRepository = repository
        return repository
    }

}
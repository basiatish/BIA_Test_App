package com.basiatish.biatestapp.di

import com.basiatish.datamodule.api.NetworkModule
import com.basiatish.datamodule.mappers.TaskMapper
import com.basiatish.datamodule.repositories.TaskRemoteSourceImpl
import com.basiatish.datamodule.repositories.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class TasksRepositoryModule {

    private val tasksMapper by lazy {
        TaskMapper()
    }

    @Singleton
    private val networkModule by lazy {
        NetworkModule()
    }

    @Singleton
    @Volatile
    var tasksRepository: TaskRepositoryImpl? = null

    @Provides
    fun provideTaskRepository(): TaskRepositoryImpl {
        return tasksRepository ?: createTaskRepository()
    }

    private fun createTaskRepository(): TaskRepositoryImpl {
        val repository = TaskRepositoryImpl(
            TaskRemoteSourceImpl(networkModule.createApi(""), tasksMapper)
        )
        tasksRepository = repository
        return repository
    }

}
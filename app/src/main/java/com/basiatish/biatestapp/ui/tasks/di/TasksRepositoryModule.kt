package com.basiatish.biatestapp.ui.tasks.di

import com.basiatish.data.api.NetworkModule
import com.basiatish.data.mappers.TaskMapper
import com.basiatish.data.repositories.tasksrepository.TaskRemoteSourceImpl
import com.basiatish.data.repositories.tasksrepository.TaskRepositoryImpl
import com.basiatish.biatestapp.BuildConfig
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.data.mappers.IncidentMapper
import dagger.Module
import dagger.Provides

@Module
class TasksRepositoryModule {

    private val tasksMapper by lazy {
        TaskMapper()
    }

    private val incidentMapper by lazy {
        IncidentMapper()
    }

    @FragmentScope
    private val networkModule by lazy {
        NetworkModule()
    }

    @Volatile
    @FragmentScope
    var tasksRepository: TaskRepositoryImpl? = null

    @FragmentScope
    @Provides
    fun provideTaskRepository(): TaskRepositoryImpl {
        return tasksRepository ?: createTaskRepository()
    }

    @FragmentScope
    fun createTaskRepository(): TaskRepositoryImpl {
        val repository = TaskRepositoryImpl(
            TaskRemoteSourceImpl(networkModule.createApi(BuildConfig.BASE_API), tasksMapper, incidentMapper)
        )
        tasksRepository = repository
        return repository
    }

}
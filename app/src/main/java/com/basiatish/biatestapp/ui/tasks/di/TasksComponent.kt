package com.basiatish.biatestapp.ui.tasks.di

import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.ui.tasks.IncidentFragment
import com.basiatish.biatestapp.ui.tasks.TaskDetailsFragment
import com.basiatish.biatestapp.ui.tasks.TaskDocumentsFragment
import com.basiatish.biatestapp.ui.tasks.TasksListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WorkManagerModule::class, TasksRepositoryModule::class, TaskUseCaseModule::class])
interface TasksComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TasksComponent
    }

    fun inject(fragment: TasksListFragment)
    fun inject(fragment: TaskDetailsFragment)
    fun inject(fragment: IncidentFragment)
    fun inject(fragment: TaskDocumentsFragment)

}
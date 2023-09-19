package com.basiatish.biatestapp.ui.tasks

import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.di.WorkManagerModule
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WorkManagerModule::class])
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
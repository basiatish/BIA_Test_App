package com.basiatish.biatestapp.ui.tasks

import dagger.Subcomponent

@Subcomponent
interface TasksComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TasksComponent
    }

    fun inject(fragment: TasksListFragment)

}
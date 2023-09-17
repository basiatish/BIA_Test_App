package com.basiatish.biatestapp.di

import android.content.Context
import com.basiatish.biatestapp.ui.login.LoginComponent
import com.basiatish.biatestapp.ui.tasks.TasksComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponents::class, TaskUseCaseModule::class, TaskUseCaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun loginComponent(): LoginComponent.Factory
    fun tasksComponent(): TasksComponent.Factory

}
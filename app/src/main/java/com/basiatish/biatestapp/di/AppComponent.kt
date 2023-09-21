package com.basiatish.biatestapp.di

import android.content.Context
import com.basiatish.biatestapp.ui.calendar.di.CalendarComponent
import com.basiatish.biatestapp.ui.login.di.LoginComponent
import com.basiatish.biatestapp.ui.tasks.di.TasksComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun loginComponent(): LoginComponent.Factory
    fun tasksComponent(): TasksComponent.Factory
    fun calendarComponent(): CalendarComponent.Factory

}
package com.basiatish.biatestapp.di

import com.basiatish.biatestapp.ui.calendar.di.CalendarComponent
import com.basiatish.biatestapp.ui.login.di.LoginComponent
import com.basiatish.biatestapp.ui.tasks.di.TasksComponent
import dagger.Module

@Module(subcomponents = [LoginComponent::class, TasksComponent::class, CalendarComponent::class])
class AppSubComponents {
}
package com.basiatish.biatestapp.di

import com.basiatish.biatestapp.ui.login.LoginComponent
import com.basiatish.biatestapp.ui.tasks.TasksComponent
import dagger.Module

@Module(subcomponents = [LoginComponent::class, TasksComponent::class])
class AppSubComponents {
}
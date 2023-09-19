package com.basiatish.biatestapp.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Module
class WorkManagerModule {

    @Provides
    fun provideWorkManager(context: Context) = WorkManager.getInstance(context)

}
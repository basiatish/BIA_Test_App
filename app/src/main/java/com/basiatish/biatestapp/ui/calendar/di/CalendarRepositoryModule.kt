package com.basiatish.biatestapp.ui.calendar.di

import com.basiatish.biatestapp.BuildConfig
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.data.api.NetworkModule
import com.basiatish.data.mappers.CalendarMapper
import com.basiatish.data.repositories.calendarepository.CalendarRemoteSourceImpl
import com.basiatish.data.repositories.calendarepository.CalendarRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class CalendarRepositoryModule {

    private val calendarMapper by lazy {
        CalendarMapper()
    }

    @FragmentScope
    private val networkModule by lazy {
        NetworkModule()
    }

    @Volatile
    @FragmentScope
    var calendarRepository: CalendarRepositoryImpl? = null

    @FragmentScope
    @Provides
    fun provideCalendarRepository(): CalendarRepositoryImpl {
        return calendarRepository ?: createCalendarRepository()
    }

    @FragmentScope
    fun createCalendarRepository(): CalendarRepositoryImpl {
        val repository = CalendarRepositoryImpl(
            CalendarRemoteSourceImpl(networkModule.createApi(BuildConfig.BASE_API), calendarMapper)
        )
        calendarRepository = repository
        return repository
    }

}
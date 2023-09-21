package com.basiatish.biatestapp.ui.calendar.di

import com.basiatish.datamodule.repositories.calendarepository.CalendarRepositoryImpl
import com.basiatish.domain.usecases.GetCalendarUseCase
import com.basiatish.domain.usecases.SaveDayUseCase
import dagger.Module
import dagger.Provides

@Module
class CalendarUseCaseModule {

    @Provides
    fun provideGetCalendarUseCase(calendarRepository: CalendarRepositoryImpl): GetCalendarUseCase {
        return GetCalendarUseCase(calendarRepository)
    }

    @Provides
    fun provideSaveDayUseCase(calendarRepository: CalendarRepositoryImpl): SaveDayUseCase {
        return SaveDayUseCase(calendarRepository)
    }

}
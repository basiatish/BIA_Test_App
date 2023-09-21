package com.basiatish.biatestapp.ui.calendar.di

import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.ui.calendar.CalendarFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [CalendarRepositoryModule::class, CalendarUseCaseModule::class])
interface CalendarComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CalendarComponent
    }

    fun inject(fragment: CalendarFragment)

}
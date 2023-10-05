package com.basiatish.biatestapp.ui.profile.di

import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.ui.profile.AddSickFragment
import com.basiatish.biatestapp.ui.profile.ProfileFragment
import com.basiatish.biatestapp.ui.profile.SickListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ProfileRepositoryModule::class, ProfileUseCaseModule::class,
SickRepositoryModule::class, SickViewUseCaseModule::class])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(profileFragment: ProfileFragment)
    fun inject(sickListFragment: SickListFragment)
    fun inject(addSickFragment: AddSickFragment)

}
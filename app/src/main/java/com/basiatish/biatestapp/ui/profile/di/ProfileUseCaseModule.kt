package com.basiatish.biatestapp.ui.profile.di

import com.basiatish.datamodule.repositories.profilerepository.ProfileRepositoryImpl
import com.basiatish.domain.usecases.LoadProfileUseCase
import dagger.Module
import dagger.Provides

@Module
class ProfileUseCaseModule {

    @Provides
    fun getLoadProfileUseCase(profileRepository: ProfileRepositoryImpl): LoadProfileUseCase {
        return LoadProfileUseCase(profileRepository)
    }

}
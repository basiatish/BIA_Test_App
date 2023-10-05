package com.basiatish.biatestapp.ui.profile.di

import com.basiatish.biatestapp.BuildConfig
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.datamodule.api.NetworkModule
import com.basiatish.datamodule.mappers.ProfileMapper
import com.basiatish.datamodule.repositories.profilerepository.ProfileRemoteSourceImpl
import com.basiatish.datamodule.repositories.profilerepository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class ProfileRepositoryModule {

    private val mapper by lazy {
        ProfileMapper()
    }

    @FragmentScope
    private val networkModule by lazy {
        NetworkModule()
    }

    @Volatile
    @FragmentScope
    var profileRepository: ProfileRepositoryImpl? = null

    @FragmentScope
    @Provides
    fun provideProfileRepository(): ProfileRepositoryImpl {
        return profileRepository ?: createProfileRepository()
    }

    @FragmentScope
    fun createProfileRepository(): ProfileRepositoryImpl {
        val repository = ProfileRepositoryImpl(
            ProfileRemoteSourceImpl(networkModule.createApi(BuildConfig.BASE_API), mapper)
        )
        profileRepository = repository
        return repository
    }
}
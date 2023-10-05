package com.basiatish.biatestapp.ui.profile.di

import com.basiatish.biatestapp.BuildConfig
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.datamodule.api.NetworkModule
import com.basiatish.datamodule.mappers.SickMapper
import com.basiatish.datamodule.repositories.sicklistrepository.SickCalendarLocalSourceImpl
import com.basiatish.datamodule.repositories.sicklistrepository.SickListRemoteSourceImpl
import com.basiatish.datamodule.repositories.sicklistrepository.SickRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class SickRepositoryModule {

    private val mapper by lazy {
        SickMapper()
    }

    @FragmentScope
    private val networkModule by lazy {
        NetworkModule()
    }

    @Volatile
    @FragmentScope
    var sickRepository: SickRepositoryImpl? = null

    @FragmentScope
    @Provides
    fun provideSickRepository(): SickRepositoryImpl {
        return sickRepository ?: createProfileRepository()
    }

    @FragmentScope
    fun createProfileRepository(): SickRepositoryImpl {
        val repository = SickRepositoryImpl(
            SickCalendarLocalSourceImpl(mapper),
            SickListRemoteSourceImpl(networkModule.createApi(BuildConfig.BASE_API), mapper)
        )
        sickRepository = repository
        return repository
    }
}
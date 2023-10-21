package com.basiatish.biatestapp.ui.profile.di

import com.basiatish.data.repositories.sicklistrepository.SickRepositoryImpl
import com.basiatish.domain.usecases.CloseSickListUseCase
import com.basiatish.domain.usecases.GetSickCalendarUseCase
import com.basiatish.domain.usecases.GetSickListUseCase
import com.basiatish.domain.usecases.SaveRangeUseCase
import dagger.Module
import dagger.Provides

@Module
class SickViewUseCaseModule {

    @Provides
    fun getCalendarViewUseCase(sickRepository: SickRepositoryImpl): GetSickCalendarUseCase {
        return GetSickCalendarUseCase(sickRepository)
    }

    @Provides
    fun saveRangeUseCase(sickRepository: SickRepositoryImpl): SaveRangeUseCase {
        return SaveRangeUseCase(sickRepository)
    }

    @Provides
    fun getSickListUseCase(sickRepository: SickRepositoryImpl): GetSickListUseCase {
        return GetSickListUseCase(sickRepository)
    }

    @Provides
    fun closeSickListUseCase(sickRepository: SickRepositoryImpl): CloseSickListUseCase {
        return CloseSickListUseCase(sickRepository)
    }

}
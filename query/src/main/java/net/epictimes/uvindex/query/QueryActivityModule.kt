package net.epictimes.uvindex.query

import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.interactor.CurrentInteractor
import net.epictimes.uvindex.data.interactor.NetworkCurrentInteractor

@Module
class QueryActivityModule {

    @Provides
    fun provideCurrentInteractor(services: Services): CurrentInteractor {
        return NetworkCurrentInteractor(services)
    }

    @Provides
    fun provideQueryPresenter(currentInteractor: CurrentInteractor): QueryPresenter {
        return QueryPresenter(currentInteractor)
    }

    @Provides
    fun provideQueryViewState(): QueryViewState {
        return QueryViewState()
    }

}

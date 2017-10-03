package net.epictimes.uvindex.query

import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.interactor.CurrentInteractor
import net.epictimes.uvindex.data.interactor.NetworkCurrentInteractor
import net.epictimes.uvindex.di.ActivityScoped

@Module
class QueryActivityModule {

    @ActivityScoped
    @Provides
    fun provideCurrentInteractor(services: Services): CurrentInteractor =
            NetworkCurrentInteractor(services)

    @ActivityScoped
    @Provides
    fun provideQueryPresenter(currentInteractor: CurrentInteractor): QueryPresenter =
            QueryPresenter(currentInteractor)

    @ActivityScoped
    @Provides
    fun provideQueryViewState(): QueryViewState = QueryViewState()

}

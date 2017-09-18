package net.epictimes.uvindex.query

import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.data.Services

@Module
class QueryActivityModule {

    @Provides
    fun provideQueryPresenter(services: Services): QueryPresenter {
        return QueryPresenter(services)
    }

    @Provides
    fun provideQueryViewState(): QueryViewState {
        return QueryViewState()
    }

}

package net.epictimes.uvindex.query

import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.interactor.NetworkWeatherInteractor
import net.epictimes.uvindex.data.interactor.WeatherInteractor

@Module
class QueryActivityModule {

    @QueryFeatureScoped
    @Provides
    fun provideCurrentInteractor(services: Services): WeatherInteractor = NetworkWeatherInteractor(services)

    @QueryFeatureScoped
    @Provides
    fun provideQueryViewState(): QueryViewState = QueryViewState()

}

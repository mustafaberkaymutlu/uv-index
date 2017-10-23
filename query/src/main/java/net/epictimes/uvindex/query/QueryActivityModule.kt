package net.epictimes.uvindex.query

import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.interactor.NetworkWeatherInteractor
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.di.ActivityScoped
import java.util.*

@Module
class QueryActivityModule {

    @ActivityScoped
    @Provides
    fun provideCurrentInteractor(services: Services): WeatherInteractor = NetworkWeatherInteractor(services)

    @ActivityScoped
    @Provides
    fun provideQueryPresenter(weatherInteractor: WeatherInteractor): QueryPresenter =
            QueryPresenter(weatherInteractor, Date())

    @ActivityScoped
    @Provides
    fun provideQueryViewState(): QueryViewState = QueryViewState()

}

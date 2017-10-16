package net.epictimes.uvindex.query

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.Weather
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*


class QueryPresenterTest {

    private lateinit var queryPresenter: QueryPresenter

    @Mock
    private lateinit var weatherInteractor: WeatherInteractor

    @Mock
    private lateinit var queryView: QueryView

    @Before
    fun setupMocksAndView() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenEmptyWeatherList_shouldDisplayError() {
        queryPresenter = QueryPresenter(weatherInteractor)

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(emptyList())
        }

        verify(queryView, times(1)).displayGetUvIndexError()
    }

    @Test
    fun givenError_shouldDisplayError() {
        queryPresenter = QueryPresenter(weatherInteractor)

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onFailGetForecast()
        }

        verify(queryView, times(1)).displayGetUvIndexError()
    }

    @Test
    fun givenValidWeatherForecast_shouldDisplayUvIndex() {
        val weather = mock<Weather>()
        val weatherForecast = Array(24) { weather }.asList()

        whenever(weather.datetime).thenReturn(Date())

        queryPresenter = QueryPresenter(weatherInteractor)

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(weatherForecast)
        }

        verify(queryView, times(1)).displayUvIndex(weatherForecast.first())
        verify(queryView, times(1)).displayUvIndexForecast(weatherForecast)
    }


}
package net.epictimes.uvindex.query

import com.nhaarman.mockito_kotlin.*
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.Weather
import org.junit.Assert.assertEquals
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
        queryPresenter = QueryPresenter(weatherInteractor, Date())

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(emptyList(), "Europe/Istanbul")
        }

        verify(queryView, times(1)).displayGetUvIndexError()
    }

    @Test
    fun givenError_shouldDisplayError() {
        queryPresenter = QueryPresenter(weatherInteractor, Date())

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
    fun givenWeatherForecast_shouldDisplayUvIndex() {
        val weather = mock<Weather>()
        val weatherForecast = Array(24) { weather }.asList()

        whenever(weather.datetime).thenReturn(Date())

        queryPresenter = QueryPresenter(weatherInteractor, Date())

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(weatherForecast, "Europe/Istanbul")
        }

        val currentUvIndex = weatherForecast.first()
        verify(queryView, times(1)).setToViewState(currentUvIndex, weatherForecast, "Europe/Istanbul")
        verify(queryView, times(1)).displayUvIndex(currentUvIndex)
        verify(queryView, times(1)).displayUvIndexForecast(weatherForecast)
    }

    @Test
    fun givenWeatherForecast_shouldGetClosestDateCorrectly() {
        val currentMillis = 2049L
        val closestMillis = 2000L

        val weatherForecast = (0..24).map({
            val mock = mock<Weather>()
            whenever(mock.datetime).thenReturn(Date(it * 100L))
            mock
        }).toList()

        queryPresenter = QueryPresenter(weatherInteractor, Date(currentMillis))

        queryPresenter.attachView(queryView)

        queryPresenter.getForecastUvIndex(0.0, 0.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor, times(1)).getForecast(any(), any(),
                    anyOrNull(), anyOrNull(), anyOrNull(), capture())
            firstValue.onSuccessGetForecast(weatherForecast, "Europe/Istanbul")
        }

        verify(queryView, times(1)).displayUvIndex(check {
            assertEquals(closestMillis, it.datetime.time)
        })
    }

}
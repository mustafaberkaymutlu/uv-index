package net.epictimes.uvindex.query

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.Date

class QueryPresenterTest {

    private val weatherInteractor: WeatherInteractor = mock()

    private val dateProvider: DateProvider = mock()

    private val queryView: QueryView = mock()

    private val queryPresenter = QueryPresenter(weatherInteractor, dateProvider)

    @BeforeEach
    fun setupMocksAndView() {
        queryPresenter.attachView(queryView)
    }

    @Test
    fun givenEmptyWeatherList_shouldDisplayError() {
        whenever(dateProvider.now()).thenReturn(Date())

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(emptyList(), "Europe/Istanbul", "Istanbul", "TR")
        }

        verify(queryView, times(1)).displayGetUvIndexError()
    }

    @Test
    fun givenError_shouldDisplayError() {
        whenever(dateProvider.now()).thenReturn(Date())

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
        whenever(dateProvider.now()).thenReturn(Date())

        queryPresenter.getForecastUvIndex(1.0, 2.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor).getForecast(eq(1.0), eq(2.0), eq(null), eq(null), eq(24),
                    capture())
            firstValue.onSuccessGetForecast(weatherForecast, "Europe/Istanbul", "Istanbul", "TR")
        }

        val currentUvIndex = weatherForecast.first()
        verify(queryView, times(1)).setToViewState(currentUvIndex, weatherForecast, "Europe/Istanbul", "Istanbul, TR")
        verify(queryView, times(1)).displayUvIndex(currentUvIndex)
        verify(queryView, times(1)).displayUvIndexForecast(weatherForecast)
    }

    @Test
    fun givenWeatherForecast_shouldGetClosestDateCorrectly() {
        val currentMillis = 2049L
        val closestMillis = 2000L

        val weatherForecast = (0..24).map {
            val mock = mock<Weather>()
            whenever(mock.datetime).thenReturn(Date(it * 100L))
            mock
        }.toList()

        whenever(dateProvider.now()).thenReturn(Date(currentMillis))

        queryPresenter.getForecastUvIndex(0.0, 0.0, null, null)

        argumentCaptor<WeatherInteractor.GetForecastCallback>().apply {
            verify(weatherInteractor, times(1)).getForecast(any(), any(),
                    anyOrNull(), anyOrNull(), anyOrNull(), capture())
            firstValue.onSuccessGetForecast(weatherForecast, "Europe/Istanbul", "Istanbul", "TR")
        }

        verify(queryView, times(1)).displayUvIndex(check {
            assertEquals(closestMillis, it.datetime.time)
        })
    }

}
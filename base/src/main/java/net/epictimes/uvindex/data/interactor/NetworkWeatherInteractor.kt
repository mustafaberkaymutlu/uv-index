package net.epictimes.uvindex.data.interactor

import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.response.GetForecastResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkWeatherInteractor constructor(private val services: Services) : WeatherInteractor {

    override fun getForecast(latitude: Double, longitude: Double, language: String?, units: String?, hours: Int?,
                             getForecastCallback: WeatherInteractor.GetForecastCallback) {

        services.getForecastByLatLon(latitude, longitude, language, units, hours)
                .enqueue(object : Callback<GetForecastResponse> {
                    override fun onResponse(call: Call<GetForecastResponse>?,
                                            response: Response<GetForecastResponse>?) {
                        val body = response?.body()
                        body?.weatherList?.let {
                            getForecastCallback.onSuccessGetForecast(it, body.timezone)
                        } ?: run {
                            getForecastCallback.onFailGetForecast()
                        }
                    }

                    override fun onFailure(call: Call<GetForecastResponse>?, t: Throwable?) =
                            getForecastCallback.onFailGetForecast()
                })

    }
}
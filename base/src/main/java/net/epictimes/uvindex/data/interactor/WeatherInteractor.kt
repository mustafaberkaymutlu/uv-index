package net.epictimes.uvindex.data.interactor

import net.epictimes.uvindex.data.model.Weather

interface WeatherInteractor {

    fun getForecast(latitude: Double, longitude: Double, language: String?, units: String?, hours: Int?,
                    getForecastCallback: GetForecastCallback)

    interface GetForecastCallback {

        fun onSuccessGetForecast(weatherForecast: List<Weather>, timezone: String, cityName: String, countryCode: String)

        fun onFailGetForecast()

    }

}
package net.epictimes.uvindex.query

import com.hannesdorfmann.mosby3.mvp.MvpView
import net.epictimes.uvindex.data.model.Weather

interface QueryView : MvpView {

    fun displayUvIndex(weather: Weather)

    fun displayUvIndexForecast(uvIndexForecast: List<Weather>)

    fun setToViewState(currentUvIndex: Weather, uvIndexForecast: List<Weather>, timezone: String, address: String)

    fun displayInstallPrompt(requestCode: Int, referrerCode: String)

    fun displayAboutUi()

    fun stopLocationUpdates(newState: QueryViewState.LocationSearchState)

    fun displayGetUvIndexError()

    fun displayGetAutoCompletePlaceError()

    fun displayUserAddress(address: String)

    fun displayCantDetectLocationError()

    fun startPlacesAutoCompleteUi()
}

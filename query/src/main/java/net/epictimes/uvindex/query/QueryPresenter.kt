package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.data.model.Weather
import java.util.*


class QueryPresenter constructor(private val weatherInteractor: WeatherInteractor, private val now: Date) : MvpBasePresenter<QueryView>() {

    companion object {
        val FORECAST_HOUR = 24
    }

    fun getForecastUvIndex(latitude: Double, longitude: Double, language: String?, units: String?) {
        weatherInteractor.getForecast(latitude, longitude, language, units, FORECAST_HOUR,
                object : WeatherInteractor.GetForecastCallback {
                    override fun onSuccessGetForecast(weatherForecast: List<Weather>, timezone: String) {
                        if (isViewAttached) {
                            if (weatherForecast.isEmpty()) {
                                view.displayGetUvIndexError()
                            } else {
                                val sortedForecast = weatherForecast.sortedBy { it.datetime.time }
                                val currentUvIndex = getClosestWeather(weatherForecast)

                                view.setToViewState(currentUvIndex, sortedForecast, timezone)
                                view.displayUvIndex(currentUvIndex)
                                view.displayUvIndexForecast(sortedForecast)
                            }
                        }
                    }

                    override fun onFailGetForecast() {
                        if (isViewAttached) {
                            view.displayGetUvIndexError()
                        }
                    }
                })
    }

    fun onLocationReceived(latLng: LatLng) {
        view.stopLocationUpdates(QueryViewState.LocationSearchState.Idle)
        view.startFetchingAddress(latLng)
        getForecastUvIndex(latLng.latitude, latLng.longitude, null, null)
    }

    fun userClickedInstallButton() =
            view.displayInstallPrompt(Constants.RequestCodes.INSTALL_FROM_QUERY_FEATURE,
                    Constants.ReferrerCodes.FROM_QUERY_FEATURE)

    fun userClickedAboutButton() = view.displayAboutUi()

    fun userClickedTextInputButton() = view.startPlacesAutoCompleteUi()

    fun userDidNotWantToChangeLocationSettings() = view.displayCantDetectLocationError()

    fun userAddressReceived(addressFetchResult: AddressFetchResult, result: String) {
        if (addressFetchResult == AddressFetchResult.SUCCESS) view.displayUserAddress(result)
        else view.displayUserAddressFetchError(result)
    }

    fun getPlaceAutoCompleteFailed() = view.displayGetAutoCompletePlaceError()

    private fun getClosestWeather(weatherList: Collection<Weather>): Weather =
            Collections.min(weatherList, { w1, w2 ->
                val diff1 = Math.abs(w1.datetime.time - now.time)
                val diff2 = Math.abs(w2.datetime.time - now.time)
                diff1.compareTo(diff2)
            })

}
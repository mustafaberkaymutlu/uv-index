package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.data.model.Weather
import net.epictimes.uvindex.service.AddressFetchResult
import java.util.*


class QueryPresenter constructor(private val weatherInteractor: WeatherInteractor, private val now: Date) : MvpBasePresenter<QueryView>() {

    companion object {
        val FORECAST_HOUR = 24
    }

    fun getForecastUvIndex(latitude: Double, longitude: Double, language: String?, units: String?) {
        weatherInteractor.getForecast(latitude, longitude, language, units, FORECAST_HOUR,
                object : WeatherInteractor.GetForecastCallback {
                    override fun onSuccessGetForecast(weatherForecast: List<Weather>, timezone: String) {
                        if (weatherForecast.isEmpty()) {
                            ifViewAttached { it.displayGetUvIndexError() }
                        } else {
                            val sortedForecast = weatherForecast.sortedBy { it.datetime.time }
                            val currentUvIndex = getClosestWeather(weatherForecast)

                            ifViewAttached {
                                it.setToViewState(currentUvIndex, sortedForecast, timezone)
                                it.displayUvIndex(currentUvIndex)
                                it.displayUvIndexForecast(sortedForecast)
                            }
                        }
                    }

                    override fun onFailGetForecast() {
                        ifViewAttached { it.displayGetUvIndexError() }
                    }
                })
    }

    fun onLocationReceived(latLng: LatLng) {
        ifViewAttached {
            it.stopLocationUpdates(QueryViewState.LocationSearchState.Idle)
            it.startFetchingAddress(latLng, 1)
        }

        getForecastUvIndex(latLng.latitude, latLng.longitude, null, null)
    }

    fun userClickedInstallButton() = ifViewAttached {
        it.displayInstallPrompt(Constants.RequestCodes.INSTALL_FROM_QUERY_FEATURE,
                Constants.ReferrerCodes.FROM_QUERY_FEATURE)
    }

    fun userClickedAboutButton() = ifViewAttached { it.displayAboutUi() }

    fun userClickedTextInputButton() = ifViewAttached { it.startPlacesAutoCompleteUi() }

    fun userDidNotWantToChangeLocationSettings() = ifViewAttached { it.displayCantDetectLocationError() }

    fun userAddressReceived(addressFetchResult: AddressFetchResult, result: String) {
        ifViewAttached {
            if (addressFetchResult == AddressFetchResult.SUCCESS) it.displayUserAddress(result)
            else it.displayUserAddressFetchError(result)
        }
    }

    fun userAddressFetchFailed(errorMessage: String) {
        ifViewAttached { it.displayUserAddressFetchError(errorMessage) }
    }

    fun getPlaceAutoCompleteFailed() = ifViewAttached { it.displayGetAutoCompletePlaceError() }

    private fun getClosestWeather(weatherList: Collection<Weather>): Weather =
            Collections.min(weatherList, { w1, w2 ->
                val diff1 = Math.abs(w1.datetime.time - now.time)
                val diff2 = Math.abs(w2.datetime.time - now.time)
                diff1.compareTo(diff2)
            })

}
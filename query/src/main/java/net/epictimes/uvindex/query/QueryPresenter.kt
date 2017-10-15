package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.interactor.WeatherInteractor
import net.epictimes.uvindex.data.model.Weather

class QueryPresenter constructor(private val weatherInteractor: WeatherInteractor) : MvpBasePresenter<QueryView>() {

    companion object {
        val FORECAST_HOUR = 24
    }

    fun getForecastUvIndex(latitude: Double, longitude: Double, language: String?, units: String?) {
        weatherInteractor.getForecast(latitude, longitude, language, units, FORECAST_HOUR,
                object : WeatherInteractor.GetForecastCallback {
                    override fun onSuccessGetForecast(weatherForecast: List<Weather>) {
                        if (isViewAttached) {
                            if (weatherForecast.isEmpty()) {
                                view.displayGetUvIndexError()
                            } else {
                                val sortedForecast = weatherForecast.sortedBy { it.datetime.time }
                                view.displayUvIndex(sortedForecast.first())
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

    fun userClickedInstallButton() {
        view.displayInstallPrompt(Constants.RequestCodes.INSTALL_FROM_QUERY_FEATURE,
                Constants.ReferrerCodes.FROM_QUERY_FEATURE)
    }

    fun userClickedAboutButton() {
        view.displayAboutUi()
    }

    fun userClickedTextInputButton() {
        view.startPlacesAutoCompleteUi(Constants.RequestCodes.PLACE_AUTO_COMPLETE)
    }

    fun userAddressReceived(resultCode: Int, result: String) {
        if (resultCode == FetchAddressIntentService.RESULT_SUCCESS) view.displayUserAddress(result)
        else view.displayUserAddressFetchError(result)
    }

    fun getPlaceAutoCompleteFailed() {
        view.displayGetAutoCompletePlaceError()
    }

}
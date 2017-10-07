package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.interactor.CurrentInteractor
import net.epictimes.uvindex.data.model.Weather

class QueryPresenter constructor(private val currentInteractor: CurrentInteractor) : MvpBasePresenter<QueryView>() {

    fun getUvIndex(latitude: Double, longitude: Double, language: String?, units: String?) {
        currentInteractor.getCurrent(latitude, longitude, language, units,
                object : CurrentInteractor.GetCurrentCallback {
                    override fun onSuccessGetCurrent(weather: Weather) {
                        if (isViewAttached) {
                            weather.uvIndex?.let {
                                view.displayUvIndex(it)
                            } ?: run {
                                view.displayGetUvIndexError()
                            }
                        }
                    }

                    override fun onFailGetCurrent() {
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
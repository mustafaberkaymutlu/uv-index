package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.data.interactor.CurrentInteractor
import net.epictimes.uvindex.data.model.Weather

class QueryPresenter constructor(private val currentInteractor: CurrentInteractor) : MvpBasePresenter<QueryView>(), CurrentInteractor.OnFinishedListener {

    fun getUvIndex(latitude: Double, longitude: Double, language: String?, units: String?) {
        currentInteractor.getCurrent(latitude, longitude, language, units, this)
    }

    fun getPlaceAutoCompleteFailed() {
        view.displayGetAutoCompletePlaceError()
    }

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

}
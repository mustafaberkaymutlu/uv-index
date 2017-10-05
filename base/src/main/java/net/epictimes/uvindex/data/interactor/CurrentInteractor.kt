package net.epictimes.uvindex.data.interactor

import net.epictimes.uvindex.data.model.Weather

interface CurrentInteractor {

    fun getCurrent(latitude: Double, longitude: Double, language: String?, units: String?,
                   getCurrentCallback: GetCurrentCallback)

    interface GetCurrentCallback {

        fun onSuccessGetCurrent(weather: Weather)

        fun onFailGetCurrent()

    }

}
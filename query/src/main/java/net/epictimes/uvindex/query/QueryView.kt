package net.epictimes.uvindex.query

import com.hannesdorfmann.mosby3.mvp.MvpView

interface QueryView : MvpView {

    fun displayUvIndex(uvIndex: Int)

    fun displayInstallPrompt(requestCode: Int, referrerCode: String)

    fun displayAboutUi()

    fun displayGetUvIndexError()

    fun displayGetAutoCompletePlaceError()

    fun displayUserAddress(address: String)

    fun displayUserAddressFetchError(errorMessage: String)
    fun startPlacesAutoCompleteUi(requestCode: Int)
}

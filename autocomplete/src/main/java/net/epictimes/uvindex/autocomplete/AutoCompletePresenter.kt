package net.epictimes.uvindex.autocomplete

import android.location.Address
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class AutoCompletePresenter : MvpBasePresenter<AutoCompleteView>() {

    fun userEnteredPlace(place: String) {
        view.startFetchingAddress(place, 20)
    }

    fun userAddressReceived(results: List<Address>) {
        if (isViewAttached) {
            view.displayAddresses(results)
        }
    }

    fun userAddressFetchFailed(errorMessage: String) {
        if (isViewAttached) {
            view.displayPlaceFetchError(errorMessage)
        }
    }

    fun userSelectedAddress(address: Address) {
        with(view) {
            setSelectedAddress(address)
            goToQueryScreen()
        }
    }

    fun userClickedUp() {
        with(view) {
            setSelectedAddress(null)
            goToQueryScreen()
        }
    }

    fun userClickedBack() {
        view.setSelectedAddress(null)
    }

}
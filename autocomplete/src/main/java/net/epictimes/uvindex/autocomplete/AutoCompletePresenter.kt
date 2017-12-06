package net.epictimes.uvindex.autocomplete

import android.location.Address
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class AutoCompletePresenter : MvpBasePresenter<AutoCompleteView>() {

    fun userEnteredPlace(place: String) {
        ifViewAttached { it.startFetchingAddress(place, 20) }
    }

    fun userAddressReceived(results: List<Address>) {
        ifViewAttached { it.displayAddresses(results) }
    }

    fun userAddressFetchFailed(errorMessage: String) {
        ifViewAttached {
            it.clearAddresses()
            it.displayPlaceFetchError(errorMessage)
        }
    }

    fun userSelectedAddress(address: Address) {
        ifViewAttached {
            it.setSelectedAddress(address)
            it.goToQueryScreen()
        }
    }

    fun userClickedUp() {
        ifViewAttached {
            it.setSelectedAddress(null)
            it.goToQueryScreen()
        }
    }

    fun userClickedBack() {
        ifViewAttached { it.setSelectedAddress(null) }
    }

}
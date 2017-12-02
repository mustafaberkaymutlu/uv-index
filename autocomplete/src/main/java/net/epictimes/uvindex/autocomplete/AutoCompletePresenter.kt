package net.epictimes.uvindex.autocomplete

import android.location.Address
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

class AutoCompletePresenter : MvpBasePresenter<AutoCompleteView>() {

    fun userEnteredPlace(place: String) {
        view.startFetchingAddress(place)
    }

    fun userAddressReceived(addressFetchResult: AddressFetchResult, message: String, results: List<Address>) {
        if (addressFetchResult == AddressFetchResult.SUCCESS) view.displayAddresses(results)
        else view.displayPlaceFetchError(message)
    }

}
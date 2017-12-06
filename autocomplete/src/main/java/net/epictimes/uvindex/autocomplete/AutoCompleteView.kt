package net.epictimes.uvindex.autocomplete

import android.location.Address
import com.hannesdorfmann.mosby3.mvp.MvpView

interface AutoCompleteView : MvpView {

    fun displayAddresses(addresses: List<Address>)

    fun clearAddresses()

    fun startFetchingAddress(place: String, maxResults: Int)

    fun goToQueryScreen()

    fun setSelectedAddress(address: Address?)

    fun displayPlaceFetchError(errorMessage: String)

}
package net.epictimes.uvindex.autocomplete

import android.location.Address
import com.hannesdorfmann.mosby3.mvp.MvpView

interface AutoCompleteView : MvpView {

    fun displayAddresses(addresses: List<Address>)

    fun startFetchingAddress(place: String)

    fun displayPlaceFetchError(errorMessage: String)

}
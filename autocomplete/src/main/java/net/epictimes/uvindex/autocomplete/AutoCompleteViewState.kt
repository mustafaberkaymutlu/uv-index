package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState

class AutoCompleteViewState : RestorableViewState<AutoCompleteView> {

    companion object {
        val KEY_SEARCH_QUERY = "search_query"
        val KEY_ADDRESSES = "addresses"
        val KEY_ADDRESS_STATE = "addressState"
        val KEY_MESSAGE = "message"
    }

    var searchQuery: String? = null
    var addresses = ArrayList<Address>()
    var addressState: AddressFetchResult = AddressFetchResult.FAIL
    var errorMessage: String? = null

    override fun saveInstanceState(out: Bundle) {
        out.putString(KEY_SEARCH_QUERY, searchQuery)
        out.putSerializable(KEY_ADDRESSES, addresses)
        out.putSerializable(KEY_ADDRESS_STATE, addressState)
        out.putString(KEY_MESSAGE, errorMessage)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<AutoCompleteView> {
        `in`?.let {
            searchQuery = it.getString(KEY_SEARCH_QUERY)
            addresses.addAll(it.getParcelableArrayList(KEY_ADDRESSES))
            addressState = it.getSerializable(KEY_ADDRESS_STATE) as AddressFetchResult
            errorMessage = it.getString(KEY_MESSAGE)
        }

        return this
    }

    override fun apply(view: AutoCompleteView, retained: Boolean) {
        if (addressState == AddressFetchResult.SUCCESS) {
            view.displayAddresses(addresses)
        } else {
            errorMessage?.let {
                view.displayPlaceFetchError(it)
            }
        }
    }
}
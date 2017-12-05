package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import net.epictimes.uvindex.service.AddressFetchResult

class AutoCompleteViewState : RestorableViewState<AutoCompleteView> {

    companion object {
        val KEY_ADDRESSES = "addresses"
        val KEY_ADDRESS_STATE = "addressState"
        val KEY_MESSAGE = "message"
    }

    var addresses = ArrayList<Address>()
    var addressState: AddressFetchResult = AddressFetchResult.FAIL
    var errorMessage: String? = null

    override fun saveInstanceState(out: Bundle) {
        out.putSerializable(KEY_ADDRESSES, addresses)
        out.putSerializable(KEY_ADDRESS_STATE, addressState)
        out.putString(KEY_MESSAGE, errorMessage)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<AutoCompleteView> {
        `in`?.let {
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
package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_auto_complete.*
import net.epictimes.uvindex.ui.BaseViewStateActivity
import timber.log.Timber
import javax.inject.Inject


class AutoCompleteActivity : BaseViewStateActivity<AutoCompleteView, AutoCompletePresenter, AutoCompleteViewState>(),
        AutoCompleteView {

    @Inject
    lateinit var autoCompletePresenter: AutoCompletePresenter

    @Inject
    lateinit var autoCompleteViewState: AutoCompleteViewState

    private val placesAdapter: PlacesRecyclerView by lazy { PlacesRecyclerView() }

    private val addressResultReceiver: AutoCompleteActivity.AddressResultReceiver by lazy { AddressResultReceiver() }

    override fun createPresenter(): AutoCompletePresenter = autoCompletePresenter

    override fun createViewState(): AutoCompleteViewState = autoCompleteViewState

    override fun onNewViewStateInstance() {
        // no-op
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAutoCompleteComponent.builder()
                .singletonComponent(singletonComponent)
                .build()
                .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete)

        editTextPlace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.userEnteredPlace(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        with(recyclerViewPlaces) {
            layoutManager = LinearLayoutManager(this@AutoCompleteActivity)
            adapter = placesAdapter
        }
    }

    override fun displayAddresses(addresses: List<Address>) {
        Timber.d("received addresses: " + addresses)
        placesAdapter.setAddresses(addresses)
    }

    override fun startFetchingAddress(place: String) {
        FetchPlaceIntentService.startIntentService(this, addressResultReceiver, place)
    }

    override fun displayPlaceFetchError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    inner class AddressResultReceiver : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            val fetchResult = AddressFetchResult.values()[resultCode]
            val receivedMessage = resultData.getString(FetchPlaceIntentService.KEY_MESSAGE)
            val receivedAddresses = resultData.getParcelableArrayList<Address>(FetchPlaceIntentService.KEY_RESULT)

            with(viewState) {
                addresses.addAll(receivedAddresses)
                addressState = fetchResult
                message = receivedMessage
            }

            presenter.userAddressReceived(fetchResult, receivedMessage, receivedAddresses)
        }
    }
}

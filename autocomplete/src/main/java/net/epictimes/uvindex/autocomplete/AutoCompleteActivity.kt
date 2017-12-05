package net.epictimes.uvindex.autocomplete

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_auto_complete.*
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.service.AddressFetchResult
import net.epictimes.uvindex.service.FetchAddressIntentService
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
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(recyclerViewPlaces) {
            layoutManager = LinearLayoutManager(this@AutoCompleteActivity)
            adapter = placesAdapter
        }

        toolbar.setNavigationOnClickListener { presenter.userClickedUp() }
        placesAdapter.rowClickListener = { address -> presenter.userSelectedAddress(address) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.autocomplete, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        makeSearchEditTextColorWhite(searchView)

        with(searchView) {
            setIconifiedByDefault(false)
            isFocusable = true
            requestFocusFromTouch()

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean = false

                override fun onQueryTextChange(s: String): Boolean {
                    presenter.userEnteredPlace(s)
                    return true
                }
            })
        }

        return true
    }

    override fun displayAddresses(addresses: List<Address>) {
        Timber.d("received addresses: " + addresses)
        placesAdapter.setAddresses(addresses)
    }

    override fun startFetchingAddress(place: String, maxResults: Int) {
        FetchAddressIntentService.startIntentService(context = this,
                resultReceiver = addressResultReceiver,
                locationName = place,
                maxResults = maxResults)
    }

    override fun goToQueryScreen() {
        finish()
    }

    override fun setSelectedAddress(address: Address?) {
        val intent = Intent()

        address?.let {
            intent.putExtra(Constants.BundleKeys.ADDRESS, address)
            setResult(RESULT_OK, intent)
        } ?: run {
            setResult(Activity.RESULT_CANCELED, intent)
        }
    }

    override fun displayPlaceFetchError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        presenter.userClickedBack()
        super.onBackPressed()
    }

    private fun makeSearchEditTextColorWhite(searchView: SearchView) {
        val searchEditText = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        val colorWhite = ContextCompat.getColor(this, android.R.color.white)

        with(searchEditText) {
            setTextColor(colorWhite)
            setHintTextColor(colorWhite)
        }
    }

    inner class AddressResultReceiver : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            val fetchResult = AddressFetchResult.values()[resultCode]
            val receivedErrorMessage: String? = resultData.getString(FetchAddressIntentService.KEY_ERROR_MESSAGE)
            val receivedAddresses = resultData.getParcelableArrayList<Address>(FetchAddressIntentService.KEY_RESULT)

            with(viewState) {
                addresses.addAll(receivedAddresses)
                addressState = fetchResult
                errorMessage = receivedErrorMessage
            }

            when (fetchResult) {
                AddressFetchResult.SUCCESS -> {
                    presenter.userAddressReceived(receivedAddresses)
                }
                AddressFetchResult.FAIL -> {
                    presenter.userAddressFetchFailed(receivedErrorMessage!!)
                }
            }
        }
    }
}

package net.epictimes.uvindex.query

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.instantapps.InstantApps
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_query.*
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.ui.BaseViewStateActivity
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import javax.inject.Inject


@RuntimePermissions
class QueryActivity : BaseViewStateActivity<QueryView, QueryPresenter, QueryViewState>(), QueryView {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, QueryActivity::class.java)
    }

    private val LOCATION_INTERVAL: Long = 10000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var queryPresenter: QueryPresenter

    @Inject
    lateinit var queryViewState: QueryViewState

    override fun createViewState(): QueryViewState = queryViewState

    override fun createPresenter(): QueryPresenter = queryPresenter

    override fun onNewViewStateInstance() {
        requestLocationUpdatesWithPermissionCheck()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        setSupportActionBar(toolbar)

        buttonGetStarted.setOnClickListener {
            viewState.location?.let {
                presenter.getUvIndex(it.latitude, it.longitude, null, null)
            } ?: run {
                requestLocationUpdatesWithPermissionCheck()
            }
        }

        textViewLocation.setOnClickListener { presenter.userClickedTextInputButton() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.query, menu)

        val menuItemInstall = menu.findItem(R.id.action_install)
        menuItemInstall.isVisible = InstantApps.isInstantApp(this)

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RequestCodes.PLACE_AUTO_COMPLETE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = PlaceAutocomplete.getPlace(this, data)
                    viewState.location = LatLng(place.latLng.latitude, place.latLng.longitude)
                    textViewLocation.text = place.address
                    presenter.getUvIndex(place.latLng.latitude, place.latLng.longitude, null, null)
                    Timber.i("Place search operation succeed with place: " + place.name)
                }
                PlaceAutocomplete.RESULT_ERROR -> {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    presenter.getPlaceAutoCompleteFailed()
                    Timber.i("Place search operation failed with message: ${status.statusMessage}")
                }
                RESULT_CANCELED -> {
                    Timber.i("The user canceled the place search operation.")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_install -> {
            presenter.userClickedInstallButton()
            true
        }
        R.id.action_about -> {
            presenter.userClickedAboutButton()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()

        with(locationRequest) {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = LOCATION_INTERVAL
            fastestInterval = LOCATION_INTERVAL
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(locationRequest, CustomLocationCallback(), null)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        Toast.makeText(this, R.string.error_required_location_permission, Toast.LENGTH_SHORT).show()
    }

    override fun displayUvIndex(uvIndex: Int) {
        with(textViewUvIndex) {
            text = uvIndex.toString()
            visibility = View.VISIBLE
        }
    }

    override fun displayInstallPrompt(requestCode: Int, referrerCode: String) {
        val isDisplayed = InstantApps.showInstallPrompt(this,
                newIntent(this),
                requestCode,
                referrerCode)

        if (!isDisplayed) {
            Timber.e("InstantApps#displayInstallPrompt failed.")
        }
    }

    override fun displayUserAddress(address: String) {
        val textColor = ContextCompat.getColor(this, android.R.color.white)

        with(textViewLocation) {
            text = address
            setTextColor(textColor)
        }
    }

    override fun displayAboutUi() {
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
    }

    override fun startPlacesAutoCompleteUi(requestCode: Int) {
        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this)
            startActivityForResult(intent, Constants.RequestCodes.PLACE_AUTO_COMPLETE)
        } catch (e: Exception) {
            // Kotlin does not support multi-catch yet.
            when (e) {
                is ActivityNotFoundException,
                is GooglePlayServicesRepairableException,
                is GooglePlayServicesNotAvailableException -> {
                    Timber.e(e)
                    presenter.getPlaceAutoCompleteFailed()
                }
                else -> throw e
            }
        }
    }

    override fun displayUserAddressFetchError(errorMessage: String) {
        val textColor = ContextCompat.getColor(this, net.epictimes.uvindex.R.color.accent)

        with(textViewLocation) {
            text = errorMessage
            setTextColor(textColor)
        }
    }

    override fun displayGetUvIndexError() {
        Toast.makeText(this, R.string.error_getting_uv_index, Toast.LENGTH_SHORT).show()
    }

    override fun displayGetAutoCompletePlaceError() {
        Toast.makeText(this, R.string.error_getting_autocomplete_place, Toast.LENGTH_SHORT).show()
    }

    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.locations?.last()?.let {
                fusedLocationClient.removeLocationUpdates(this)
                viewState.location = LatLng(it.latitude, it.longitude)
                FetchAddressIntentService.startIntentService(this@QueryActivity, AddressResultReceiver(), it)
            }
        }
    }

    inner class AddressResultReceiver : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            // Display the address string or an error message sent from the intent service.
            val result: String = resultData.getString(FetchAddressIntentService.KEY_RESULT)

            presenter.userAddressReceived(resultCode, result)
        }
    }

}

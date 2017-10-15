package net.epictimes.uvindex.query

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.instantapps.InstantApps
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_query.*
import net.epictimes.uvindex.Constants
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.data.model.Weather
import net.epictimes.uvindex.ui.BaseViewStateActivity
import net.epictimes.uvindex.util.getReadableHour
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@RuntimePermissions
class QueryActivity : BaseViewStateActivity<QueryView, QueryPresenter, QueryViewState>(), QueryView {

    companion object {
        private val LOCATION_INTERVAL: Long = 10000 // update interval in milliseconds
        private val LOCATION_INTERVAL_FASTEST: Long = LOCATION_INTERVAL / 2

        private val LINE_CHART_ANIM_DURATION = 1500

        private val UV_INDEX_MIN = 0
        private val UV_INDEX_MAX = 11

        fun newIntent(context: Context): Intent = Intent(context, QueryActivity::class.java)
    }

    @Inject
    lateinit var queryPresenter: QueryPresenter

    @Inject
    lateinit var queryViewState: QueryViewState

    private val settingsClient: SettingsClient by lazy { LocationServices.getSettingsClient(this) }
    private val locationSettingsRequest: LocationSettingsRequest by lazy { createLocationSettingsRequest() }

    private val locationRequest: LocationRequest by lazy { createLocationRequest() }
    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationCallback: QueryLocationCallback by lazy { QueryLocationCallback() }

    private val chartColors by lazy { createChartColors() }

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

        styleLineChart()

        buttonGetStarted.setOnClickListener {
            viewState.location?.let {
                presenter.getForecastUvIndex(it.latitude, it.longitude, null, null)
            } ?: run {
                requestLocationUpdatesWithPermissionCheck()
            }
        }

        textViewLocation.setOnClickListener { presenter.userClickedTextInputButton() }
    }

    override fun onResume() {
        super.onResume()

        if (viewState.locationSearchState == QueryViewState.LocationSearchState.Paused) {
            requestLocationUpdatesWithPermissionCheck()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates(QueryViewState.LocationSearchState.Paused)
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

                    with(viewState) {
                        location = LatLng(place.latLng.latitude, place.latLng.longitude)
                        locationSearchState = QueryViewState.LocationSearchState.Idle
                    }

                    textViewLocation.text = place.address
                    presenter.getForecastUvIndex(place.latLng.latitude, place.latLng.longitude, null, null)
                    Timber.i("Place search operation succeed with place: " + place.name)
                }
                PlaceAutocomplete.RESULT_ERROR -> {
                    viewState.locationSearchState = QueryViewState.LocationSearchState.Idle
                    val status = PlaceAutocomplete.getStatus(this, data)
                    presenter.getPlaceAutoCompleteFailed()
                    Timber.i("Place search operation failed with message: ${status.statusMessage}")
                }
                RESULT_CANCELED -> {
                    Timber.i("The user canceled the place search operation.")
                }
            }
        } else if (requestCode == Constants.RequestCodes.UPDATE_LOCATION_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> {
                    // Nothing to do. startLocationUpdates() gets called in onResume again.
                    Timber.i("User agreed to make required location settings changes.")
                    viewState.locationSearchState = QueryViewState.LocationSearchState.Paused
                }
                RESULT_CANCELED -> {
                    Timber.i("User chose not to make required location settings changes.")
                    viewState.locationSearchState = QueryViewState.LocationSearchState.Idle
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
        viewState.locationSearchState = QueryViewState.LocationSearchState.SearchingLocation

        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                }
                .addOnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ")
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                (e as ResolvableApiException).startResolutionForResult(this@QueryActivity,
                                        Constants.RequestCodes.UPDATE_LOCATION_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Timber.i("PendingIntent unable to execute request.")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            Timber.e(errorMessage)
                            Toast.makeText(this@QueryActivity, errorMessage, Toast.LENGTH_LONG).show()
                            viewState.locationSearchState = QueryViewState.LocationSearchState.Idle
                        }
                    }
                }
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        Snackbar.make(coordinatorLayout, R.string.error_required_location_permission, Snackbar.LENGTH_LONG).show()
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()

        with(locationRequest) {
            interval = LOCATION_INTERVAL
            fastestInterval = LOCATION_INTERVAL_FASTEST
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        return locationRequest
    }

    private fun createLocationSettingsRequest(): LocationSettingsRequest {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        return builder.build()
    }

    private fun createChartColors(): List<Int> {
        val typedArray = resources.obtainTypedArray(net.epictimes.uvindex.query.R.array.uv_indexes)
        val colors = ArrayList<Int>()

        (0 until typedArray.length()).mapTo(colors) { typedArray.getColor(it, Color.BLACK) }

        typedArray.recycle()
        return colors
    }

    override fun displayUvIndex(weather: Weather) {
        with(textViewUvIndex) {
            text = weather.uvIndex.toString()
        }

        val cardBackgroundColor = chartColors[weather.uvIndex]
        cardViewCurrent.setCardBackgroundColor(cardBackgroundColor)

        val recommendedProtection: Int
        val info: Int

        when (weather.uvIndex) {
            in 0 until 3 -> {
                recommendedProtection = R.string.recommended_prot_0_3
                info = R.string.info_0_3
            }
            in 3 until 6 -> {
                recommendedProtection = R.string.recommended_prot_3_6
                info = R.string.info_3_6
            }
            in 6 until 8 -> {
                recommendedProtection = R.string.recommended_prot_6_8
                info = R.string.info_6_8
            }
            in 8 until 11 -> {
                recommendedProtection = R.string.recommended_prot_8_11
                info = R.string.info_8_11
            }
            11 -> {
                recommendedProtection = R.string.recommended_prot_extreme
                info = R.string.info_extreme
            }
            else -> {
                recommendedProtection = R.string.recommended_prot_0_3
                info = R.string.info_0_3
            }
        }

        textViewRecommendedProtection.setText(recommendedProtection)
        textViewInfo.setText(info)
    }

    override fun displayUvIndexForecast(uvIndexForecast: List<Weather>) {
        Timber.d("Displaying uv index forecast:")
        uvIndexForecast.forEach { Timber.d(it.toString()) }

        with(viewState.uvIndexForecast) {
            clear()
            addAll(uvIndexForecast)
        }

        val sliderColors = arrayListOf<Int>()

        uvIndexForecast.mapTo(sliderColors) { chartColors[it.uvIndex] }

        with(lineChart) {
            data = getForecastLineData(uvIndexForecast)
            animateX(LINE_CHART_ANIM_DURATION)
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
        val textColor = ContextCompat.getColor(this, android.R.color.black)

        with(textViewLocation) {
            text = address
            setTextColor(textColor)
        }
    }

    override fun displayAboutUi() {
        Snackbar.make(coordinatorLayout, "Not implemented", Snackbar.LENGTH_LONG).show()
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
        Snackbar.make(coordinatorLayout, R.string.error_getting_uv_index, Snackbar.LENGTH_LONG).show()
    }

    override fun displayGetAutoCompletePlaceError() {
        Snackbar.make(coordinatorLayout, R.string.error_getting_autocomplete_place, Snackbar.LENGTH_LONG).show()
    }

    private fun stopLocationUpdates(newState: QueryViewState.LocationSearchState) {
        if (viewState.locationSearchState != QueryViewState.LocationSearchState.SearchingLocation) {
            Timber.d("stopLocationUpdates: updates never requested, no-op.")
            return
        }

        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener {
                    viewState.locationSearchState = newState
                }
    }

    private fun styleLineChart() {
        val chartDesc = Description()
        with(chartDesc) {
            text = getString(R.string.chart_desc)
            textColor = ContextCompat.getColor(this@QueryActivity, net.epictimes.uvindex.R.color.primary)
        }

        with(lineChart) {
            description = chartDesc
            isDragEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false) // the background rectangle behind the chart drawing-area
            setDrawBorders(true) // lines surrounding the chart
            setNoDataText(getString(R.string.chart_no_data))
            setNoDataTextColor(ContextCompat.getColor(this@QueryActivity, android.R.color.black))
            setScaleEnabled(false)
            legend.isEnabled = false

            with(xAxis) {
                isEnabled = true
                setAvoidFirstLastClipping(true)
                setDrawLabels(true)
                setValueFormatter { value, _ ->
                    viewState.uvIndexForecast[value.toInt()].datetime.getReadableHour()
                }
            }

            with(axisLeft) {
                isEnabled = true
                axisMinimum = UV_INDEX_MIN.toFloat()
                axisMaximum = UV_INDEX_MAX.toFloat()
                setDrawLabels(true)
                setDrawGridLines(true)
                setValueFormatter { value, _ -> String.format("%.0f", value) }
            }

            axisRight.isEnabled = false

            invalidate()
        }
    }

    private fun getForecastLineData(weatherForecast: List<Weather>): LineData {
        val yValues = ArrayList<Entry>()

        weatherForecast
                .forEachIndexed { index, weather ->
                    val entry = Entry(index.toFloat(), weather.uvIndex.toFloat())
                    yValues.add(entry)
                }

        val lineDataSet = UvIndexDataSet(yValues, "All UV Indexes")

        with(lineDataSet) {
            mode = LineDataSet.Mode.LINEAR
            lineWidth = 4f
            setDrawFilled(true)
            circleRadius = 1f
            colors = chartColors
            isHighlightEnabled = false
            valueTextSize = 12f
            setDrawValues(true)
            setDrawHighlightIndicators(false) // disable the drawing of highlight indicator (lines)
            valueFormatter = IValueFormatter { value, _, _, _ ->
                if (value == 0f) {
                    ""
                } else {
                    String.format("%.0f", value)
                }
            }
        }

        return LineData(lineDataSet)
    }

    inner class QueryLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.lastLocation?.let {
                stopLocationUpdates(QueryViewState.LocationSearchState.Idle)
                viewState.location = LatLng(it.latitude, it.longitude)
                presenter.getForecastUvIndex(it.latitude, it.longitude, null, null)
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

package net.epictimes.uvindex.query

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_query.*
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.ui.BaseViewStateActivity
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject


@RuntimePermissions
class QueryActivity : BaseViewStateActivity<QueryView, QueryPresenter, QueryViewState>(), QueryView {

    private val LOCATION_INTERVAL: Long = 10000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var queryPresenter: QueryPresenter

    @Inject
    lateinit var queryViewState: QueryViewState

    override fun createViewState(): QueryViewState {
        return queryViewState
    }

    override fun createPresenter(): QueryPresenter {
        return queryPresenter
    }

    override fun onNewViewStateInstance() {
        // no-op
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)

        // TODO use AndroidInjection

        DaggerQueryComponent.builder()
                .application(application as BaseApplication)
                .singletonComponent((application as BaseApplication).singletonComponent)
                .queryActivityModule(QueryActivityModule())
                .build()
                .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)

        buttonGetStarted.setOnClickListener {
            viewState.location?.let {
                presenter.getUvIndex(it.latitude, it.longitude, null, null)
            } ?: run {
                requestLocationUpdatesWithPermissionCheck()
            }
        }

        requestLocationUpdatesWithPermissionCheck()
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
        Toast.makeText(this, R.string.error_required_location_permission_, Toast.LENGTH_SHORT).show()
    }

    override fun displayUvIndex(uvIndex: Int) {
        textViewUvIndex.text = uvIndex.toString()
    }

    override fun displayGetUvIndexError() {
        Toast.makeText(this, R.string.error_getting_uv_index, Toast.LENGTH_SHORT).show()
    }

    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.locations?.last()?.let {
                fusedLocationClient.removeLocationUpdates(this)
                viewState.location = it
                FetchAddressIntentService.startIntentService(this@QueryActivity, AddressResultReceiver(), it)
            }
        }
    }

    inner class AddressResultReceiver : ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)

            // Display the address string or an error message sent from the intent service.
            val result: String = resultData.getString(FetchAddressIntentService.KEY_RESULT)

            @ColorInt
            val textColor: Int = when (resultCode) {
                FetchAddressIntentService.RESULT_SUCCESS -> R.color.colorPrimary
                FetchAddressIntentService.RESULT_FAILURE -> R.color.colorPrimaryDark
                else -> android.R.color.black
            }

            with(textViewLocation) {
                text = result
                setTextColor(ContextCompat.getColor(this@QueryActivity, textColor))
            }
        }
    }

}

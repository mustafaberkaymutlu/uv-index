package net.epictimes.uvindex.query

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
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
class QueryActivity : BaseViewStateActivity<QueryView, QueryPresenter, QueryViewState>() {

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

        buttonGetObservationsByLocation.setOnClickListener { requestLocationUpdatesWithPermissionCheck() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        fusedLocationClient.requestLocationUpdates(locationRequest, CustomLocationCallback(), null)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        Toast.makeText(this, "Location permission required to get location", Toast.LENGTH_SHORT).show()
    }

    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)

            p0?.locations?.last()?.let {
                fusedLocationClient.removeLocationUpdates(this)
                presenter.getUvIndex(it.latitude, it.longitude, null, null)
            }
        }
    }

}

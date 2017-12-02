package net.epictimes.uvindex.autocomplete

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import timber.log.Timber
import java.io.IOException
import java.util.*


class FetchPlaceIntentService : IntentService("FetchPlaceIntentService") {

    companion object {
        private val MAX_RESULTS = 20

        private val PACKAGE_NAME = "net.epictimes.uvindex.autocomplete"
        val KEY_RESULT_RECEIVER = PACKAGE_NAME + ".RESULT_RECEIVER"
        val KEY_PLACE = PACKAGE_NAME + ".PLACE"
        val KEY_MESSAGE = PACKAGE_NAME + ".MESSAGE"
        val KEY_RESULT = PACKAGE_NAME + ".RESULT"

        fun startIntentService(context: Context,
                               resultReceiver: ResultReceiver,
                               place: String) {
            val intent = Intent(context, FetchPlaceIntentService::class.java)
            intent.putExtra(KEY_RESULT_RECEIVER, resultReceiver)
            intent.putExtra(KEY_PLACE, place)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent) {
        val resultReceiver = intent.getParcelableExtra<ResultReceiver>(KEY_RESULT_RECEIVER)
        val place = intent.getStringExtra(KEY_PLACE)

        val geocoder = Geocoder(this, Locale.getDefault())

        var errorMessage = ""
        val addresses = arrayListOf<Address>()

        try {
            val results = geocoder.getFromLocationName(place, MAX_RESULTS)
            addresses.addAll(results)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.address_service_not_available)
            Timber.e(errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid name values.
            errorMessage = getString(R.string.place_service_invalid_place)
            Timber.e("Place can not be null", illegalArgumentException)
        }

        // Handle case where no place was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.address_service_no_place_found)
                Timber.e(errorMessage)
            }
            deliverResultToReceiver(resultReceiver, AddressFetchResult.FAIL, errorMessage, addresses)
        } else {
            Timber.i(getString(R.string.address_service_address_found))
            deliverResultToReceiver(resultReceiver, AddressFetchResult.SUCCESS, getString(R.string.address_service_address_found), addresses)
        }
    }

    private fun deliverResultToReceiver(resultReceiver: ResultReceiver,
                                        fetchResult: AddressFetchResult,
                                        message: String,
                                        address: ArrayList<Address>) {
        val bundle = Bundle()
        bundle.putString(KEY_MESSAGE, message)
        bundle.putParcelableArrayList(KEY_RESULT, address)
        resultReceiver.send(fetchResult.ordinal, bundle)
    }
}

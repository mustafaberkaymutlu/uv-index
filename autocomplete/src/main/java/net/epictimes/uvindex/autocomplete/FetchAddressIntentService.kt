package net.epictimes.uvindex.autocomplete

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import net.epictimes.uvindex.base.R
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FetchAddressIntentService : IntentService("FetchAddressIntentService") {

    companion object {
        private const val PACKAGE_NAME = "net.epictimes.uvindex"
        private const val KEY_RESULT_RECEIVER = PACKAGE_NAME + ".RESULT_RECEIVER"
        private const val KEY_LOCATION_NAME = PACKAGE_NAME + ".LOCATION_NAME"
        private const val KEY_LOCALE = PACKAGE_NAME + ".LOCALE"
        private const val KEY_MAX_RESULTS = PACKAGE_NAME + ".MAX_RESULTS"

        const val KEY_ERROR_MESSAGE = PACKAGE_NAME + ".ERROR_MESSAGE"
        const val KEY_RESULT = PACKAGE_NAME + ".RESULT"

        fun startIntentService(context: Context,
                               resultReceiver: ResultReceiver,
                               locationName: String,
                               locale: Locale = Locale.getDefault(),
                               maxResults: Int = 1) {
            val intent = Intent(context, FetchAddressIntentService::class.java)
            with(intent) {
                putExtra(KEY_RESULT_RECEIVER, resultReceiver)
                putExtra(KEY_LOCATION_NAME, locationName)
                putExtra(KEY_LOCALE, locale)
                putExtra(KEY_MAX_RESULTS, maxResults)
            }
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent) {
        val resultReceiver: ResultReceiver = intent.getParcelableExtra(KEY_RESULT_RECEIVER)
        val locale = intent.getSerializableExtra(KEY_LOCALE) as Locale
        val maxResults = intent.getIntExtra(KEY_MAX_RESULTS, 1)

        val geocoder = Geocoder(this, locale)

        var errorMessage = ""
        val addresses = ArrayList<Address>()

        try {
            val locationName = intent.getStringExtra(KEY_LOCATION_NAME)
            val results = geocoder.getFromLocationName(locationName, maxResults)
            addresses.addAll(results)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.address_service_not_available)
            Timber.e(ioException, errorMessage)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.address_service_invalid_input)
            Timber.e(illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.address_service_no_address_found)
                Timber.e(errorMessage)
            }
            deliverResultToReceiver(resultReceiver, AddressFetchResult.FAIL, addresses, errorMessage)
        } else {
            Timber.i(getString(R.string.address_service_address_found))
            deliverResultToReceiver(resultReceiver, AddressFetchResult.SUCCESS, addresses, null)
        }
    }

    private fun deliverResultToReceiver(resultReceiver: ResultReceiver,
                                        fetchResult: AddressFetchResult,
                                        address: ArrayList<Address>,
                                        errorMessage: String?) {
        val bundle = Bundle()
        with(bundle) {
            putParcelableArrayList(KEY_RESULT, address)
            putString(KEY_ERROR_MESSAGE, errorMessage)
        }
        resultReceiver.send(fetchResult.ordinal, bundle)
    }
}

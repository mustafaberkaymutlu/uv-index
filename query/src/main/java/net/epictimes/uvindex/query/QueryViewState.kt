package net.epictimes.uvindex.query

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.data.model.Weather
import java.util.*

class QueryViewState : RestorableViewState<QueryView> {

    enum class LocationSearchState {
        Idle,
        SearchingLocation,
        Paused
    }

    companion object {
        const val KEY_LOCATION = "location"
        const val KEY_ADDRESS = "address"
        const val KEY_STATE = "locationSearchState"
        const val KEY_CURRENT_UV_INDEX = "currentUvIndex"
        const val KEY_UV_INDEX_FORECAST = "uvIndexForecast"
        const val KEY_TIMEZONE = "timezone"
    }

    var location: LatLng? = null
    var locationSearchState: LocationSearchState = LocationSearchState.Idle

    var currentUvIndex: Weather? = null
    val uvIndexForecast = ArrayList<Weather>()

    var timezone: String? = null
    var address: String? = null

    override fun saveInstanceState(out: Bundle) {
        out.putParcelable(KEY_LOCATION, location)
        out.putString(KEY_ADDRESS, address)
        out.putSerializable(KEY_STATE, locationSearchState)
        out.putParcelable(KEY_CURRENT_UV_INDEX, currentUvIndex)
        out.putSerializable(KEY_UV_INDEX_FORECAST, uvIndexForecast)
        out.putString(KEY_TIMEZONE, timezone)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<QueryView> {
        `in`?.let {
            location = it.getParcelable(KEY_LOCATION)
            address = it.getString(KEY_ADDRESS)
            locationSearchState = it.getSerializable(KEY_STATE) as LocationSearchState
            currentUvIndex = it.getParcelable(KEY_CURRENT_UV_INDEX)
            uvIndexForecast.addAll(it.getParcelableArrayList(KEY_UV_INDEX_FORECAST))
            timezone = it.getString(KEY_TIMEZONE)
        }

        return this
    }

    override fun apply(view: QueryView, retained: Boolean) {
        with(view) {
            currentUvIndex?.let {
                displayUvIndex(it)
                displayUvIndexForecast(uvIndexForecast)

                address?.let { displayUserAddress(it) }
            }
        }
    }
}

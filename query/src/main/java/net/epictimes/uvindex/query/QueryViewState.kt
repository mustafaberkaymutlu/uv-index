package net.epictimes.uvindex.query

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import net.epictimes.uvindex.data.model.LatLng
import net.epictimes.uvindex.data.model.Weather

class QueryViewState : RestorableViewState<QueryView> {

    enum class LocationSearchState {
        Idle,
        SearchingLocation,
        Paused
    }

    companion object {
        val KEY_LOCATION = "location"
        val KEY_STATE = "locationSearchState"
        val KEY_UV_INDEX_FORECAST = "uvIndexForecast"
    }

    var location: LatLng? = null

    var locationSearchState: LocationSearchState = LocationSearchState.Idle

    val uvIndexForecast = ArrayList<Weather>()

    override fun saveInstanceState(out: Bundle) {
        out.putParcelable(KEY_LOCATION, location)
        out.putSerializable(KEY_STATE, locationSearchState)
        out.putSerializable(KEY_UV_INDEX_FORECAST, uvIndexForecast)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<QueryView> {
        `in`?.let {
            location = it.getParcelable(KEY_LOCATION)
            locationSearchState = it.getSerializable(KEY_STATE) as LocationSearchState
            uvIndexForecast.addAll(it.getSerializable(KEY_UV_INDEX_FORECAST) as ArrayList<Weather>)
        }

        return this
    }

    override fun apply(view: QueryView, retained: Boolean) {

    }
}

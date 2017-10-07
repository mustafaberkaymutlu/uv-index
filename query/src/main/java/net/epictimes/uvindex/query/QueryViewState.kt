package net.epictimes.uvindex.query

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import net.epictimes.uvindex.data.model.LatLng

class QueryViewState : RestorableViewState<QueryView> {

    enum class LocationSearchState {
        Idle,
        SearchingLocation,
        Paused
    }

    companion object {
        val KEY_LOCATION = "location"
        val KEY_STATE = "locationSearchState"
    }

    var location: LatLng? = null

    var locationSearchState: LocationSearchState = LocationSearchState.Idle

    override fun saveInstanceState(out: Bundle) {
        out.putParcelable(KEY_LOCATION, location)
        out.putSerializable(KEY_STATE, locationSearchState)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<QueryView> {
        `in`?.let {
            location = it.getParcelable(KEY_LOCATION)
            locationSearchState = it.getSerializable(KEY_STATE) as LocationSearchState
        }

        return this
    }

    override fun apply(view: QueryView, retained: Boolean) {

    }
}

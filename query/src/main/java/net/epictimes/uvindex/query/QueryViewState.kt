package net.epictimes.uvindex.query

import android.location.Location
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState

class QueryViewState : RestorableViewState<QueryView> {
    companion object {
        val KEY_LOCATION = "location"
    }

    var location: Location? = null

    override fun saveInstanceState(out: Bundle) {
        out.putParcelable(KEY_LOCATION, location)
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<QueryView> {
        `in`?.let {
            location = it.getParcelable(KEY_LOCATION)
        }

        return this
    }

    override fun apply(view: QueryView, retained: Boolean) {

    }
}

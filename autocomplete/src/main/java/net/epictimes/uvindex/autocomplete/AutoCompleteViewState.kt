package net.epictimes.uvindex.autocomplete

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState

class AutoCompleteViewState : RestorableViewState<AutoCompleteView> {
    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<AutoCompleteView> {
        return this
    }

    override fun saveInstanceState(out: Bundle) {

    }

    override fun apply(view: AutoCompleteView?, retained: Boolean) {

    }
}
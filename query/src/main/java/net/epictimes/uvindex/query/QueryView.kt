package net.epictimes.uvindex.query

import com.hannesdorfmann.mosby3.mvp.MvpView

interface QueryView : MvpView {

    fun displayUvIndex(uvIndex: Int)

    fun displayGetUvIndexError()

    fun displayGetAutoCompletePlaceError()

}

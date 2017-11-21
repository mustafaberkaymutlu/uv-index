package net.epictimes.uvindex.ui

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.di.SingletonComponent

abstract class BaseViewStateActivity<V : MvpView, P : MvpPresenter<V>, VS : ViewState<V>> : MvpViewStateActivity<V, P, VS>() {

    val singletonComponent: SingletonComponent
        get() = (application as BaseApplication).singletonComponent

}
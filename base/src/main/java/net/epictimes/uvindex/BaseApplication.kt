package net.epictimes.uvindex

import android.app.Application
import timber.log.Timber

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    open fun initTimber() {
        Timber.plant(CrashReportingTree())
    }
}

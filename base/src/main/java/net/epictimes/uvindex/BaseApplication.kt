package net.epictimes.uvindex

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.android.instantapps.InstantApps
import io.fabric.sdk.android.Fabric
import net.epictimes.uvindex.base.BuildConfig
import net.epictimes.uvindex.data.ApiModule
import net.epictimes.uvindex.di.DaggerSingletonComponent
import net.epictimes.uvindex.di.SingletonComponent
import net.epictimes.uvindex.di.SingletonModule
import timber.log.Timber

open class BaseApplication : Application() {

    lateinit var singletonComponent: SingletonComponent

    override fun onCreate() {
        super.onCreate()

        initCrashlytics()

        initTimber()

        initDagger()
    }

    private fun initCrashlytics() {
        Fabric.with(this, Crashlytics())
        Crashlytics.setBool("InstantApp", InstantApps.isInstantApp(this))
    }

    open fun initTimber() {
        val timberTree = if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree()
        Timber.plant(timberTree)
    }

    open fun initDagger() {
        singletonComponent = DaggerSingletonComponent.builder()
                .application(this)
                .singletonModule(SingletonModule())
                .apiModule(ApiModule())
                .build()

        singletonComponent.inject(this)
    }
}

package net.epictimes.uvindex

import android.app.Application
import com.facebook.stetho.Stetho
import net.epictimes.uvindex.data.ApiModule
import net.epictimes.uvindex.di.DaggerSingletonComponent
import net.epictimes.uvindex.di.SingletonComponent
import net.epictimes.uvindex.di.SingletonModule
import timber.log.Timber

abstract class BaseApplication : Application() {

    lateinit var singletonComponent: SingletonComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()

        Stetho.initializeWithDefaults(this)

        initDagger()
    }

    open fun initTimber() {
        Timber.plant(CrashReportingTree())
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

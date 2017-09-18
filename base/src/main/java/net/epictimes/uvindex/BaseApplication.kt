package net.epictimes.uvindex

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.epictimes.uvindex.data.ApiModule
import net.epictimes.uvindex.di.DaggerSingletonComponent
import net.epictimes.uvindex.di.SingletonComponent
import net.epictimes.uvindex.di.SingletonModule
import timber.log.Timber
import javax.inject.Inject

open class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var singletonComponent: SingletonComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()

        initDagger()
    }

    open fun initTimber() {
        Timber.plant(CrashReportingTree())
    }

    private fun initDagger() {
        singletonComponent = DaggerSingletonComponent.builder()
                .application(this)
                .singletonModule(SingletonModule())
                .apiModule(ApiModule())
                .build()

        singletonComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }
}

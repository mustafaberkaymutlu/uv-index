package net.epictimes.uvindex.query

import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.epictimes.uvindex.BaseApplication
import javax.inject.Inject

open class QueryApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private lateinit var queryComponent: QueryComponent

    override fun initDagger() {
        super.initDagger()

        queryComponent = DaggerQueryComponent.builder()
                .application(this)
                .singletonComponent(singletonComponent)
                .build()

        queryComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector
}
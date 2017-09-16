package net.epictimes.uvindex

import timber.log.Timber

class BaseDebugApplication : BaseApplication() {

    override fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}

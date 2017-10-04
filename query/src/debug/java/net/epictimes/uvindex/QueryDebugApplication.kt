package net.epictimes.uvindex

import net.epictimes.uvindex.query.QueryApplication
import timber.log.Timber

class QueryDebugApplication : QueryApplication() {

    override fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}
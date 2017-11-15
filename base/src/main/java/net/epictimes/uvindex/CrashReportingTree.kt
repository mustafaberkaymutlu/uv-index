package net.epictimes.uvindex

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.ERROR

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        Crashlytics.log(priority, tag, message)
        throwable?.let { Crashlytics.logException(it) }
    }

}

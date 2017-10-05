package net.epictimes.uvindex

import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.ERROR

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
//        Crashlytics.log(priority, tag, message)
//
//        if (throwable != null) {
//            Crashlytics.logException(throwable)
//        }
    }

}

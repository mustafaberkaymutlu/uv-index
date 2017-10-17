package net.epictimes.uvindex.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.getReadableHour(timeZone: String): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
    return simpleDateFormat.format(this)
}
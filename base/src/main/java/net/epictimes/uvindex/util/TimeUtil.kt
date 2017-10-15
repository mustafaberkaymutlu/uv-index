package net.epictimes.uvindex.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.getHour(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.HOUR_OF_DAY)
}

fun Date.getReadableHour(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
package net.epictimes.uvindex.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.getReadableHour(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
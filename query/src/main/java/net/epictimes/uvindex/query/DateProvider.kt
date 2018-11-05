package net.epictimes.uvindex.query

import java.util.Date
import javax.inject.Inject

class DateProvider @Inject constructor() {

    fun now() = Date()

}
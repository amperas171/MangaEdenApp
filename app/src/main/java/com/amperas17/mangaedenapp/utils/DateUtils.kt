package com.amperas17.mangaedenapp.utils

import com.amperas17.mangaedenapp.utils.DateUtils.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    const val MILLIS_IN_SECOND = 1000
}

fun Long.formatDefaultFromSeconds(): String = DATE_FORMAT.format(Date(DateUtils.MILLIS_IN_SECOND * this))



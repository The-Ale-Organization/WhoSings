package com.musixmatch.whosings.business.util

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateConverter @Inject constructor() {

    fun formatTime(time: Long): String {
        val date = Date(time)
        val format: Format = SimpleDateFormat("yyyy/MM/dd   HH:mm:ss", Locale.ITALY)
        return format.format(date)
    }
}
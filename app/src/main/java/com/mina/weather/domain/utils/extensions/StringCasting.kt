package com.mina.weather.domain.utils.extensions

import java.text.SimpleDateFormat
import java.util.Locale


fun String.to12HourFormat(): String {
        return try {
            val hoursStr = this.split(":").first()
            val hours = hoursStr.toInt()

            when {
                hours == 0 -> "12 AM"  // 00:00 → 12 AM
                hours < 12 -> "${hours.toString().padStart(2, '0')} AM"
                hours == 12 -> "12 PM"
                else -> "${(hours - 12).toString().padStart(2, '0')} PM"
            }
        } catch (e: Exception) {
            this
        }
    }


fun String.getDayName(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        dayFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

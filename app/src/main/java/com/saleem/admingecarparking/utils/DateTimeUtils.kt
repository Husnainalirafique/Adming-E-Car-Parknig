package com.saleem.admingecarparking.utils

import com.saleem.admingecarparking.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    private const val PATTERN_DATE_OF_BIRTH = "MMM, dd, yyyy"
    fun formatHoursMinutes(timestamp: Long): String =
        SimpleDateFormat(Constants.TIME_FORMAT_H_MM_A, Locale.getDefault()).format(timestamp)

    fun formatCompleteDateAndTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.DATE_TIME_FORMAT_MM_DD_YYYY_HH_MM_SS, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun formatDateOfBirth(dateStamp: Long): String = SimpleDateFormat(Constants.DATE_FORMAT_MMM_DD_YYYY, Locale.getDefault()).format(dateStamp)

    fun formatDateOfBirth(selectedDate: Date): String {
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT_MMM_DD_YYYY, Locale.getDefault())
        return dateFormat.format(selectedDate)
    }
}
package com.saleem.admingecarparking.utils

object Constants {
    const val ROOM_DB_NAME = "roomDb"
    const val KEY_EMAIL = "userEmail"
    const val KEY_PASSWORD = "userPassword"
    const val KEY_PARCEL_MODEL_PARKING = "parcelModelParkingSpace"

    /*........ Date/Time Format Patterns ........*/
    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd" // Four-digit year, two-digit month, and two-digit day (e.g., "2024-01-28").
    const val DATE_FORMAT_MMM_DD_YYYY = "MMM dd, yyyy" //(e.g., "Jan 28, 2024").
    const val TIME_FORMAT_H_MM_A = "h:mm a" // Hour in 12-hour format, minutes, and AM/PM indicator (e.g., "2:30 PM").
    const val TIME_FORMAT_HH_MM = "HH:mm" // Hour in 24-hour format and minutes (e.g., "14:30").
    const val DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy" // Two-digit day, two-digit month, and four-digit year (e.g., "28/01/2024").
    const val DATE_FORMAT_MMMM_DD_YYYY = "MMMM dd, yyyy" // Full month name, day of the month, and four-digit year (e.g., "January 28, 2024").
    const val DATE_TIME_FORMAT_MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss" // Month, day, year, hour, and minutes (e.g., "01/28/2024 14:30:45").
    const val DATE_TIME_FORMAT_EEE_MMM_D_YYYY_HH_MM_A = "EEE, MMM d, yyyy HH:mm a" // Day of the week, abbreviated month name, day of the month, year, hour in 12-hour format, minutes, and AM/PM indicator (e.g., "Fri, Jan 28, 2024 02:30 PM").
    const val DATE_FORMAT_DD_MMMM_YYYY = "dd MMMM yyyy" // Day of the month, full month name, and four-digit year (e.g., "28 January 2024").
    const val TIME_FORMAT_HH_MM_SS = "HH:mm:ss" // Hours, minutes, and seconds in 24-hour format (e.g., "14:30:45").

}
package com.saleem.admingecarparking.utils

import com.google.android.gms.maps.model.LatLng

object LatLngUtil {
    fun latLngToString(latLng: LatLng): String {
        return "${latLng.latitude},${latLng.longitude}"
    }

    fun stringToLatLng(latLngString: String): LatLng {
        val (latitude, longitude) = latLngString.split(",").map { it.toDouble() }
        return LatLng(latitude, longitude)
    }
}
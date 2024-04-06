package com.saleem.admingecarparking.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelParkingSpace(
    val spaceName: String,
    val spaceLocationInText: String,
    val pricePerHour: String,
    val numberOfSpots: Int,
    val spaceMapLocation: String,
    var spaceImage: String,
    var docId: String
):Parcelable{
    constructor():this("","","",0,"","","")
}

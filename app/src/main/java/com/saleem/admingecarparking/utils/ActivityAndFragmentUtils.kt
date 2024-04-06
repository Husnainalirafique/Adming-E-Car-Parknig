package com.saleem.admingecarparking.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.visible() {
    visibility = View.VISIBLE
}

fun String.withHi(): String {
    return "Hi, $this!"
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun String.wrapWithRsHr(): String {
    return "Rs $this/hr"
}

fun String.wrapWithSpots(): String {
    return "$this Spots"
}

fun Activity.startActivity(destinationActivity: Class<*>) {
    val intent = Intent(this, destinationActivity)
    startActivity(intent)
}

fun Fragment.startActivity(destinationActivity: Class<*>) {
    val intent = Intent(requireActivity(), destinationActivity)
    startActivity(intent)
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Activity.getColorFromId(colorId: Int): Int {
    return this.resources.getColor(colorId, null)
}

fun Fragment.getColorFromId(colorId: Int): Int {
    return requireContext().resources.getColor(colorId, null)
}

fun Context.getColorFromId(colorId: Int): Int {
    return this.resources.getColor(colorId, null)
}

fun openAppSettings(activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivity(intent)
}



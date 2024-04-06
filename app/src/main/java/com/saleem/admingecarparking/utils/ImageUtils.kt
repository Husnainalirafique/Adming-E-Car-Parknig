package com.saleem.admingecarparking.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.IOException

object ImageUtils {
    fun Fragment.uriToBitmap(uri: String): Bitmap? {
        return try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri.toUri())
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

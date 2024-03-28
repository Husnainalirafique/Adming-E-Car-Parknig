package com.saleem.admingecarparking.utils

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object BackPressedUtils {
    inline fun AppCompatActivity.goBackPressed(crossinline callback: () -> Unit) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback()
            }
        })
    }


    inline fun Fragment.goBackPressed(crossinline callback: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback()
            }
        })
    }
}
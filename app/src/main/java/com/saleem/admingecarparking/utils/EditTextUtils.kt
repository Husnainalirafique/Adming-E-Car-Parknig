package com.saleem.admingecarparking.utils

import android.annotation.SuppressLint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.EditText
import com.saleem.admingecarparking.R

object EditTextUtils {
    @SuppressLint("ClickableViewAccessibility")
    fun setPasswordVisibilityToggle(editText: EditText) {
        editText.setOnTouchListener { view, motionEvent ->
            val drawableEnd = editText.compoundDrawablesRelative[2]
            if (drawableEnd != null && motionEvent.action == MotionEvent.ACTION_UP) {
                val bounds = drawableEnd.bounds
                val x = motionEvent.x.toInt()

                if (x >= view.width - view.paddingRight - bounds.width()) {
                    val cursorPosition = editText.selectionStart
                    val isPasswordVisible =
                        editText.transformationMethod == PasswordTransformationMethod.getInstance()

                    editText.transformationMethod =
                        if (isPasswordVisible) HideReturnsTransformationMethod.getInstance()
                        else PasswordTransformationMethod.getInstance()

                    editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        if (isPasswordVisible) R.drawable.icon_password_visibility_off else R.drawable.icon_password_visibility_on,
                        0
                    )

                    editText.setSelection(cursorPosition)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}
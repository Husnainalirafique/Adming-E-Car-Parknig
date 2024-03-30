package com.saleem.admingecarparking.utils
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipboardUtils {

    fun Activity.copyToClipboard(text: String) {
        val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboardManager.setPrimaryClip(clipData)
    }
}

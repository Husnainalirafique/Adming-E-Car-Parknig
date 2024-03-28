package com.saleem.admingecarparking.utils

import androidx.fragment.app.Fragment

object ProgressDialogUtil {

    private var progressDialog: ProgressDialogFragment? = null

    fun Fragment.showProgressDialog() {
        progressDialog = ProgressDialogFragment()
        progressDialog?.show(this.parentFragmentManager, "progressDialog")
    }

    fun Fragment.dismissProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}

package com.mina.weather.presentation.ui.utils.helpers

import android.content.Context
import android.widget.Toast

object ToastHelper {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG).apply { show() }
    }
}
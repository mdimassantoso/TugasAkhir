package com.app.msm.extension

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun <T> Context.openActivity(
    destination: Class<T>,
    clearTask: Boolean = false,
    singleTop: Boolean = false,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, destination)
    extras.invoke(intent)
    if (clearTask) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if (singleTop) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
}

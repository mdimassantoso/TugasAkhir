package com.app.msm.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

inline fun <T : ViewBinding> ViewGroup.inflateBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToParent: Boolean = false
) = bindingInflater.invoke(LayoutInflater.from(this.context), this, attachToParent)

fun View.showSnackBar(message: String) {
    if (message.isEmpty()) return
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}
package com.app.msm.helper

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.fragment.app.FragmentManager
import com.app.msm.BuildConfig
import com.app.msm.R
import com.app.msm.extension.addZeroPrefixIfNeeded
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

object Helper {
    fun isDebugBuild(): Boolean = BuildConfig.DEBUG
    fun isAndroidQ(): Boolean = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q

    fun showTimePickerDialog(
        fragmentManager: FragmentManager,
        onSelected: (time: String) -> Unit
    ) {
        MaterialTimePicker.Builder().apply {
            setPositiveButtonText(R.string.label_set)
            setNegativeButtonText(R.string.label_cancel)
            setTitleText(R.string.label_select_time)
            setTimeFormat(TimeFormat.CLOCK_24H)
        }.build().apply {
            addOnPositiveButtonClickListener {
                onSelected.invoke("${hour.addZeroPrefixIfNeeded()}:${minute.addZeroPrefixIfNeeded()}")
            }
        }.also { dialog ->
            dialog.show(fragmentManager, MaterialTimePicker::class.java.name)
        }
    }

    fun showAlertDialog(
        context: Context,
        title: String,
        positiveButtonText: String,
        message: String? = null,
        negativeButtonText: String? = null,
        onPositiveButtonClicked: (dialog: DialogInterface) -> Unit = { dialog -> dialog.dismiss() },
        onNegativeButtonClicked: (dialog: DialogInterface) -> Unit = { dialog -> dialog.dismiss() }
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositiveButtonClicked.invoke(dialog)
            }
            if (!message.isNullOrEmpty()) {
                setMessage(message)
            }
            if (!negativeButtonText.isNullOrEmpty()) {
                setNegativeButton(negativeButtonText) { dialog, _ ->
                    onNegativeButtonClicked.invoke(dialog)
                }
            }
        }.show()
    }
}

package com.app.msm.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Control(
    val id: Int,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    var isChecked: Boolean = false
)

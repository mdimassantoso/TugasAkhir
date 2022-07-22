package com.app.msm.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.msm.ui.main.controlling.ControlType

data class Control(
    val id: Int,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    var type: ControlType = ControlType.Switch()
)

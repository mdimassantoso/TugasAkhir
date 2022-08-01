package com.app.msm.model.controlling

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.msm.ui.main.controlling.ControlType

data class Control(
    @StringRes val label: Int,
    val id: Int = label,
    @DrawableRes val icon: Int,
    var type: ControlType = ControlType.Switch()
)

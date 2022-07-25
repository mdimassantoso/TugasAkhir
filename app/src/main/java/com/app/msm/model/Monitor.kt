package com.app.msm.model

import androidx.annotation.StringRes

data class Monitor(
    @StringRes val label: Int,
    val id: Int = label,
    val value: String
)

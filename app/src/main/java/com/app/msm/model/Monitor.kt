package com.app.msm.model

import androidx.annotation.StringRes

data class Monitor(
    val id: Int,
    @StringRes val label: Int,
    val value: String
)

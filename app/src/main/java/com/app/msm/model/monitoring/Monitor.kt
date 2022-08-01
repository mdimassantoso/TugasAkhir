package com.app.msm.model.monitoring

import androidx.annotation.StringRes

data class Monitor(
    @StringRes val label: Int,
    val id: Int = label,
    val value: String
) {
    companion object {
        const val DATE_PATTERN = "dd-MM-yyy"
    }
}

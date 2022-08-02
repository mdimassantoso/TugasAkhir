package com.app.msm.model.monitoring

import androidx.annotation.StringRes

data class Monitor(
    @StringRes val label: Int,
    val id: Int = label,
    val value: String,
    val type: ViewType
) {
    data class ViewType(
        val view: Int,
        val data: Int
    )
    companion object {
        const val DATE_PATTERN = "dd-MM-yyy"
        const val VIEW_TYPE_DEFAULT = 0
        const val VIEW_TYPE_GAUGE = 1
        const val DATA_TYPE_TEMP = 0
        const val DATA_TYPE_HUMIDITY = 1
        const val DATA_TYPE_PLAIN_TEXT = 2
    }
}

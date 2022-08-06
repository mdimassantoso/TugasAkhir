package com.app.msm.model.monitoring

import androidx.annotation.StringRes

data class Monitor(
    @StringRes val label: Int,
    val id: Int = label,
    val value: String,
    val viewType: ViewType,
    val dataType: DataType,
    val unitMeasurement: UnitMeasurement
) {
    sealed class DataType {
        object Temperature : DataType()
        object Humidity : DataType()
        object PlainText : DataType()
    }

    sealed class UnitMeasurement {
        object Celcius : UnitMeasurement()
        object Percent : UnitMeasurement()
        object Day : UnitMeasurement()
        object NoMeasurement: UnitMeasurement()
    }

    sealed class ViewType {
        object Default : ViewType() {
            const val VIEW_TYPE = 0
        }

        object DefaultSeparatedUnitMeasurement : ViewType() {
            const val VIEW_TYPE = 1
        }

        object Gauge : ViewType() {
            const val VIEW_TYPE = 2
        }
    }

    companion object {
        const val DATE_PATTERN = "dd-MM-yyy"
        const val VIEW_TYPE_DEFAULT = 0
        const val VIEW_TYPE_GAUGE = 1
        const val DATA_TYPE_TEMP = 0
        const val DATA_TYPE_HUMIDITY = 1
        const val DATA_TYPE_PLAIN_TEXT = 2
    }
}

package com.app.msm.data.api.response.monitoring

import com.app.msm.R
import com.app.msm.extension.orDash
import com.app.msm.extension.orZero
import com.app.msm.model.monitoring.Monitor
import com.google.firebase.database.PropertyName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MonitoringResponse(

    @JvmField
    @PropertyName("age")
    val age: Int? = null,

    @JvmField
    @PropertyName("kelembapan")
    val humidity: String? = null,

    @JvmField
    @PropertyName("suhu")
    val temperature: String? = null

) {
    fun toMonitors(): List<Monitor> {
        val temperature = Monitor(
            label = R.string.label_suhu,
            value = this.temperature.toString(),
            type = Monitor.ViewType(
                view = Monitor.VIEW_TYPE_GAUGE,
                data = Monitor.DATA_TYPE_TEMP
            )
        )
        val humidity = Monitor(
            label = R.string.label_kelembapan,
            value = this.humidity.toString(),
            type = Monitor.ViewType(
                view = Monitor.VIEW_TYPE_GAUGE,
                data = Monitor.DATA_TYPE_HUMIDITY
            )
        )
        val age = Monitor(
            label = R.string.label_age,
            value = "${age.orZero()} Hari",
            type = Monitor.ViewType(
                view = Monitor.VIEW_TYPE_DEFAULT,
                data = Monitor.DATA_TYPE_PLAIN_TEXT
            )
        )
        val currentDate = Monitor(
            label = R.string.label_date,
            value = LocalDate.now().format(DateTimeFormatter.ofPattern(Monitor.DATE_PATTERN)),
            type = Monitor.ViewType(
                view = Monitor.VIEW_TYPE_DEFAULT,
                data = Monitor.DATA_TYPE_PLAIN_TEXT
            )
        )
        return listOf(temperature, humidity, age, currentDate)
    }
}
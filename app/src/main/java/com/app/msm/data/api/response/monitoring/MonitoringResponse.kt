package com.app.msm.data.api.response.monitoring

import android.content.Context
import android.icu.util.LocaleData
import com.app.msm.R
import com.app.msm.extension.orDash
import com.app.msm.extension.orZero
import com.app.msm.model.monitoring.Monitor
import com.google.firebase.database.PropertyName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
            value = "${temperature.orDash()}C"
        )
        val humidity = Monitor(
            label = R.string.label_kelembapan,
            value = humidity.orDash()
        )
        val age = Monitor(
            label = R.string.label_age,
            value = "${age.orZero()} Hari"
        )
        val currentDate = Monitor(
            label = R.string.label_date,
            value = LocalDate.now().format(DateTimeFormatter.ofPattern(Monitor.DATE_PATTERN))
        )
        return listOf(temperature, humidity, age, currentDate)
    }
}
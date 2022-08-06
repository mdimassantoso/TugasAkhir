package com.app.msm.data.api.response.monitoring

import com.app.msm.R
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
            viewType = Monitor.ViewType.Gauge,
            dataType = Monitor.DataType.Temperature,
            unitMeasurement = Monitor.UnitMeasurement.Celcius
        )
        val humidity = Monitor(
            label = R.string.label_kelembapan,
            value = this.humidity.toString(),
            viewType = Monitor.ViewType.Gauge,
            dataType = Monitor.DataType.Humidity,
            unitMeasurement = Monitor.UnitMeasurement.Percent
        )
        val age = Monitor(
            label = R.string.label_age,
            value = this.age.orZero().toString(),
            viewType = Monitor.ViewType.DefaultSeparatedUnitMeasurement,
            dataType = Monitor.DataType.PlainText,
            unitMeasurement = Monitor.UnitMeasurement.Day
        )
        val currentDate = Monitor(
            label = R.string.label_date,
            value = LocalDate.now().format(DateTimeFormatter.ofPattern(Monitor.DATE_PATTERN)),
            viewType = Monitor.ViewType.Default,
            dataType = Monitor.DataType.PlainText,
            unitMeasurement = Monitor.UnitMeasurement.NoMeasurement
        )
        return listOf(temperature, humidity, age, currentDate)
    }
}
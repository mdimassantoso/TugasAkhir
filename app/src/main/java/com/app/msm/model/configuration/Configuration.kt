package com.app.msm.model.configuration

import android.os.Parcelable
import com.app.msm.data.api.request.ConfigurationRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configuration(
    val firstWateringSchedule: String,
    val secondWateringSchedule: String,
    val thirdWateringSchedule: String,
    val wateringDuration: Int,
    val humidityUpperLimit: Int,
    val humidityLowerLimit: Int,
    val temperatureUpperLimit: Int,
    val temperatureLowerLimit: Int
) : Parcelable {
    fun toConfigurationRequest(): ConfigurationRequest = ConfigurationRequest(
        firstWateringSchedule = firstWateringSchedule,
        secondWateringSchedule = secondWateringSchedule,
        thirdWateringSchedule = thirdWateringSchedule,
        wateringDuration = wateringDuration,
        humidityUpperLimit = humidityUpperLimit,
        humidityLowerLimit = humidityLowerLimit,
        temperatureUpperLimit = temperatureUpperLimit,
        temperatureLowerLimit = temperatureLowerLimit
    )
}

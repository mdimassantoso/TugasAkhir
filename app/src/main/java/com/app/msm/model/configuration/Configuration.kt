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

    fun isNotConfiguredYet(): Boolean = firstWateringSchedule.isEmpty() or
        secondWateringSchedule.isEmpty() or
        thirdWateringSchedule.isEmpty() or
        (wateringDuration == 0) or
        (temperatureUpperLimit == 0) or
        (temperatureLowerLimit == 0) or
        (humidityUpperLimit == 0) or
        (humidityLowerLimit == 0)

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

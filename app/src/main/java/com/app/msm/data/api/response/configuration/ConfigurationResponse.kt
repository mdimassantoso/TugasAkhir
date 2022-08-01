package com.app.msm.data.api.response.configuration

import com.app.msm.extension.orZero
import com.app.msm.model.configuration.Configuration
import com.google.firebase.database.PropertyName

data class ConfigurationResponse(

    @JvmField
    @PropertyName("penyiraman_1")
    val firstWateringSchedule: String? = null,

    @JvmField
    @PropertyName("penyiraman_2")
    val secondWateringSchedule: String? = null,

    @JvmField
    @PropertyName("penyiraman_3")
    val thirdWateringSchedule: String? = null,

    @JvmField
    @PropertyName("durasi_penyiraman")
    val wateringDuration: Int? = null,

    @JvmField
    @PropertyName("batas_atas_kelembapan")
    val humidityUpperLimit: Int? = null,

    @JvmField
    @PropertyName("batas_bawah_kelembapan")
    val humidityLowerLimit: Int? = null,

    @JvmField
    @PropertyName("batas_atas_suhu")
    val temperatureUpperLimit: Int? = null,

    @JvmField
    @PropertyName("batas_bawah_suhu")
    val temperatureLowerLimit: Int? = null

) {
    fun toConfiguration(): Configuration = Configuration(
        firstWateringSchedule = firstWateringSchedule.orEmpty(),
        secondWateringSchedule = secondWateringSchedule.orEmpty(),
        thirdWateringSchedule = thirdWateringSchedule.orEmpty(),
        wateringDuration = wateringDuration.orZero(),
        humidityUpperLimit = humidityUpperLimit.orZero(),
        humidityLowerLimit = humidityLowerLimit.orZero(),
        temperatureUpperLimit = temperatureUpperLimit.orZero(),
        temperatureLowerLimit = temperatureLowerLimit.orZero()
    )
}

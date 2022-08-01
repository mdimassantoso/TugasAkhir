package com.app.msm.data.api.request

import com.google.firebase.database.PropertyName

data class ConfigurationRequest(

    @JvmField
    @PropertyName("penyiraman_1")
    var firstWateringSchedule: String? = null,

    @JvmField
    @PropertyName("penyiraman_2")
    var secondWateringSchedule: String? = null,

    @JvmField
    @PropertyName("penyiraman_3")
    var thirdWateringSchedule: String? = null,

    @JvmField
    @PropertyName("durasi_penyiraman")
    var wateringDuration: Int? = null,

    @JvmField
    @PropertyName("batas_atas_kelembapan")
    var humidityUpperLimit: Int? = null,

    @JvmField
    @PropertyName("batas_bawah_kelembapan")
    var humidityLowerLimit: Int? = null,

    @JvmField
    @PropertyName("batas_atas_suhu")
    val temperatureUpperLimit: Int? = null,

    @JvmField
    @PropertyName("batas_bawah_suhu")
    val temperatureLowerLimit: Int? = null

)
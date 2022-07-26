package com.app.msm.data.api.response.monitoring

import com.google.firebase.database.PropertyName

data class MonitoredDataResponse(

    @JvmField
    @PropertyName("blower")
    val blower: MonitoredItemResponse? = null,

    @JvmField
    @PropertyName("kelembapan")
    val kelembapan: MonitoredItemResponse? = null,

    @JvmField
    @PropertyName("suhu")
    val suhu: MonitoredItemResponse? = null

)
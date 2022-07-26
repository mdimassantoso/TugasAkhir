package com.app.msm.data.api.response.controlling

import com.google.firebase.database.PropertyName

data class ControllingDataResponse(

    @JvmField
    @PropertyName("blower")
    val blower: ControllingItemResponse? = null,

    @JvmField
    @PropertyName("kelembapan")
    val kelembapan: ControllingItemResponse? = null,

    @JvmField
    @PropertyName("suhu")
    val suhu: ControllingItemResponse? = null,

    @JvmField
    @PropertyName("lampu")
    val lampu: ControllingItemResponse? = null

)
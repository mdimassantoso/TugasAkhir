package com.app.msm.data.api.response

import com.google.firebase.database.PropertyName

data class MonitoredItemResponse(

    @JvmField
    @PropertyName("value")
    val value: String? = null

)

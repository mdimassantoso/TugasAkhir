package com.app.msm.data.api.response.controlling

import com.app.msm.R
import com.app.msm.model.controlling.Control
import com.app.msm.ui.main.controlling.ControlType
import com.google.firebase.database.PropertyName

data class ControllingResponse(

    @JvmField
    @PropertyName("blower")
    val blower: Int? = null,

    @JvmField
    @PropertyName("lampu")
    val lamp: Int? = null,

    @JvmField
    @PropertyName("penyiraman_air")
    val watering: Int? = null

) {
    fun toControls(): List<Control> {
        val watering = Control(
            label = R.string.label_watering,
            type = ControlType.Switch(isChecked = watering == 1),
            icon = R.drawable.ic_showers
        )
        val blower = Control(
            label = R.string.label_blower,
            type = ControlType.Switch(isChecked = blower == 1),
            icon = R.drawable.ic_electric_fan
        )
        val lamp = Control(
            label = R.string.label_lampu,
            type = ControlType.Switch(isChecked = lamp == 1),
            icon = R.drawable.ic_lamp
        )
        val downloadImage = Control(
            label = R.string.label_view_image,
            type = ControlType.Button(isEnabled = true),
            icon = R.drawable.ic_download
        )
        return listOf(watering, blower, lamp, downloadImage)
    }
}
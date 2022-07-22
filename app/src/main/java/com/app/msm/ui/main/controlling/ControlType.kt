package com.app.msm.ui.main.controlling

sealed class ControlType {
    data class Switch(var isChecked: Boolean = false) : ControlType()
    data class Button(var isEnabled: Boolean = false) : ControlType()
}

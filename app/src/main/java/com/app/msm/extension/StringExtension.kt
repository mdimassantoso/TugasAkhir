package com.app.msm.extension

fun String?.orDash(): String = if (isNullOrEmpty()) "-" else this
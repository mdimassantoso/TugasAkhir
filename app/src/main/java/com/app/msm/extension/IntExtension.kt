package com.app.msm.extension

fun Int?.orZero(): Int = this ?: 0

fun Int?.toBoolean(): Boolean = this == 1

fun Int.addZeroPrefixIfNeeded(): String = if (this > 9 || this < 0) this.toString() else "0$this"

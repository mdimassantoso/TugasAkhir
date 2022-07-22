package com.app.msm.helper

import android.os.Build
import com.app.msm.BuildConfig

object Helper {
    fun isDebugBuild(): Boolean = BuildConfig.DEBUG
    fun isAndroidQ(): Boolean = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q
}

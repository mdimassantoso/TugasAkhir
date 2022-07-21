package com.app.msm.data.preferences

import android.content.Context
import android.content.SharedPreferences

object SharedPrefProvider {

    private const val APP_PREFERENCE = "app_preference"

    private lateinit var appPreferences: SharedPreferences

    val appPref: SharedPreferences?
        get() = if (::appPreferences.isInitialized) appPreferences else null

    fun initAppPreferences(context: Context) {
        if (::appPreferences.isInitialized) return
        appPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)
    }
}

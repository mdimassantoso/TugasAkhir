package com.app.msm.ui.splash

import androidx.lifecycle.ViewModel
import com.app.msm.data.preferences.SharedPrefKey
import com.app.msm.data.preferences.SharedPrefProvider

class SplashViewModel : ViewModel() {

    fun isLoggedIn(): Boolean = SharedPrefProvider.appPref?.run {
        getBoolean(SharedPrefKey.SESSION_IS_LOGIN, false)
    } ?: false
}

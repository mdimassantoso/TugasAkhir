package com.app.msm.ui.main.setting

import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.app.msm.data.preferences.SharedPrefKey
import com.app.msm.data.preferences.SharedPrefProvider

class SettingViewModel : ViewModel() {

    fun logout() {
        SharedPrefProvider.appPref?.edit {
            putBoolean(SharedPrefKey.SESSION_IS_LOGIN, false)
            putString(SharedPrefKey.SESSION_EMAIL, null)
        }
    }
}

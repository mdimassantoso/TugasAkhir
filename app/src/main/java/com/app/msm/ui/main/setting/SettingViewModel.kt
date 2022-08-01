package com.app.msm.ui.main.setting

import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.api.response.configuration.ConfigurationResponse
import com.app.msm.data.preferences.SharedPrefKey
import com.app.msm.data.preferences.SharedPrefProvider
import com.app.msm.model.configuration.Configuration
import com.app.msm.vo.ViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SettingViewModel : ViewModel() {

    private val _configurationViewState: Channel<ViewState<Configuration>> = Channel()
    val configurationViewState = _configurationViewState.receiveAsFlow()

    private val configurationReference: DatabaseReference by lazy { FirebaseProvider.configurationReference }

    private val configurationValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnDataChange(snapshot)
        override fun onCancelled(error: DatabaseError) {
            _configurationViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    private fun handleOnDataChange(snapshot: DataSnapshot) {
        try {
            snapshot.getValue(ConfigurationResponse::class.java)?.toConfiguration()?.let { configuration ->
                _configurationViewState.trySend(ViewState.Success(configuration))
            } ?: run {
                _configurationViewState.trySend(ViewState.Error(Exception("Failed to parse configuration")))
            }
        } catch (e: Exception) {
            _configurationViewState.trySend(ViewState.Error(e))
        }
    }

    fun listenToConfiguration() {
        _configurationViewState.trySend(ViewState.Loading)
        configurationReference.addValueEventListener(configurationValueListener)
    }

    fun removeConfigurationListener() {
        configurationReference.removeEventListener(configurationValueListener)
    }

    fun logout() {
        SharedPrefProvider.appPref?.edit {
            putBoolean(SharedPrefKey.SESSION_IS_LOGIN, false)
            putString(SharedPrefKey.SESSION_EMAIL, null)
        }
    }
}

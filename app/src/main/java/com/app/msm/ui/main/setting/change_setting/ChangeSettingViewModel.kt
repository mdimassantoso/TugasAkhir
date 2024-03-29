package com.app.msm.ui.main.setting.change_setting

import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.model.configuration.Configuration
import com.app.msm.vo.ViewState
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ChangeSettingViewModel : ViewModel() {

    private val _configurationViewState: Channel<ViewState<Configuration>> = Channel()
    val configurationViewState = _configurationViewState.receiveAsFlow()

    private val configurationReference: DatabaseReference by lazy { FirebaseProvider.configurationReference }
    private val autoConfigurationReference: DatabaseReference by lazy { FirebaseProvider.autoConfigReference }

    fun updateConfiguration(configuration: Configuration) {
        with(_configurationViewState) {
            trySend(ViewState.Loading)
            configurationReference
                .setValue(configuration.toConfigurationRequest())
                .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
                .addOnFailureListener { exception -> trySend(ViewState.Error(exception)) }
                .addOnSuccessListener {
                    enableAutoConfiguration()
                    trySend(ViewState.Success(configuration))
                }
        }
    }

    private fun enableAutoConfiguration() {
        autoConfigurationReference.setValue(1)
    }
}

package com.app.msm.ui.main.monitoring

import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.api.response.monitoring.MonitoringResponse
import com.app.msm.model.monitoring.Monitor
import com.app.msm.vo.ViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class MonitoringViewModel : ViewModel() {

    private val _monitoringViewState: Channel<ViewState<List<Monitor>>> = Channel()
    val monitoringViewState = _monitoringViewState.receiveAsFlow()

    private val monitoringReference: DatabaseReference by lazy { FirebaseProvider.monitoringReference }

    private val monitoredDataValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnDataChange(snapshot)
        override fun onCancelled(error: DatabaseError) {
            _monitoringViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    fun listenToMonitoring() {
        _monitoringViewState.trySend(ViewState.Loading)
        monitoringReference.addValueEventListener(monitoredDataValueListener)
    }

    fun removeMonitoringListener() {
        monitoringReference.removeEventListener(monitoredDataValueListener)
    }

    private fun handleOnDataChange(snapshot: DataSnapshot) {
        try {
            val monitors = snapshot.getValue(MonitoringResponse::class.java)?.toMonitors()
            _monitoringViewState.trySend(ViewState.Success(monitors.orEmpty()))
        } catch (e: Exception) {
            _monitoringViewState.trySend(ViewState.Error(e))
        }
    }
}

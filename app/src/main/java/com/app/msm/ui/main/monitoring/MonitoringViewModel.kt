package com.app.msm.ui.main.monitoring

import androidx.lifecycle.ViewModel
import com.app.msm.R
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.api.response.MonitoredDataResponse
import com.app.msm.model.Monitor
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

    private val monitoredDataReference: DatabaseReference by lazy {
        FirebaseProvider.firebaseDatabase.getReference("msm")
    }

    private val monitoredDataValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnDataChange(snapshot)

        override fun onCancelled(error: DatabaseError) {
            _monitoringViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    fun listenToMonitoredData() {
        _monitoringViewState.trySend(ViewState.Loading)
        monitoredDataReference.addValueEventListener(monitoredDataValueListener)
    }

    fun removeMonitoredDataListener() {
        monitoredDataReference.removeEventListener(monitoredDataValueListener)
    }

    private fun handleOnDataChange(snapshot: DataSnapshot) {
        try {
            val monitors = mutableListOf<Monitor>()
            snapshot.getValue(MonitoredDataResponse::class.java)?.let { response ->
                val blower = Monitor(
                    label = R.string.label_blower,
                    value = response.blower?.value.orEmpty()
                )
                val kelembapan = Monitor(
                    label = R.string.label_kelembapan,
                    value = response.kelembapan?.value.orEmpty()
                )
                val suhu = Monitor(
                    label = R.string.label_suhu,
                    value = response.suhu?.value.orEmpty()
                )
                monitors.addAll(listOf(blower, kelembapan, suhu))
            }
            _monitoringViewState.trySend(ViewState.Success(monitors))
        } catch (e: Exception) {
            _monitoringViewState.trySend(ViewState.Error(e))
        }
    }
}

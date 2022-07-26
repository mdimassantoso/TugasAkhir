package com.app.msm.ui.main.controlling

import androidx.lifecycle.ViewModel
import com.app.msm.R
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.api.response.controlling.ControllingDataResponse
import com.app.msm.extension.toInt
import com.app.msm.model.Control
import com.app.msm.vo.ViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ControllingViewModel : ViewModel() {

    private val _controllingActionState: Channel<ViewState<Boolean>> = Channel()
    val controllingActionState = _controllingActionState.receiveAsFlow()

    private val _controllingViewState: Channel<ViewState<List<Control>>> = Channel()
    val controllingViewState = _controllingViewState.receiveAsFlow()

    private val msmDataReference: DatabaseReference by lazy { FirebaseProvider.msmReference }

    fun updateControllingData(databaseReference: DatabaseReference, turnOn: Boolean) {
        with(_controllingActionState) {
            _controllingActionState.trySend(ViewState.Loading)
            databaseReference
                .setValue(turnOn.toInt())
                .addOnSuccessListener { trySend(ViewState.Success(turnOn)) }
                .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
                .addOnFailureListener { exception ->
                    trySend(ViewState.Error(exception))
                }
        }
    }

    private val controllingDataValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnDataChange(snapshot)

        override fun onCancelled(error: DatabaseError) {
            _controllingViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    fun listenToControllingData() {
        _controllingViewState.trySend(ViewState.Loading)
        msmDataReference.addValueEventListener(controllingDataValueListener)
    }

    fun removeControllingDataListener() {
        msmDataReference.removeEventListener(controllingDataValueListener)
    }

    private fun handleOnDataChange(snapshot: DataSnapshot) {
        try {
            val controls = mutableListOf<Control>()
            snapshot.getValue(ControllingDataResponse::class.java)?.let { response ->
                val blower = Control(
                    label = R.string.label_blower,
                    type = ControlType.Switch(isChecked = response.blower?.status == 1),
                    icon = R.drawable.ic_electric_fan
                )
                val kelembapan = Control(
                    label = R.string.label_kelembapan,
                    type = ControlType.Switch(isChecked = response.kelembapan?.status == 1),
                    icon = R.drawable.ic_showers
                )
                val suhu = Control(
                    label = R.string.label_suhu,
                    type = ControlType.Switch(isChecked = response.suhu?.status == 1),
                    icon = R.drawable.ic_foggy_fog
                )
                val lampu = Control(
                    label = R.string.label_lampu,
                    type = ControlType.Switch(isChecked = response.lampu?.status == 1),
                    icon = R.drawable.ic_lamp
                )
                val download = Control(
                    label = R.string.label_download_laporan,
                    type = ControlType.Button(isEnabled = true),
                    icon = R.drawable.ic_download
                )
                controls.addAll(listOf(blower, kelembapan, suhu, lampu, download))
            }
            _controllingViewState.trySend(ViewState.Success(controls))
        } catch (e: Exception) {
            _controllingViewState.trySend(ViewState.Error(e))
        }
    }
}

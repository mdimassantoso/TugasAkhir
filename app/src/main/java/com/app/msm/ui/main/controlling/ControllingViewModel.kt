package com.app.msm.ui.main.controlling

import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.api.response.controlling.ControllingResponse
import com.app.msm.extension.toBoolean
import com.app.msm.extension.toInt
import com.app.msm.model.controlling.Control
import com.app.msm.vo.ViewState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ControllingViewModel : ViewModel() {

    private val _automaticSwitchViewState: Channel<ViewState<Boolean>> = Channel()
    val automaticSwitchViewState = _automaticSwitchViewState.receiveAsFlow()

    private val _automaticSwitchActionState: Channel<ViewState<Boolean>> = Channel()
    val automaticSwitchActionState = _automaticSwitchActionState.receiveAsFlow()

    private val _controllingActionState: Channel<ViewState<Boolean>> = Channel()
    val controllingActionState = _controllingActionState.receiveAsFlow()

    private val _controllingViewState: Channel<ViewState<List<Control>>> = Channel()
    val controllingViewState = _controllingViewState.receiveAsFlow()

    private val controllingReference: DatabaseReference by lazy { FirebaseProvider.controllingReference }
    private val autoConfigReference: DatabaseReference by lazy { FirebaseProvider.autoConfigReference }

    /*
        Controlling - Action
     */

    fun updateControllingData(databaseReference: DatabaseReference, turnOn: Boolean) {
        with(_controllingActionState) {
            trySend(ViewState.Loading)
            databaseReference
                .setValue(turnOn.toInt())
                .addOnSuccessListener { trySend(ViewState.Success(turnOn)) }
                .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
                .addOnFailureListener { exception ->
                    trySend(ViewState.Error(exception))
                }
        }
    }

    /*
        Controlling - View
     */

    private val controllingValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnControllingChange(snapshot)
        override fun onCancelled(error: DatabaseError) {
            _controllingViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    fun listenToControlling() {
        _controllingViewState.trySend(ViewState.Loading)
        controllingReference.addValueEventListener(controllingValueListener)
    }

    fun removeControllingListener() {
        controllingReference.removeEventListener(controllingValueListener)
    }

    private fun handleOnControllingChange(snapshot: DataSnapshot) {
        try {
            val controls = snapshot.getValue(ControllingResponse::class.java)?.toControls()
            _controllingViewState.trySend(ViewState.Success(controls.orEmpty()))
        } catch (e: Exception) {
            _controllingViewState.trySend(ViewState.Error(e))
        }
    }

    /*
        Automatic Configuration - Action
     */

    fun updateAutomaticConfiguration(turnOn: Boolean) {
        with(_automaticSwitchActionState) {
            trySend(ViewState.Loading)
            autoConfigReference
                .setValue(turnOn.toInt())
                .addOnSuccessListener { trySend(ViewState.Success(turnOn)) }
                .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
                .addOnFailureListener { exception ->
                    trySend(ViewState.Error(exception))
                }
        }
    }

    /*
        Automatic Configuration - View
     */

    private val autoConfigValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) = handleOnAutoConfigChange(snapshot)
        override fun onCancelled(error: DatabaseError) {
            _controllingViewState.trySend(ViewState.Error(error.toException()))
        }
    }

    private fun handleOnAutoConfigChange(snapshot: DataSnapshot) {
        try {
            val autoConfig = snapshot.getValue(Int::class.java).toBoolean()
            _automaticSwitchViewState.trySend(ViewState.Success(autoConfig))
        } catch (e: Exception) {
            _automaticSwitchViewState.trySend(ViewState.Error(e))
        }
    }

    fun listenToAutoConfig() {
        _automaticSwitchViewState.trySend(ViewState.Loading)
        autoConfigReference.addValueEventListener(autoConfigValueListener)
    }

    fun removeAutoConfigListener() {
        autoConfigReference.removeEventListener(autoConfigValueListener)
    }
}

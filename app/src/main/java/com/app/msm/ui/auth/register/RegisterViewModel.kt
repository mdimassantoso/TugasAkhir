package com.app.msm.ui.auth.register

import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.vo.ViewState
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RegisterViewModel : ViewModel() {

    private val _registerViewState: Channel<ViewState<String>> = Channel()
    val registerViewState = _registerViewState.receiveAsFlow()

    fun register(
        email: String,
        password: String
    ) = with(_registerViewState) {
        trySend(ViewState.Loading)
        FirebaseProvider.firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
            .addOnFailureListener { exception -> trySend(ViewState.Error(exception)) }
            .addOnSuccessListener { authResult: AuthResult? ->
                authResult?.user?.let { user ->
                    trySend(ViewState.Success(user.email.orEmpty()))
                } ?: run {
                    trySend(ViewState.Error(Throwable()))
                }
            }
    }
}

package com.app.msm.ui.auth.login

import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.data.preferences.SharedPrefKey
import com.app.msm.data.preferences.SharedPrefProvider
import com.app.msm.vo.ViewState
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel : ViewModel() {

    private val _loginViewState: Channel<ViewState<String>> = Channel()
    val loginViewState = _loginViewState.receiveAsFlow()

    fun login(
        email: String,
        password: String
    ) = with(_loginViewState) {
        trySend(ViewState.Loading)
        FirebaseProvider.firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCanceledListener { trySend(ViewState.Error(CancellationException())) }
            .addOnFailureListener { exception -> trySend(ViewState.Error(exception)) }
            .addOnSuccessListener { authResult: AuthResult? ->
                authResult?.user?.email?.let { email ->
                    saveSessionCredential(email)
                    trySend(ViewState.Success(email))
                } ?: run {
                    trySend(ViewState.Error(Throwable()))
                }
            }
    }

    private fun saveSessionCredential(email: String) {
        SharedPrefProvider.appPref?.edit {
            putBoolean(SharedPrefKey.SESSION_IS_LOGIN, true)
            putString(SharedPrefKey.SESSION_EMAIL, email)
        }
    }
}

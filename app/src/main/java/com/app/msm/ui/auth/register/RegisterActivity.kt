package com.app.msm.ui.auth.register

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.msm.databinding.ActivityRegisterBinding
import com.app.msm.extension.openActivity
import com.app.msm.extension.showSnackBar
import com.app.msm.ui.auth.login.LoginActivity
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserveResult()
        initListener()
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerViewState.collect { viewState ->
                    handleRegisterViewState(viewState)
                }
            }
        }
    }

    private fun handleRegisterViewState(viewState: ViewState<String>) {
        when (viewState) {
            is ViewState.Loading -> showLoading(true)
            is ViewState.Error -> {
                showLoading(false)
                showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                showLoading(false)
                navigateToLogin(clearTask = true)
            }
        }
    }

    private fun showSnackBar(message: String) {
        binding.root.showSnackBar(message)
    }

    private fun showLoading(show: Boolean) = with(binding) {
        btnLogin.isEnabled = show.not()
        btnRegister.isEnabled = show.not()
        pbLoading.isVisible = show
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            navigateToLogin(singleTop = true)
        }

        btnRegister.setOnClickListener {
            processRegister()
        }
    }

    private fun navigateToLogin(singleTop: Boolean = true, clearTask: Boolean = false) {
        LoginActivity.start(
            context = this@RegisterActivity,
            singleTop = singleTop,
            clearTask = clearTask
        )
    }

    private fun processRegister() = with(binding) {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        when {
            email.isEmpty() -> root.showSnackBar("Email cannot be empty")
            password.isEmpty() -> root.showSnackBar("Password cannot be empty")
            else -> viewModel.register(email, password)
        }
    }

    companion object {
        fun start(
            context: Context,
            clearTask: Boolean = false,
            singleTop: Boolean = false
        ) {
            context.openActivity(
                destination = RegisterActivity::class.java,
                clearTask = clearTask,
                singleTop = singleTop
            )
        }
    }
}

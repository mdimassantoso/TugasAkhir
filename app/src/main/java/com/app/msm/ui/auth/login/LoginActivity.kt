package com.app.msm.ui.auth.login

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.msm.databinding.ActivityLoginBinding
import com.app.msm.extension.openActivity
import com.app.msm.extension.showSnackBar
import com.app.msm.ui.auth.register.RegisterActivity
import com.app.msm.ui.main.MainActivity
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserveResult()
        initListener()
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginViewState.collect { viewState ->
                    handleLoginViewState(viewState)
                }
            }
        }
    }

    private fun handleLoginViewState(viewState: ViewState<String>) {
        when (viewState) {
            is ViewState.Loading -> showLoading(true)
            is ViewState.Error -> showSnackBar(viewState.e.message.orEmpty())
            is ViewState.Success -> {
                showLoading(false)
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        MainActivity.start(this@LoginActivity, clearTask = true)
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
            processLogin()
        }

        btnRegister.setOnClickListener {
            RegisterActivity.start(this@LoginActivity, singleTop = true)
        }
    }

    private fun processLogin() = with(binding) {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        viewModel.login(email, password)
    }

    companion object {
        fun start(
            context: Context,
            clearTask: Boolean = false,
            singleTop: Boolean = false
        ) {
            context.openActivity(
                destination = LoginActivity::class.java,
                clearTask = clearTask,
                singleTop = singleTop
            )
        }
    }
}

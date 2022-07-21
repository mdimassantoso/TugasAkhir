package com.app.msm.ui.auth.login

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.msm.databinding.ActivityLoginBinding
import com.app.msm.extension.openActivity
import com.app.msm.ui.auth.register.RegisterActivity
import com.app.msm.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            // TODO process login & redirect to main with clear task
            MainActivity.start(this@LoginActivity, clearTask = true)
        }

        btnRegister.setOnClickListener {
            RegisterActivity.start(this@LoginActivity, singleTop = true)
        }
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

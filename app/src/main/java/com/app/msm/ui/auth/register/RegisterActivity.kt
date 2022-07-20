package com.app.msm.ui.auth.register

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.msm.databinding.ActivityRegisterBinding
import com.app.msm.extension.openActivity
import com.app.msm.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            LoginActivity.start(this@RegisterActivity, singleTop = true)
        }

        btnRegister.setOnClickListener {
            // TODO process register & redirect to login with clear task
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

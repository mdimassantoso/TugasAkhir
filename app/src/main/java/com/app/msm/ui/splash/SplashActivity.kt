package com.app.msm.ui.splash

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.msm.R
import com.app.msm.helper.Helper
import com.app.msm.ui.auth.login.LoginActivity
import com.app.msm.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        initSplashScreenDelay()
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition { true }
        }
    }

    private fun initSplashScreenDelay() {
        Handler(mainLooper).postDelayed(SPLASH_SCREEN_DELAY) {
            handleRedirection()
        }
    }

    private fun handleRedirection() {
        if (viewModel.isLoggedIn()) {
            navigateToMain()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        LoginActivity.start(this)
    }

    private fun navigateToMain() {
        MainActivity.start(this)
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY = 2000L
    }
}

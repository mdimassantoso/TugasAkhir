package com.app.msm.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.msm.R
import com.app.msm.databinding.ActivityMainBinding
import com.app.msm.extension.openActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
    }

    private fun initNavController() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment
        val navHostController = navHost.navController

        navHostController.addOnDestinationChangedListener { _, destination, _ ->
            handleAppTitle(destination.label.toString())
            handleBottomNavBarVisibility(destination.id)
        }
        binding.bnvMain.setupWithNavController(navHostController)
    }

    private fun handleAppTitle(label: String) {
        binding.tvAppTitle.text = label
    }

    private fun handleBottomNavBarVisibility(fragmentId: Int) {
        binding.bnvMain.isVisible = getMainFragments().contains(fragmentId)
    }

    private fun getMainFragments(): List<Int> = listOf(
        R.id.monitoringFragment,
        R.id.controllingFragment,
        R.id.settingFragment
    )

    companion object {
        fun start(context: Context, clearTask: Boolean = false) {
            context.openActivity(
                destination = MainActivity::class.java,
                clearTask = clearTask
            )
        }
    }
}

package com.app.msm.ui.main.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.msm.R
import com.app.msm.databinding.FragmentSettingBinding
import com.app.msm.helper.viewBinding
import com.app.msm.ui.auth.login.LoginActivity

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding(FragmentSettingBinding::bind)

    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() = with(binding) {
        btnAction.setOnClickListener {

        }

        btnLogout.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        LoginActivity.start(requireContext(), clearTask = true)
    }
}

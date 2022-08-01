package com.app.msm.ui.main.setting.change_setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.msm.R
import com.app.msm.databinding.FragmentChangeSettingBinding
import com.app.msm.helper.viewBinding

class ChangeSettingFragment : Fragment(R.layout.fragment_change_setting) {

    private val binding by viewBinding(FragmentChangeSettingBinding::bind)

    private val viewModel: ChangeSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() = with(binding) {
        btnSave.setOnClickListener {
            /* TODO save configuration */
        }
    }
}

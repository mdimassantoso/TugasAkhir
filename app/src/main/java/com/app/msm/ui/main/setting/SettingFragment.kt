package com.app.msm.ui.main.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.msm.R
import com.app.msm.databinding.FragmentSettingBinding
import com.app.msm.helper.viewBinding

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding(FragmentSettingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

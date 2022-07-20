package com.app.msm.ui.main.controlling

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.msm.R
import com.app.msm.databinding.FragmentControllingBinding
import com.app.msm.helper.viewBinding

class ControllingFragment : Fragment(R.layout.fragment_controlling) {

    private val binding by viewBinding(FragmentControllingBinding::bind)

    private val controllingAdapter by lazy {
        ControllingAdapter { control -> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

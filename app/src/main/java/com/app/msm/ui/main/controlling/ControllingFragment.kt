package com.app.msm.ui.main.controlling

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.msm.R
import com.app.msm.databinding.FragmentControllingBinding
import com.app.msm.helper.viewBinding
import com.app.msm.model.Control
import com.app.msm.ui.auth.login.LoginViewModel

class ControllingFragment : Fragment(R.layout.fragment_controlling) {

    private val binding by viewBinding(FragmentControllingBinding::bind)

    private val viewModel: ControllingViewModel by viewModels()

    private val controllingAdapter by lazy {
        ControllingAdapter(
            onSwitchChecked = { control ->

            },
            onButtonClicked = { control ->

            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initDummyData()
    }

    private fun initDummyData() {
        val listOfControl = listOf(
            Control(
                id = 1,
                label = R.string.label_kelembapan,
                icon = R.drawable.ic_showers,
                type = ControlType.Switch(isChecked = false)
            ),
            Control(
                id = 2,
                label = R.string.label_suhu,
                icon = R.drawable.ic_foggy_fog,
                type = ControlType.Switch(isChecked = false)
            ),
            Control(
                id = 3,
                label = R.string.label_download_laporan,
                icon = R.drawable.ic_download,
                type = ControlType.Button(isEnabled = true)
            )
        )
        controllingAdapter.submitList(listOfControl)
    }

    private fun initRecyclerView() {
        binding.rvControlling.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = controllingAdapter
        }
    }

}

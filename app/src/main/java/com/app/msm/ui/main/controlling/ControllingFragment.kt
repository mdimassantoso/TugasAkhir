package com.app.msm.ui.main.controlling

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.msm.R
import com.app.msm.data.api.FirebaseProvider
import com.app.msm.databinding.FragmentControllingBinding
import com.app.msm.extension.showSnackBar
import com.app.msm.helper.Helper
import com.app.msm.helper.viewBinding
import com.app.msm.model.controlling.Control
import com.app.msm.ui.preview_image.PreviewImageDialog
import com.app.msm.vo.ViewState
import kotlinx.coroutines.launch

class ControllingFragment : Fragment(R.layout.fragment_controlling), CompoundButton.OnCheckedChangeListener {

    private val binding by viewBinding(FragmentControllingBinding::bind)

    private val viewModel: ControllingViewModel by viewModels()

    private val controllingAdapter by lazy {
        ControllingAdapter(
            onSwitchChecked = { id, isChecked -> onSwitchChanged(id, isChecked) },
            onButtonClicked = { id -> onControllingChanged(id) },
            onAutoConfigEnabled = { binding.root.showSnackBar("Tidak dapat mengubah, otomatis sedang dinyalakan") }
        )
    }

    private fun onSwitchChanged(id: Int, isChecked: Boolean) {
        val reference = when (id) {
            R.string.label_watering -> FirebaseProvider.wateringControlReference
            R.string.label_blower -> FirebaseProvider.blowerControlReference
            R.string.label_lampu -> FirebaseProvider.lampControlReference
            else -> return
        }
        viewModel.updateControllingData(reference, isChecked)
    }

    private fun onControllingChanged(id: Int) {
        when (id) {
            R.string.label_view_image -> showImagePreview()
            else -> return
        }
    }

    private fun showImagePreview() {
        viewModel.getCameraImageUrl(
            onSuccess = { url ->
                PreviewImageDialog.newInstance(url).also { dialog ->
                    dialog.show(childFragmentManager, PreviewImageDialog.TAG)
                }
            },
            onError = { throwable ->
                binding.root.showSnackBar(throwable.message.orEmpty())
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserveResult()
        initListener()
        listenToFirebase()
    }

    private fun listenToFirebase() {
        viewModel.listenToAutoConfig()
        viewModel.listenToControlling()
    }

    private fun initListener() = with(binding) {
        swAutomatic.setOnCheckedChangeListener(this@ControllingFragment)
    }

    private fun onAutoConfigurationCheckedListener(isChecked: Boolean) {
        viewModel.getConfiguration(
            onSuccess = { configuration ->
                if (configuration.isNotConfiguredYet()) {
                    setAutoConfigSwitch(isChecked.not())
                    Helper.showAlertDialog(
                        context = requireContext(),
                        title = "Warning",
                        message = "Tidak dapat mengaktifkan, Konfigurasi belum di set",
                        positiveButtonText = "OK"
                    )
                } else {
                    viewModel.updateAutomaticConfiguration(isChecked)
                }
            },
            onError = { setAutoConfigSwitch(isChecked.not()) }
        )
    }

    private fun setAutoConfigSwitch(checked: Boolean) {
        binding.swAutomatic.apply {
            setOnCheckedChangeListener(null)
            isChecked = checked
            setOnCheckedChangeListener(this@ControllingFragment)
        }
    }

    private fun initObserveResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.controllingViewState.collect { viewState ->
                    handleControllingViewState(viewState)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.controllingActionState.collect { actionState ->
                    handleControllingActionState(actionState)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.automaticSwitchViewState.collect { viewState ->
                    handleAutomaticViewState(viewState)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.automaticSwitchActionState.collect { actionState ->
                    handleAutomaticActionState(actionState)
                }
            }
        }
    }

    private fun handleAutomaticActionState(actionState: ViewState<Boolean>) = with(binding) {
        swAutomatic.isEnabled = actionState !is ViewState.Loading
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                val message = if (actionState.data) "Berhasil mengaktifkan" else "Berhasil mematikan"

                root.showSnackBar(message)
            }
        }
    }

    private fun handleAutomaticViewState(viewState: ViewState<Boolean>) = with(binding) {
        swAutomatic.isEnabled = viewState !is ViewState.Loading
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                controllingAdapter.isAutoConfigEnable = viewState.data
                setAutoConfigSwitch(viewState.data)
            }
        }
    }

    private fun handleControllingActionState(actionState: ViewState<Boolean>) = with(binding) {
        controllingAdapter.isDisableAction = actionState is ViewState.Loading
        when (actionState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                root.showSnackBar(actionState.e.message.orEmpty())
            }
            is ViewState.Success -> {
                val message =
                    if (actionState.data) "Berhasil mengaktifkan" else "Berhasil mematikan"
                root.showSnackBar(message)
            }
        }
    }

    private fun handleControllingViewState(viewState: ViewState<List<Control>>) {
        when (viewState) {
            is ViewState.Loading -> Unit
            is ViewState.Error -> {
                binding.root.showSnackBar(viewState.e.message.orEmpty())
            }
            is ViewState.Success -> controllingAdapter.submitList(viewState.data)
        }
    }

    override fun onDestroyView() {
        removeFirebaseListener()
        super.onDestroyView()
    }

    private fun removeFirebaseListener() {
        viewModel.removeAutoConfigListener()
        viewModel.removeControllingListener()
    }

    private fun initRecyclerView() {
        binding.rvControlling.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = controllingAdapter
            itemAnimator = null
        }
    }

    override fun onCheckedChanged(switch: CompoundButton, isChecked: Boolean) {
        onAutoConfigurationCheckedListener(isChecked)
    }
}

package com.app.msm.ui.preview_image

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import coil.load
import com.app.msm.R
import com.app.msm.databinding.DialogImagePreviewBinding
import com.app.msm.helper.viewBinding

class PreviewImageDialog : DialogFragment(R.layout.dialog_image_preview) {

    private val binding by viewBinding(DialogImagePreviewBinding::bind)

    private lateinit var imageUrl: String

    override fun getTheme(): Int = R.style.FullscreenDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        processArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        bindData()
    }

    private fun bindData() {
        binding.myZoomageView.load(imageUrl)
    }

    private fun processArguments() {
        imageUrl = arguments?.getString(EXTRA_URL).orEmpty()
    }

    private fun initListener() = with(binding) {
        btnClose.setOnClickListener { dismiss() }
    }

    companion object {
        private const val EXTRA_URL = "extra_url"
        const val TAG = "tag_preview_image_dialog"
        fun newInstance(
            url: String
        ): PreviewImageDialog = PreviewImageDialog().apply {
            arguments = bundleOf(EXTRA_URL to url)
        }
    }
}

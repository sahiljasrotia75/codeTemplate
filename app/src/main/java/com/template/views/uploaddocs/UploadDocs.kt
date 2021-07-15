package com.template.views.uploaddocs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.UploadDocsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadDocs : Fragment(R.layout.upload_docs) {

    lateinit var binding: UploadDocsBinding
    private val viewModel: UploadDocsVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UploadDocsBinding.bind(view)
        binding.vm = viewModel
    }

}
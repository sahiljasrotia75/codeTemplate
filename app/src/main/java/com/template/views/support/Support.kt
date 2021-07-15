package com.template.views.support

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.SupportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Support : Fragment(R.layout.support) {

    lateinit var binding: SupportBinding
    private val viewModel: SupportVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SupportBinding.bind(view)
        binding.vm = viewModel
    }

}
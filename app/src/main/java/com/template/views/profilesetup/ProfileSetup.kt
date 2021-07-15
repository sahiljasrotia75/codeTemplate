package com.template.views.profilesetup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.ProfileSetupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetup : Fragment(R.layout.profile_setup) {
    private var binding: ProfileSetupBinding? = null
    private val viewModel: ProfileSetupVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bundle = arguments
        viewModel.setData()
        binding = ProfileSetupBinding.bind(view)
        binding?.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        setBottomSheet()
    }

    private fun setBottomSheet() {

    }

}
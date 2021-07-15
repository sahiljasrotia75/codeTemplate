package com.template.views.vehicleregistration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.VehicleRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleRegistration : Fragment(R.layout.vehicle_registration) {

    lateinit var binding: VehicleRegistrationBinding
    private val viewModel: VehicleRegistrationVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = VehicleRegistrationBinding.bind(view)
        binding.vm = viewModel
    }
}
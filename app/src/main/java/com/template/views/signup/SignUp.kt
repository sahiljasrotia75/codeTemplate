package com.dignitycustomer.views.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.SignupBinding
import com.template.views.signup.SignUpVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment(R.layout.signup) {
    private var binding: SignupBinding? = null
    private val viewModel: SignUpVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignupBinding.bind(view)
        binding?.vm = viewModel
    }
}
package com.template.views.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.ForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassword : Fragment(R.layout.forgot_password) {
    private var binding: ForgotPasswordBinding? = null
    private val viewModel: ForgotPasswordVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForgotPasswordBinding.bind(view)
        binding?.viewModel = viewModel
        setListener()
    }

    override fun onResume() {
        super.onResume()
        setListener()
    }

    private fun setListener() {
        MainActivity.navListener?.isLockDrawer(true)
    }
}
package com.template.views.otpverification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.OtpVerificationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class OtpVerification : Fragment(R.layout.otp_verification) {

    private var binding: OtpVerificationBinding? = null
    private val viewModel: OtpVerificationVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bundle = arguments
        binding = OtpVerificationBinding.bind(view)
        binding?.vm = viewModel
        viewModel.rootView = WeakReference(binding?.root)
    }

    override fun onResume() {
        super.onResume()
        setNavigation()
    }

    private fun setNavigation() {
        MainActivity.navListener?.isLockDrawer(true)
    }
}
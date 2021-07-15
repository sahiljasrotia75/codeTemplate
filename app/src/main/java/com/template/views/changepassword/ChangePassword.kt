package com.template.views.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.ChangepasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassword : Fragment(R.layout.changepassword) {

    var binding: ChangepasswordBinding? = null
    val viewModel: ChangePasswordVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bundle = arguments
        viewModel.isForgotPassword.set(requireArguments().containsKey("verificationToken"))
        binding = ChangepasswordBinding.bind(view)
        binding?.vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        setNavigationListener()
    }

    private fun setNavigationListener() {
        MainActivity.navListener?.isLockDrawer(false)
    }

}
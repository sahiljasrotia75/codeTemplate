package com.template.views.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.R
import com.template.databinding.NotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notifications : Fragment(R.layout.notifications) {
    private var binding: NotificationsBinding? = null
    private val viewModel: NotificationsVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NotificationsBinding.bind(view)
        binding?.vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        setNavigation()
    }

    private fun setNavigation() {

    }

}
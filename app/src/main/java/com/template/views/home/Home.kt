package com.template.views.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.HomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment(R.layout.home) {

    private var binding: HomeBinding? = null
    private val viewModel: HomeVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeBinding.bind(view)
        binding?.vm = viewModel
        setListener()
    }

    override fun onResume() {
        super.onResume()
        setListener()
    }

    private fun setListener() {
        MainActivity.navListener?.isLockDrawer(false)
    }

}
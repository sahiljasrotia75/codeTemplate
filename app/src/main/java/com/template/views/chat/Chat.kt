package com.template.views.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.ChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Chat : Fragment(R.layout.chat) {

    private var binding: ChatBinding? = null
    private val viewModel: ChatVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatBinding.bind(view)
        binding?.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        setNavListener()
    }

    private fun setNavListener() {
        MainActivity.navListener?.isLockDrawer(true)
    }
}
package com.template.views.contactus

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.template.MainActivity
import com.template.R
import com.template.databinding.ContactUsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUs : Fragment(R.layout.contact_us) {

    private var binding: ContactUsBinding? = null
    private val viewModel: ContactUsVM by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContactUsBinding.bind(view)
        binding?.vm = viewModel
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
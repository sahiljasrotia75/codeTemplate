package com.template.views.welcome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.template.R
import com.template.databinding.WelcomeBinding
import com.template.utils.navigateWithId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Welcome : Fragment(R.layout.welcome) {

    lateinit var binding: WelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WelcomeBinding.bind(view)

        navigateUpto()

    }


    /**
     * Navigate to using Handler
     * */
    private fun navigateUpto() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.root.navigateWithId(R.id.action_welcome_to_home2)
        }, 1000)
    }
}
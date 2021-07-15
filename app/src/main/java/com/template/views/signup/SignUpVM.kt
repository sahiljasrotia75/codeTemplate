package com.template.views.signup

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.models.SignUpData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import com.template.validations.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignUpVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    val email by lazy { ObservableField("") }
    val phone by lazy { ObservableField("") }
    val countryCode by lazy { ObservableField("91") }
    val password by lazy { ObservableField("") }
    val confirmPassword by lazy { ObservableField("") }

    fun onClick(view: View) {

        when (view.id) {

            R.id.back -> {
                view.navigateBack()
            }

            R.id.tvGotoSignIn -> {
                view.navigateBack()
            }

            R.id.btSignUp -> {
                val context = MainActivity.context.get()
                val validation = Validation.create(MainActivity.context.get()).apply {
                    isEmpty(email.get(), context?.getString(R.string.enter_email) ?: "")
                    isEmailValid(email.get(), context?.getString(R.string.enter_valid_email) ?: "")
                    isEmpty(
                        countryCode.get(),
                        context?.getString(R.string.select_country_code) ?: ""
                    )
                    isEmpty(phone.get(), context?.getString(R.string.enter_phone) ?: "")
                    isPhoneValid(phone.get(), context?.getString(R.string.enter_valid_phone) ?: "")
                    isEmpty(password.get(), context?.getString(R.string.enter_password) ?: "")
                    isValidPassword(
                        password.get(),
                        context?.getString(R.string.strong_password) ?: ""
                    )
                    isEmpty(
                        confirmPassword.get(),
                        context?.getString(R.string.enter_confirm_password) ?: ""
                    )
                    areEqual(
                        confirmPassword.get(),
                        password.get(),
                        context?.getString(R.string.password_does_not_match) ?: ""
                    )
                }
                if (validation.isValid()) {
                    hitSignUp(view)
                }
            }
        }
    }

    private fun hitSignUp(view: View) = viewModelScope.launch {
        repository.makeCall(apiKey = ApiEnums.SIGNUP,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<SignUpData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<SignUpData> {
                    return retrofitApi.signUp(
                        email = email.get()?.trim(),
                        countryCode = countryCode.get()?.trim(),
                        phone = phone.get()?.trim(),
                        password = password.get()?.trim(),
                        verificationType = 0
                    )
                }

                override fun onResponse(res: Response<SignUpData>) {
                    val bundle = Bundle().apply {
                        putString("type", "signUp")
                        putSerializable("signUpData", res.body())
                    }
                    view.navigateWithId(R.id.action_signUp_to_otpVerification, bundle)
                }

            })
    }

}
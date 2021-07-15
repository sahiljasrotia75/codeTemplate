package com.template.views.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.models.ForgotPasswordData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import com.template.utils.showNegativeAlerter
import com.template.validations.ValidatorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {

    val username by lazy { ObservableField("") }
    fun clickBack(view: View) {
        view.navigateBack()
    }

    fun submit(view: View) {
        val context = MainActivity.context.get()
        when {
            username.get()?.trim()?.isEmpty() == true ->
                context?.showNegativeAlerter(context.getString(R.string.enter_email_or_password))

            !ValidatorUtils.isEmailValid(username.get() ?: "") &&
                    !ValidatorUtils.isMobileValid(username.get() ?: "") ->
                context?.showNegativeAlerter(context.getString(R.string.enter_valid_email_or_password))
            else ->
                forgotPassword(view)

        }
    }

    private fun forgotPassword(view: View?) = viewModelScope.launch {
        repository.makeCall(
            apiKey = ApiEnums.LOGIN,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<ForgotPasswordData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<ForgotPasswordData> {
                    return retrofitApi.forgotPassword(
                        key = username.get()?.trim()
                    )
                }

                override fun onResponse(res: Response<ForgotPasswordData>) {
                    val bundle = Bundle().apply {
                        putString("key", username.get()?.trim())
                    }
                    view?.navigateWithId(R.id.action_forgotPassword_to_otpVerification, bundle)
                }
            }
        )
    }

}
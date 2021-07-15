package com.template.views.otpverification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukesh.mukeshotpview.completeListener.MukeshOtpCompleteListener
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.models.SignUpData
import com.template.models.TokenData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import com.template.utils.showPositiveAlerter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class OtpVerificationVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    var bundle: Bundle? = null
    var rootView: WeakReference<View>? = null

    val otpListener by lazy {
        object : MukeshOtpCompleteListener {
            override fun otpCompleteListener(otp: String?) {
                verifyOtp(view = rootView?.get(), otp = otp)
            }
        }
    }

    fun clickBack(view: View) {
        view.navigateBack()
    }

    fun resend(view: View) {
        if (bundle?.containsKey("signUpData") == true) {
            resendOtp(view = view)
        } else
            view.navigateBack()
    }

    private fun verifyOtp(view: View?, otp: String?) = viewModelScope.launch {
        val isSignUp = bundle?.containsKey("signUpData") ?: false
        val key = if (isSignUp) {
            val signUpData: SignUpData? = bundle?.getSerializable("signUpData") as SignUpData?
            signUpData?.phone
        } else {
            bundle?.getString("key")
        }
        repository.makeCall(
            apiKey = ApiEnums.VERIFY_OTP,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<TokenData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<TokenData> {
                    return retrofitApi.verifyOtp(key = key, code = otp)
                }

                override fun onResponse(res: Response<TokenData>) {
                    if (isSignUp) {
                        val signUpData: SignUpData? =
                            bundle?.getSerializable("signUpData") as SignUpData?
                        val bundle = Bundle().apply {
                            putString("type", "signUp")
                            putSerializable("signUpData", signUpData)
                            putSerializable("verificationToken", res.body())
                        }
                        view?.navigateWithId(R.id.action_otpVerification_to_profileSetup, bundle)
                    } else {
                        val bundle = Bundle().apply {
                            putSerializable("verificationToken", res.body())
                        }
                        view?.navigateWithId(R.id.action_otpVerification_to_changePassword, bundle)
                    }

                }

            }
        )
    }

    private fun resendOtp(view: View?) = viewModelScope.launch {
        val signUpData: SignUpData? = bundle?.getSerializable("signUpData") as SignUpData?
        repository.makeCall(
            apiKey = ApiEnums.RESEND_OTP,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<TokenData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<TokenData> {
                    return retrofitApi.resendOtp(phone = signUpData?.phone, id = signUpData?._id)
                }

                override fun onResponse(res: Response<TokenData>) {
                    val context = MainActivity.context.get()
                    context?.showPositiveAlerter(context.getString(R.string.otp_resent))
                }

            }
        )
    }

}
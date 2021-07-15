package com.template.views.changepassword

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.models.TokenData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.pref.token
import com.template.pref.tokenType
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import com.template.validations.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePasswordVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    var bundle: Bundle? = null
    val isForgotPassword by lazy { ObservableField(false) }
    val oldPassword by lazy { ObservableField("") }
    val newPassword by lazy { ObservableField("") }
    val confirmPassword by lazy { ObservableField("") }
    fun onClick(view: View) {
        when (view.id) {

            R.id.ivBack -> {
                view.navigateBack()
            }

            R.id.tvSave -> {
                val isForgotPassword = bundle?.containsKey("verificationToken") ?: false
                val context = MainActivity.context.get()
                val validation = Validation.create(context).apply {
                    if (!isForgotPassword) {
                        isEmpty(
                            oldPassword.get(),
                            context?.getString(R.string.enter_old_password) ?: ""
                        )
                    }
                    isEmpty(
                        newPassword.get(),
                        context?.getString(R.string.enter_new_password) ?: ""
                    )
                    isValidPassword(
                        newPassword.get(),
                        context?.getString(R.string.strong_password) ?: ""
                    )
                    isEmpty(
                        confirmPassword.get(),
                        context?.getString(R.string.confirm_new_password) ?: ""
                    )
                    areEqual(
                        newPassword.get(),
                        confirmPassword.get(),
                        context?.getString(R.string.password_does_not_match) ?: ""
                    )
                }
                if (validation.isValid()) {
                    changePassword(view)
                }
            }
        }

    }

    private fun changePassword(view: View?) = viewModelScope.launch {
        val isForgotPassword = bundle?.containsKey("verificationToken") ?: false
        val authToken = if (isForgotPassword) {
            /**Forgot Password*/
            val verificationToken: TokenData =
                bundle?.getSerializable("verificationToken") as TokenData
            "${verificationToken.type} ${verificationToken.token}"
        } else {
            /**change password*/
            /**read token from data store*/
            "${preferenceFile.retrieveKey(tokenType)} ${preferenceFile.retrieveKey(token)}"
        }
        repository.makeCall(
            apiKey = ApiEnums.CHANGE_PASSWORD,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<Any>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<Any> {
                    return if (isForgotPassword)
                        retrofitApi.changePassword(
                            authToken = authToken,
                            password = newPassword.get()?.trim()
                        )
                    else
                        retrofitApi.changePassword(
                            authToken = authToken,
                            password = newPassword.get()?.trim(),
                            oldPassword = oldPassword.get()?.trim()
                        )
                }

                override fun onResponse(res: Response<Any>) {
                    if (isForgotPassword) {
                        /**Navigate to Login*/
                        view?.navigateWithId(R.id.action_changePassword_to_login)
                    } else {
                        view?.navigateBack()
                    }
                }

            }
        )

    }


}




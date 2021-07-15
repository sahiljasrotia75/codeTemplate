package com.template.views.login

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.datastore.LOGIN_DATA
import com.template.datastore.REMEMBER
import com.template.models.LoginData
import com.template.models.SignUpData
import com.template.models.TokenData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.pref.refreshToken
import com.template.pref.token
import com.template.pref.tokenType
import com.template.utils.navigateWithId
import com.template.utils.showNegativeAlerter
import com.template.utils.showPositiveAlerter
import com.template.validations.ValidatorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStoreUtil,
    private val preferences: PreferenceFile
) : ViewModel() {

    var isLogout: Boolean? = false
    val username by lazy { ObservableField("") }
    val password by lazy { ObservableField("") }
    val deviceToken = "dbhbvdhv"

    init {
        MainActivity.navListener?.isLockDrawer(true)
        if (isLogout!!) {
            NavHostFragment.findNavController(Login()).popBackStack()
        }
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.tvForgotPassword -> {
                view.navigateWithId(R.id.action_login_to_forgotPassword)
            }

            R.id.btSignIn -> {
                val context = MainActivity.context.get()

                when {
                    username.get()?.trim()?.isEmpty() == true ->
                        context?.showNegativeAlerter(context.getString(R.string.enter_email_or_password))

                    !ValidatorUtils.isEmailValid(username.get() ?: "") &&
                            !ValidatorUtils.isMobileValid(username.get() ?: "") ->
                        context?.showNegativeAlerter(context.getString(R.string.enter_valid_email_or_password))

                    password.get()?.trim()?.isEmpty() == true ->
                        context?.showNegativeAlerter(context.getString(R.string.enter_password))

                    else ->
                        login(view = view)

                }
            }

            R.id.tvSignup -> {
                view.navigateWithId(R.id.action_login_to_signUp)
            }
        }
    }

    private fun login(view: View?) = viewModelScope.launch {
        repository.makeCall(
            apiKey = ApiEnums.LOGIN,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<LoginData>> {

                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<LoginData> {
                    return retrofitApi.login(
                        username = username.get()?.trim(),
                        password = password.get()?.trim(),
                        deviceType = "ANDROID",
                        deviceToken = deviceToken

                    )
                }

                override fun onResponse(res: Response<LoginData>) {
                    val context = MainActivity.context.get()
                    if (res.body()?.isProfileComplete == true) {
                        val signUpData = SignUpData().apply {
                            _id = res.body()?._id ?: ""
                            email = res.body()?.email ?: ""
                            phone = res.body()?.phone ?: ""

                        }
                        val verificationToken = TokenData().apply {
                            type = res.body()?.type ?: ""
                            token = res.body()?.token ?: ""
                        }
                        val bundle = Bundle().apply {
                            putString("type", "signUp")
                            putSerializable("signUpData", signUpData)
                            putSerializable("verificationToken", verificationToken)
                        }
                        view?.navigateWithId(R.id.action_login_to_profileSetup, bundle)

                    } else {
                        dataStore.saveObject(key = LOGIN_DATA, value = res.body())
                        dataStore.saveData(key = REMEMBER, value = true)
                        preferences.storeKey(token, res.body()?.token ?: "")
                        preferences.storeKey(refreshToken, res.body()?.refreshToken ?: "")
                        preferences.storeKey(tokenType, res.body()?.type ?: "")
                        context?.showPositiveAlerter(context.getString(R.string.logged_in_successfully))
                        view?.navigateWithId(R.id.action_login_to_home2)
                    }
                }
            }
        )
    }

}
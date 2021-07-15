package com.template.views.profilesetup

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.models.LoginData
import com.template.models.SignUpData
import com.template.models.TokenData
import com.template.networkcalls.ApiEnums
import com.template.networkcalls.ApiProcessor
import com.template.networkcalls.Repository
import com.template.networkcalls.RetrofitApi
import com.template.pref.PreferenceFile
import com.template.utils.getPartRequestBody
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import com.template.utils.showPositiveAlerter
import com.template.validations.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileSetupVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    val firstName by lazy { ObservableField("") }
    val lastName by lazy { ObservableField("") }
    val email by lazy { ObservableField("") }
    val phone by lazy { ObservableField("") }
    val countryCode by lazy { ObservableField("") }
    val address by lazy { ObservableField("") }
    var latitude = 0.0
    var longitude = 0.0
    var bundle: Bundle? = null

    fun setData() = try {
        val signUpData: SignUpData? = bundle?.getSerializable("signUpData") as SignUpData?
        email.set(signUpData?.email)
        phone.set(signUpData?.phone)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.ivBack -> {
                view.navigateBack()
            }

            R.id.btSubmit -> {
                val context = MainActivity.context.get()
                val validation = Validation.create(MainActivity.context.get()).apply {
                    isEmpty(firstName.get(), context?.getString(R.string.enter_first_name) ?: "")
                    isEmpty(lastName.get(), context?.getString(R.string.enter_last_name) ?: "")
                    isEmpty(address.get(), context?.getString(R.string.enter_address) ?: "")

                }
                if (validation.isValid()) {
                    setUpProfile(view)
                }

//                view.navigateWithId(R.id.action_profileSetup_to_uploadDocs)
            }
        }
    }

    private fun setUpProfile(view: View?) = viewModelScope.launch {
        val signUpData: SignUpData? = bundle?.getSerializable("signUpData") as SignUpData?
        val tokenData: TokenData? = bundle?.getSerializable("verificationToken") as TokenData?
        repository.makeCall(
            apiKey = ApiEnums.UPDATE_PROFILE,
            loader = true,
            saveInCache = false,
            requestProcessor = object : ApiProcessor<Response<LoginData>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<LoginData> {
                    val hashMap = HashMap<String, RequestBody?>()
                    hashMap["firstName"] = getPartRequestBody(firstName.get())
                    hashMap["lastName"] = getPartRequestBody(lastName.get())
                    hashMap["deviceType"] = getPartRequestBody("ANDROID")
                    hashMap["address"] = getPartRequestBody(address.get())
                    hashMap["latitude"] = getPartRequestBody(latitude.toString())
                    hashMap["longitude"] = getPartRequestBody(longitude.toString())

                    return retrofitApi.updateProfile(
                        authToken = "${tokenData?.type} ${tokenData?.token}",
                        hashMap = hashMap,
                        part = null
                    )
                }

                override fun onResponse(res: Response<LoginData>) {
                    val context = MainActivity.context.get()
                    context?.showPositiveAlerter(context.getString(R.string.signed_up_successfully))
                    view?.navigateWithId(R.id.action_profileSetup_to_login)
                }
            }
        )
    }

}
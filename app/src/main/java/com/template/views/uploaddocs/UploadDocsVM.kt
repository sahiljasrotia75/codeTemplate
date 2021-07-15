package com.template.views.uploaddocs

import android.view.View
import androidx.lifecycle.ViewModel
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import com.template.utils.navigateBack
import com.template.utils.navigateWithId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadDocsVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil
) : ViewModel() {

    init {
        MainActivity.navListener?.isLockDrawer(true)
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.ivBack -> {
                view.navigateBack()
            }

            R.id.btUploading -> {
                view.navigateWithId(R.id.action_uploadDocs_to_vehicleRegistration)
            }
        }
    }

}

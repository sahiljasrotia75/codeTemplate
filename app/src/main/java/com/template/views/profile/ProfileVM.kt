package com.template.views.profile

import android.view.View
import androidx.lifecycle.ViewModel
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {

    fun onClick(view: View) {
        when (view.id) {

            R.id.navBar -> {
                MainActivity.navListener?.openDrawer()
            }
        }
    }
}
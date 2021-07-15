package com.template.views.chat

import android.view.View
import androidx.lifecycle.ViewModel
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import com.template.utils.navigateBack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor(
    private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    val adapter by lazy { ChatAdapter() }

    fun clickBack(view: View) {
        view.navigateBack()
    }

}
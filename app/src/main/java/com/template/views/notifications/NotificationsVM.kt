package com.template.views.notifications

import android.view.View
import androidx.lifecycle.ViewModel
import com.template.MainActivity
import com.template.R
import com.template.datastore.DataStoreUtil
import com.template.networkcalls.Repository
import com.template.pref.PreferenceFile
import com.template.recycleradapter.DummyModel
import com.template.recycleradapter.RecyclerAdapter
import com.template.utils.navigateBack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsVM @Inject constructor(private val repository: Repository,
    private val preferenceFile: PreferenceFile,
    private val dataStore: DataStoreUtil,
) : ViewModel() {
    // TODO: Implement the ViewModel
    val adapter by lazy { RecyclerAdapter<DummyModel>(R.layout.notification_adapter) }
    val olderAdapter by lazy { RecyclerAdapter<DummyModel>(R.layout.notification_adapter) }

    init {
        adapter.addItems(
            listOf(
                DummyModel(),
                DummyModel()
            )
        )

        olderAdapter.addItems(
            listOf(
                DummyModel(true),
                DummyModel(true),
                DummyModel(true),
                DummyModel(true),
                DummyModel(true)
            )
        )
    }

    fun onClick(view: View) {
        when (view.id) {

            R.id.ivBack -> {
                view.navigateBack()
            }

            R.id.navBar -> {
                MainActivity.navListener?.openDrawer()
            }
        }
    }
}
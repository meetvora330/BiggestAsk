package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.ResetMilestoneResponse
import com.biggestAsk.data.repository.SettingRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    application: Application
) : AndroidViewModel(application) {
    var openDialogDeleteSetting: Boolean by mutableStateOf(false)
    var isAllMilestoneReset: Boolean by mutableStateOf(false)
    var resetMilestoneResponse: MutableLiveData<NetworkResult<ResetMilestoneResponse>> =
        MutableLiveData()

    fun resetMilestone(resetMilestoneRequest: ResetMilestoneRequest) {
        resetMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            settingRepository.resetMilestone(
                resetMilestoneRequest = resetMilestoneRequest
            ).collect {
                resetMilestoneResponse.value = it
            }
        }
    }

}
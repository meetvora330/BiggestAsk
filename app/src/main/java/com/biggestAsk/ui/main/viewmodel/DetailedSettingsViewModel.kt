package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.NotificationStatusUpdateRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetNotificationStatusResponse
import com.biggestAsk.data.repository.DetailedSettingsRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedSettingsViewModel @Inject constructor(
    private val detailedSettingsRepository: DetailedSettingsRepository,
    application: Application
) : AndroidViewModel(application) {

    var checkedStateNotification: Boolean by mutableStateOf(false)
    var checkedStateNewsLetters: Boolean by mutableStateOf(true)
    var isNotificationStatusUpdated: Boolean by mutableStateOf(false)
    var isNotificationStatusFetched: Boolean by mutableStateOf(false)
    var notificationStatusUpdateResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    var getNotificationStatusResponse: MutableLiveData<NetworkResult<GetNotificationStatusResponse>> =
        MutableLiveData()
    var userLogoutResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()

    fun notificationStatusUpdate(notificationStatusUpdateRequest: NotificationStatusUpdateRequest) {
        notificationStatusUpdateResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            detailedSettingsRepository.notificationStatusUpdate(
                notificationStatusUpdateRequest = notificationStatusUpdateRequest
            ).collect {
                notificationStatusUpdateResponse.value = it
            }
        }
    }

    fun getNotificationStatus(
        type: String,
        user_id: Int
    ) {
        getNotificationStatusResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            detailedSettingsRepository.getNotificationStatus(
                type = type,
                userId = user_id
            ).collect {
                getNotificationStatusResponse.value = it
            }
        }
    }

    fun userLogout(
        type: String,
        user_id: Int
    ) {
        userLogoutResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            detailedSettingsRepository.userLogout(
                type = type,
                userId = user_id
            ).collect {
                userLogoutResponse.value = it
            }
        }
    }

}
package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetNotificationRequest
import com.biggestAsk.data.model.response.GetNotificationResponse
import com.biggestAsk.data.model.response.GetNotificationResponseData
import com.biggestAsk.data.repository.NotificationRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Abhin.
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isDataNull: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var notificationList = mutableStateListOf<GetNotificationResponseData>()
    var notificationDaysList = mutableStateListOf<Int>()
    var getNotificationResponse: MutableLiveData<NetworkResult<GetNotificationResponse>> =
        MutableLiveData()
    val isNotificationScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNotificationDetailsScreen: MutableState<Boolean> = mutableStateOf(false)
    var isSearchClicked: MutableState<Boolean> = mutableStateOf(false)
    var updatedList = mutableStateListOf<GetNotificationResponseData>()
    var searchText: String by mutableStateOf("")

    fun getNotification(getNotificationRequest: GetNotificationRequest) {
        getNotificationResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            notificationRepository.getNotification(getNotificationRequest).collect {
                getNotificationResponse.value = it
            }
        }
    }

    fun getFilteredList(searchText: String) {
        val data = notificationList.filter {
            it.title.contains(searchText)
        }
        updatedList.clear()
        updatedList.addAll(data)
    }
}
package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.LogoutRequest
import com.biggestAsk.data.model.response.LogoutResponse
import com.biggestAsk.data.repository.LogoutRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutRepository: LogoutRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isLoading: Boolean by mutableStateOf(false)
    var logOutResponse: MutableLiveData<NetworkResult<LogoutResponse>> = MutableLiveData()
    var logOutSuccessMessage: String by mutableStateOf("")
    var openLogoutDialog: Boolean by mutableStateOf(false)

    fun logOut(logoutRequest: LogoutRequest) {
        logOutResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            logoutRepository.logOut(logoutRequest).collect {
                logOutResponse.value = it
            }
        }
    }
}
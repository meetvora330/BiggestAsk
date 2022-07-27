package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.repository.EmailVerificationRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val emailVerificationRepository: EmailVerificationRepository,
    application: Application
) : AndroidViewModel(application) {
    var isLoading: Boolean by mutableStateOf(false)
    var textEmailVerify: String by mutableStateOf("")
    var sendOtpResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()

    fun sendOtp(sendOtpRequest: SendOtpRequest) {
        sendOtpResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            emailVerificationRepository.sendOtp(sendOtpRequest).collect {
                sendOtpResponse.value = it
            }
        }
    }
}
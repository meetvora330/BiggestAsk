package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.CheckOtpRequest
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.repository.VerifyOtpRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Abhin.
 * verify otp screen viewModel
 */
@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val verifyOtpRepository: VerifyOtpRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isOtpValueVerified: Boolean by mutableStateOf(false)
    var ticks: Int by mutableStateOf(60)
    var minute: Int by mutableStateOf(0)
    var isLoading: Boolean by mutableStateOf(false)
    var checkOtpResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()
    var reSendOtpResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()
    fun resendOtp(sendOtpRequest: SendOtpRequest) {
        reSendOtpResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            verifyOtpRepository.resendOtp(sendOtpRequest = sendOtpRequest).collect {
                reSendOtpResponse.value = it
            }
        }
    }

    fun checkOtp(checkOtpRequest: CheckOtpRequest) {
        viewModelScope.launch {
            checkOtpResponse.value = NetworkResult.Loading()
            verifyOtpRepository.checkOtp(checkOtpRequest).collect {
                checkOtpResponse.value = it
            }
        }
    }
}
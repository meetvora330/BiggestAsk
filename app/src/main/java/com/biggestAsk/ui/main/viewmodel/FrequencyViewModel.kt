package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.ScreenQuestionStatusRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FrequencyViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application,
) : AndroidViewModel(application) {

    //Frequency Screen
    var isLoading: Boolean by mutableStateOf(false)
    val selectedValueEveryDayRb = mutableStateOf(true)
    val selectedValueEvery3DaysRb = mutableStateOf(false)
    var screenQuestionStatus: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()

    fun screenQuestionStatus(screenQuestionStatusRequest: ScreenQuestionStatusRequest) {
        screenQuestionStatus.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.screenQuestionStatus(screenQuestionStatusRequest).collect {
                screenQuestionStatus.value = it
            }
        }
    }

}
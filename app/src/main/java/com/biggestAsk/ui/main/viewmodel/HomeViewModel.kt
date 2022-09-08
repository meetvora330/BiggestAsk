package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.StoreQuestionAnsRequest
import com.biggestAsk.data.model.request.UpdatePaymentStatusRequest
import com.biggestAsk.data.model.response.BaseScreenQuestionResponse
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application,
) : AndroidViewModel(application) {

    //Globally Used Variable
    var isLoading: Boolean by mutableStateOf(false)

    //Base Question Screen
    var baseScreenQuestion: MutableLiveData<NetworkResult<List<BaseScreenQuestionResponse>>> =
        MutableLiveData()
    val valueStateList = mutableStateListOf<BaseScreenQuestionResponse>()
    var updatePaymentStatusResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()

    var storeQuestionAns: MutableLiveData<NetworkResult<StoreQuestionAnsRequest>> =
        MutableLiveData()


    fun updatePaymentStatus(updatePaymentStatusRequest: UpdatePaymentStatusRequest) {
        updatePaymentStatusResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.updatePaymentStatus(updatePaymentStatusRequest).collect {
                updatePaymentStatusResponse.value = it
            }
        }
    }

    fun getBaseScreenQuestion() {
        baseScreenQuestion.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getBaseScreenQuestion().collect {
                baseScreenQuestion.value = it
            }
        }
    }


}
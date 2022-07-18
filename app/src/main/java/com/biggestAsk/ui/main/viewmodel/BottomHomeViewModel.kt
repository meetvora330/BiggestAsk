package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.IntendedParentQuestionAnsRequest
import com.biggestAsk.data.model.response.GetHomeScreenQuestionResponse
import com.biggestAsk.data.model.response.GetNearestMilestoneResponse
import com.biggestAsk.data.model.response.GetPregnancyMilestoneResponse
import com.biggestAsk.data.model.response.IntendedParentQuestionResponse
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomHomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    var pregnancyTittle: String by mutableStateOf("")
    var pregnancyDescription: String by mutableStateOf("")
    var pregnancyImageUrl: String by mutableStateOf("")
    var homeScreenLatestQuestion: String by mutableStateOf("")
    var intendedParentQuestion: String by mutableStateOf("")
    var intendedParentAnswer: String by mutableStateOf("")
    var intendedParentDays: Int by mutableStateOf(0)
    var intendedParentUserName: String by mutableStateOf("")
    var nearestMilestoneTittle: String by mutableStateOf("")
    var nearestMilestoneDate: String by mutableStateOf("")
    var nearestMilestoneTime: String by mutableStateOf("")
    var isAllDataLoaded: Boolean by mutableStateOf(true)
    var isPregnancyDataLoaded: Boolean by mutableStateOf(true)
    var isHomeScreenQuestionDataLoaded: Boolean by mutableStateOf(true)
    var isIntendedParentQuestionDataLoaded: Boolean by mutableStateOf(true)
    var isNearestMilestoneDataLoaded: Boolean by mutableStateOf(true)
    var getPregnancyMilestoneResponse: MutableLiveData<NetworkResult<GetPregnancyMilestoneResponse>> =
        MutableLiveData()
    var getHomeScreenQuestionResponse: MutableLiveData<NetworkResult<GetHomeScreenQuestionResponse>> =
        MutableLiveData()
    var intendedPartnerQuestionAnsResponse: MutableLiveData<NetworkResult<IntendedParentQuestionResponse>> =
        MutableLiveData()
    var getNearestMilestoneResponse: MutableLiveData<NetworkResult<GetNearestMilestoneResponse>> =
        MutableLiveData()

    fun getPregnancyMilestone(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getPregnancyMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getPregnancyMilestone(getPregnancyMilestoneRequest).collect {
                getPregnancyMilestoneResponse.value = it
            }
        }
    }

    fun getHomeScreenQuestion(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getHomeScreenQuestionResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getHomeScreenQuestion(getPregnancyMilestoneRequest).collect {
                getHomeScreenQuestionResponse.value = it
            }
        }
    }

    fun getIntendedParentQuestionAns(intendedParentQuestionAnsRequest: IntendedParentQuestionAnsRequest) {
        intendedPartnerQuestionAnsResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getIntendedParentQuestionAns(intendedParentQuestionAnsRequest).collect {
                intendedPartnerQuestionAnsResponse.value = it
            }
        }
    }

    fun getNearestMilestone(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getNearestMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getNearestMilestone(getPregnancyMilestoneRequest).collect {
                getNearestMilestoneResponse.value = it
            }
        }
    }
}
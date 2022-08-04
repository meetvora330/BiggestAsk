package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.request.Answer
import com.biggestAsk.data.model.response.*
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
    var homeScreenImportantQuestion: String by mutableStateOf("")
    var homeScreenLatestQuestion: String by mutableStateOf("")
    var intendedParentQuestion: String by mutableStateOf("")
    var intendedParentAnswer: String by mutableStateOf("")
    var intendedParentDays: Int by mutableStateOf(0)
    var intendedParentUserName: String by mutableStateOf("")
    var nearestMilestoneTittle: String by mutableStateOf("")
    var nearestMilestoneDate: String by mutableStateOf("")
    var nearestMilestoneTime: String by mutableStateOf("")
    var homeScreenQuestionAns: String by mutableStateOf("")
    var homeScreenQuestionCategeryId: Int by mutableStateOf(0)
    var homeScreenQuestionId: Int by mutableStateOf(0)
    var homeScreenImportantQuestionId: Int by mutableStateOf(0)
    var isAllDataLoaded: Boolean by mutableStateOf(true)
    var isErrorOccurred: Boolean by mutableStateOf(false)
    var isErrorOccurredPregnancyMilestone: Boolean by mutableStateOf(false)
    var isErrorOccurredHomeScreenQuestion: Boolean by mutableStateOf(false)
    var isErrorOccurredIntendedParentQuestion: Boolean by mutableStateOf(false)
    var isErrorOccurredNearestMilestone: Boolean by mutableStateOf(false)
    var isPregnancyDataLoaded: Boolean by mutableStateOf(false)
    var isQuestionDataEmpty: Boolean by mutableStateOf(false)
    var isHomeScreenQuestionDataLoaded: Boolean by mutableStateOf(false)
    var isIntendedParentQuestionDataLoaded: Boolean by mutableStateOf(false)
    var upperQuestion: Boolean by mutableStateOf(false)
    var isNearestMilestoneDataLoaded: Boolean by mutableStateOf(false)
    var getPregnancyMilestoneResponse: MutableLiveData<NetworkResult<GetImportantQuestionResponse>> =
        MutableLiveData()
    var isHomeScreenQuestionAnsEmpty: Boolean by mutableStateOf(false)
    var isHomeScreenQuestionAnswered: Boolean by mutableStateOf(false)

    var getHomeScreenQuestionResponse: MutableLiveData<NetworkResult<GetHomeScreenQuestionResponse>> =
        MutableLiveData()
    var intendedPartnerQuestionAnsResponse: MutableLiveData<NetworkResult<IntendedParentQuestionResponse>> =
        MutableLiveData()
    var getNearestMilestoneResponse: MutableLiveData<NetworkResult<GetNearestMilestoneResponse>> =
        MutableLiveData()
    var storeBaseScreenQuestionAnsResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    var storeAnsImportantQuestionResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    var answerList = mutableStateListOf<Answer>()
    var parentList = mutableStateListOf<String>()

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

    fun storeBaseScreenQuestionAns(storeBaseScreenQuestionAnsRequest: StoreBaseScreenQuestionAnsRequest) {
        storeBaseScreenQuestionAnsResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.storeBaseScreenQuestionAns(storeBaseScreenQuestionAnsRequest).collect {
                storeBaseScreenQuestionAnsResponse.value = it
            }
        }
    }

    fun storeAnsImportantQuestion(storeAnsImportantQuestionRequest: StoreAnsImportantQuestionRequest) {
        storeAnsImportantQuestionResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.storeAnsImportantQuestion(storeAnsImportantQuestionRequest).collect {
                storeAnsImportantQuestionResponse.value = it
            }
        }
    }
}
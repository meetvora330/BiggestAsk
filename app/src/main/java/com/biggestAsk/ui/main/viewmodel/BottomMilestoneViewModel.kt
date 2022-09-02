package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.CreateMilestoneRequest
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetMilestoneResponse
import com.biggestAsk.data.model.response.Milestone
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomMilestoneViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {
    var getMilestoneResponse: MutableLiveData<NetworkResult<GetMilestoneResponse>> =
        MutableLiveData()
    var resetMilestoneResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    val milestoneList = mutableStateListOf<Milestone>()
    var emptyList by mutableStateOf(listOf<Milestone>())
    var isAllMilestoneLoaded: Boolean by mutableStateOf(false)
    var isAnyErrorOccurred: Boolean by mutableStateOf(false)
    var addNewMilestoneTittle: MutableState<String> = mutableStateOf("")
    var addNewMilestoneTittleEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneDate: MutableState<String> = mutableStateOf("")
    var addNewMilestoneDateEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneTime: MutableState<String> = mutableStateOf("")
    var addNewMilestoneTimeEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneLocationB: MutableState<String> = mutableStateOf("")
    var addNewMilestoneLocationBEmpty: MutableState<Boolean> = mutableStateOf(false)
    var isNewMilestoneAdded: MutableState<Boolean> = mutableStateOf(false)
    var isBottomSheetOpen: MutableState<Boolean> = mutableStateOf(false)
    var createMilestoneResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()
    var isSelected: Boolean by mutableStateOf(false)

    fun getMilestones(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getMilestones(getPregnancyMilestoneRequest).collect {
                getMilestoneResponse.value = it
            }
        }
    }

    fun createMilestone(createMilestoneRequest: CreateMilestoneRequest) {
        createMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.createMilestone(createMilestoneRequest).collect {
                createMilestoneResponse.value = it
            }
        }
    }

    fun resetMilestone(resetMilestoneRequest: ResetMilestoneRequest) {
        resetMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.resetMilestone(resetMilestoneRequest).collect {
                resetMilestoneResponse.value = it
            }
        }
    }

}
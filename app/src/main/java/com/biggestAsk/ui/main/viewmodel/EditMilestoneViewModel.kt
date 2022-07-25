package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.EditMilestoneRequest
import com.biggestAsk.data.model.request.SaveNoteRequest
import com.biggestAsk.data.model.request.UpdateMilestoneAnsInfoRequest
import com.biggestAsk.data.model.response.EditMilestoneImageResponse
import com.biggestAsk.data.model.response.EditMilestoneResponse
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMilestoneViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {
    var editMilestoneTittle: MutableState<String> = mutableStateOf("")
    var editMilestoneTittleEmpty: MutableState<Boolean> = mutableStateOf(false)
    var editMilestoneDate: MutableState<String> = mutableStateOf("")
    var editMilestoneDateEmpty: MutableState<Boolean> = mutableStateOf(false)
    var editMilestoneTime: MutableState<String> = mutableStateOf("")
    var editMilestoneTimeEmpty: MutableState<Boolean> = mutableStateOf(false)
    var editMilestoneLocationB: MutableState<String> = mutableStateOf("")
    var editMilestoneLocationBEmpty: MutableState<Boolean> = mutableStateOf(false)
    var addNewMilestoneNotes: MutableState<String> = mutableStateOf("")
    var isEditMilestoneDataLoaded: MutableState<Boolean> = mutableStateOf(false)
    var isMilestoneTittleEditable: MutableState<Boolean> = mutableStateOf(false)
    var milestoneId: MutableState<Int> = mutableStateOf(0)
    var isMilestoneDataUpdated: MutableState<Boolean> = mutableStateOf(false)
    var isNoteSaved: MutableState<Boolean> = mutableStateOf(false)
    var imageList = mutableListOf<EditMilestoneImageResponse>()
    var tempImageList = mutableListOf<Bitmap>()
    var imageListIndex = mutableStateOf<Int>(-1)
    var editMilestoneResponse: MutableLiveData<NetworkResult<EditMilestoneResponse>> =
        MutableLiveData()
    var updateMilestoneAnsInfoResponse: MutableLiveData<NetworkResult<SendOtpResponse>> =
        MutableLiveData()
    var saveNoteResponse: MutableLiveData<NetworkResult<SendOtpResponse>> = MutableLiveData()

    fun editMilestone(editMilestoneRequest: EditMilestoneRequest) {
        editMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.editMilestone(editMilestoneRequest).collect {
                editMilestoneResponse.value = it
            }
        }
    }

    fun updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest: UpdateMilestoneAnsInfoRequest) {
        updateMilestoneAnsInfoResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest).collect {
                updateMilestoneAnsInfoResponse.value = it
            }
        }
    }

    fun saveNote(saveNoteRequest: SaveNoteRequest) {
        saveNoteResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.saveNote(saveNoteRequest).collect {
                saveNoteResponse.value = it
            }
        }
    }
}
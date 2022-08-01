package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.DeleteMilestoneImageRequest
import com.biggestAsk.data.model.request.EditMilestoneRequest
import com.biggestAsk.data.model.request.SaveNoteRequest
import com.biggestAsk.data.model.request.UpdateMilestoneAnsInfoRequest
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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
    var isMilestoneAnsUpdated: MutableState<Boolean> = mutableStateOf(false)
    var isImageDeleted: MutableState<Boolean> = mutableStateOf(false)
    var isMilestoneImageUpdated: MutableState<Boolean> = mutableStateOf(false)
    var checkBoxShareWithParents: Boolean by mutableStateOf(true)
    var checkBoxShareWithBiggestAsk: Boolean by mutableStateOf(false)
    var isPermissionAllowed: MutableState<Boolean> = mutableStateOf(false)
    var cancelDialog: Boolean by mutableStateOf(true)
    var imageList = mutableStateListOf<EditMilestoneImageResponse>()
    var imageListIndex = mutableStateOf<Int>(-1)
    var editMilestoneResponse: MutableLiveData<NetworkResult<EditMilestoneResponse>> =
        MutableLiveData()
    var updateMilestoneAnsInfoResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    var saveNoteResponse: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()
    var updateMilestoneResponse: MutableLiveData<NetworkResult<UpdateUserProfileResponse>> =
        MutableLiveData()
    var updateMilestoneImage: MutableLiveData<NetworkResult<UpdateImageResponse>> =
        MutableLiveData()
    var deleteMilestoneImageResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()
    var getUpdatedStatusResponse: MutableLiveData<NetworkResult<UpdatedStatusResponse>> =
        MutableLiveData()

    fun getMilestoneDetails(editMilestoneRequest: EditMilestoneRequest) {
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

    fun storeMilestoneAns(
        images: ArrayList<MultipartBody.Part?>,
        user_id: MultipartBody.Part?,
        type: MultipartBody.Part?,
        milestone_id: MultipartBody.Part?,
        note_status: MultipartBody.Part?,
        note_biggest: MultipartBody.Part?
    ) {
        updateMilestoneResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.storeMilestoneAns(
                images,
                user_id,
                type,
                milestone_id,
                note_status,
                note_biggest
            ).collect {
                updateMilestoneResponse.value = it
            }
        }
    }

    fun updateMilestoneImage(image_id: MultipartBody.Part?, image: MultipartBody.Part?) {
        updateMilestoneImage.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.updateMilestoneImage(
                image_id, image
            ).collect {
                updateMilestoneImage.value = it
            }
        }
    }

    fun deleteMileStoneImage(image_id: DeleteMilestoneImageRequest) {
        deleteMilestoneImageResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.deleteMilestoneImage(image_id).collect {
                deleteMilestoneImageResponse.value = it
            }
        }
    }

    fun getUpdatedStatus(userId: Int, type: String) {
        getUpdatedStatusResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getUpdatedStatus(userId, type).collect {
                getUpdatedStatusResponse.value = it
            }
        }
    }
}
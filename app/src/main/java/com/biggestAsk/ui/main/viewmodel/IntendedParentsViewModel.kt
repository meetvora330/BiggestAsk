package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.response.GetIntendedProfileResponse
import com.biggestAsk.data.model.response.QuestionAn
import com.biggestAsk.data.repository.IntendedParentsRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntendedParentsViewModel @Inject constructor(
    private val intendedParentsRepository: IntendedParentsRepository,
    application: Application,
) : AndroidViewModel(application) {
    var isIntendedParentsDataLoading: Boolean by mutableStateOf(false)
    var isAnyErrorOccurred: Boolean by mutableStateOf(false)
    var isFatherClicked: Boolean by mutableStateOf(true)
    var isMotherClicked: Boolean by mutableStateOf(false)
    var parentFullName: String by mutableStateOf("")
    var parentDateOfBirth: String by mutableStateOf("")
    var parentHomeAddress: String by mutableStateOf("")
    var parentPhoneNumber: String by mutableStateOf("")
    var parentEmail: String by mutableStateOf("")
    var motherFullName: String by mutableStateOf("")
    var motherDateOfBirth: String by mutableStateOf("")
    var motherHomeAddress: String by mutableStateOf("")
    var motherPhoneNumber: String by mutableStateOf("")
    var motherEmail: String by mutableStateOf("")
    var imageFather: String by mutableStateOf("")
    var imageMother: String by mutableStateOf("")
    var intendedProfileResponseQuestionList = mutableStateListOf<QuestionAn>()
    var intendedProfileResponseDaysList = mutableStateListOf<Int>()


    var getIntendedProfileResponse: MutableLiveData<NetworkResult<GetIntendedProfileResponse>> =
        MutableLiveData()

    fun getIntendedParentProfile(type: String, userId: Int) {
        getIntendedProfileResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            intendedParentsRepository.getIntendedParentProfile(type = type, user_id = userId)
                .collect {
                    getIntendedProfileResponse.value = it
                }
        }
    }
}
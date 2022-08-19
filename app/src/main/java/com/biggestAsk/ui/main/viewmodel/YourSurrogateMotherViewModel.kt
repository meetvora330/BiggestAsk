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
import com.biggestAsk.data.repository.YourSurrogateMotherRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourSurrogateMotherViewModel @Inject constructor(
    private val yourSurrogateMotherRepository: YourSurrogateMotherRepository,
    application: Application,
) : AndroidViewModel(application) {
    var isSurrogateDataLoading: Boolean by mutableStateOf(false)
    var isAnyErrorOccurred: Boolean by mutableStateOf(false)
    var intendedProfileResponseQuestionList = mutableStateListOf<QuestionAn>()
    var intendedProfileResponseDaysList = mutableStateListOf<Int>()
    var getIntendedProfileResponse: MutableLiveData<NetworkResult<GetIntendedProfileResponse>> =
        MutableLiveData()

    var surrogateMotherFullName: String by mutableStateOf("")
    var surrogatePartnerFullName: String by mutableStateOf("Not inserted")
    var surrogateMotherImg: String by mutableStateOf("")
    var surrogateMotherPhoneNumber: String by mutableStateOf("")
    var surrogateMotherEmail: String by mutableStateOf("")
    var surrogateMotherHomeAddress: String by mutableStateOf("")
    var surrogateMotherDateOfBirth: String by mutableStateOf("")

    fun getIntendedParentProfile(type: String, userId: Int) {
        getIntendedProfileResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourSurrogateMotherRepository.getIntendedParentProfile(type = type, user_id = userId)
                .collect {
                    getIntendedProfileResponse.value = it
                }
        }
    }
}
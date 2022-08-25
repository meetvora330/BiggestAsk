package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.Answer
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.StoreAnsImportantQuestionRequest
import com.biggestAsk.data.model.request.StoreBaseScreenQuestionAnsRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.DataXXX
import com.biggestAsk.data.model.response.GetFrequencyResponse
import com.biggestAsk.data.model.response.GetHomeScreenQuestionResponse
import com.biggestAsk.data.repository.QuestionRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    application: Application
) : AndroidViewModel(application) {

    var isAnsweredQuestionLoading: Boolean by mutableStateOf(false)
    var isFrequencyDataLoading: Boolean by mutableStateOf(false)
    var isQuestionScreenQuestionAnswered: Boolean by mutableStateOf(false)
    var isAnswerEmpty: Boolean by mutableStateOf(false)
    var isErrorOccurredInQuestionLoading: Boolean by mutableStateOf(false)
    var isQuestionScreenQuestionDataLoaded: Boolean by mutableStateOf(false)
    var dropDownItemParentsName = mutableStateListOf<String>()
    var isErrorOccurredQuestionScreenQuestion: Boolean by mutableStateOf(false)
    var questionScreenQuestionCategeryId: Int by mutableStateOf(0)
    var questionScreenQuestionId: Int by mutableStateOf(0)
    var questionScreenLatestQuestion: String by mutableStateOf("")
    var questionScreenQuestionAnswer: String by mutableStateOf("")
    var questionScreenQuestionAns: String by mutableStateOf("")
    var questionAnsweredList = mutableStateListOf<DataXXX>()
    var questionAnsweredDaysList = mutableStateListOf<Int>()
    var questionParentList = mutableStateListOf<String>()
    var frequency: String by mutableStateOf("")
    var answerList = mutableStateListOf<Answer>()
    var getHomeScreenQuestionResponse: MutableLiveData<NetworkResult<GetHomeScreenQuestionResponse>> =
        MutableLiveData()
    var getFrequencyResponse: MutableLiveData<NetworkResult<GetFrequencyResponse>> =
        MutableLiveData()
    var storeAnsImportantQuestionResponse: MutableLiveData<NetworkResult<CommonResponse>> =
        MutableLiveData()

    fun getHomeScreenQuestion(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getHomeScreenQuestionResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            questionRepository.getHomeScreenQuestion(getPregnancyMilestoneRequest).collect {
                getHomeScreenQuestionResponse.value = it
            }
        }
    }

    fun getFrequency(user_id: Int, type: String) {
        getFrequencyResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            questionRepository.getFrequency(user_id = user_id, type = type).collect {
                getFrequencyResponse.value = it
            }
        }
    }

    fun storeQuestionScreenAnswer(storeBaseScreenQuestionAnsRequest: StoreBaseScreenQuestionAnsRequest) {
        storeAnsImportantQuestionResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            questionRepository.storeAnsQuestionScreen(storeBaseScreenQuestionAnsRequest = storeBaseScreenQuestionAnsRequest)
                .collect {
                    storeAnsImportantQuestionResponse.value = it
                }
        }
    }
}
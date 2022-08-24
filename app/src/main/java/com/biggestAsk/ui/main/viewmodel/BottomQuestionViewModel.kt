package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.response.DataXXX
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
    var questionAnsweredList = mutableStateListOf<DataXXX>()
    var questionAnsweredDaysList = mutableStateListOf<Int>()
    var getHomeScreenQuestionResponse: MutableLiveData<NetworkResult<GetHomeScreenQuestionResponse>> =
        MutableLiveData()

    fun getHomeScreenQuestion(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest) {
        getHomeScreenQuestionResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            questionRepository.getHomeScreenQuestion(getPregnancyMilestoneRequest).collect {
                getHomeScreenQuestionResponse.value = it
            }
        }
    }
}
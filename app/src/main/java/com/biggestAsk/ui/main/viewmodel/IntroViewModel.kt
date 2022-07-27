package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.response.DataXX
import com.biggestAsk.data.model.response.IntroInfoResponse
import com.biggestAsk.data.model.response.UpdatedStatusResponse
import com.biggestAsk.data.repository.HomeRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {
    var getIntroInfoResponse :MutableLiveData<NetworkResult<IntroInfoResponse>> = MutableLiveData()
    var getUpdatedStatusResponse :MutableLiveData<NetworkResult<UpdatedStatusResponse>> = MutableLiveData()
    var introInfoDetailList = mutableListOf<DataXX>()
    var isIntroDataLoaded:Boolean by mutableStateOf(false)
    var isUserStatusDataLoaded:Boolean by mutableStateOf(false)
    var isAPILoadingFailed:Boolean by mutableStateOf(false)
    fun getIntroInfo(){
        getIntroInfoResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getIntroInfo().collect{
                getIntroInfoResponse.value = it
            }
        }
    }
    fun getUpdatedStatus(userId:Int, type: String){
        getUpdatedStatusResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            homeRepository.getUpdatedStatus(userId,type).collect{
                getUpdatedStatusResponse.value = it
            }
        }
    }
}
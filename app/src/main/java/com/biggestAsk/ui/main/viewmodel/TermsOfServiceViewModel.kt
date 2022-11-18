package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.response.GetTermsOfServiceResponse
import com.biggestAsk.data.model.response.GetTermsOfServiceResponseData
import com.biggestAsk.data.repository.TermsOfServiceRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Abhin.
 * terms of policy screen viewModel
 */
@HiltViewModel
class TermsOfServiceViewModel @Inject constructor(
    private val termsOfServiceRepository: TermsOfServiceRepository,
    application: Application,
) : AndroidViewModel(application) {

    var getTermsOfServiceResponse: MutableLiveData<NetworkResult<GetTermsOfServiceResponse>> =
        MutableLiveData()
    var isDataNull: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var termsOfServiceList = mutableStateListOf<GetTermsOfServiceResponseData>()
    var lastUpdatedDate: String by mutableStateOf("")

    fun getAboutApp() {
        getTermsOfServiceResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            termsOfServiceRepository.getTermsOfService().collect {
                getTermsOfServiceResponse.value = it
            }
        }
    }
}
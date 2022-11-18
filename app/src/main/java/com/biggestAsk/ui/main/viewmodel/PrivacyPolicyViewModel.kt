package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.response.GetPrivacyPolicyResponse
import com.biggestAsk.data.model.response.GetPrivacyPolicyResponseData
import com.biggestAsk.data.repository.PrivacyPolicyRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Abhin.
 * privacy policy screen viewModel
 */
@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val getPrivacyPolicyRepository: PrivacyPolicyRepository,
    application: Application,
) : AndroidViewModel(application) {

    var getPrivacyPolicyResponse: MutableLiveData<NetworkResult<GetPrivacyPolicyResponse>> =
        MutableLiveData()
    var isDataNull: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var privacyPolicyList = mutableStateListOf<GetPrivacyPolicyResponseData>()
    var lastUpdatedDate: String by mutableStateOf("")

    fun getPrivacyPolicy() {
        getPrivacyPolicyResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            getPrivacyPolicyRepository.getPrivacyPolicy().collect {
                getPrivacyPolicyResponse.value = it
            }
        }
    }
}
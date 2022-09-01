package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.response.GetAboutAppResponse
import com.biggestAsk.data.model.response.GetAboutAppResponseData
import com.biggestAsk.data.repository.AboutAppRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Abhin.
 */
@HiltViewModel
class AboutAppViewModel @Inject constructor(
    private val aboutAppRepository: AboutAppRepository,
    application: Application,
) : AndroidViewModel(application) {

    var getAboutAppResponse: MutableLiveData<NetworkResult<GetAboutAppResponse>> = MutableLiveData()
    var isDataNull: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var aboutAppList = mutableStateListOf<GetAboutAppResponseData>()
    var lastUpdatedDate: String by mutableStateOf("")

    fun getAboutApp() {
        getAboutAppResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            aboutAppRepository.getAboutApp().collect {
                getAboutAppResponse.value = it
            }
        }
    }
}
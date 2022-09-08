package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.LoginBodyRequest
import com.biggestAsk.data.model.response.LoginBodyResponse
import com.biggestAsk.data.repository.LoginRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    application: Application,
) : AndroidViewModel(application) {

    var loginTextEmail: String by mutableStateOf("")
    var loginTextPass: String by mutableStateOf("")
    var isLoginEmailEmpty: Boolean by mutableStateOf(false)
    var isLoginEmailValid: Boolean by mutableStateOf(false)
    var isLoginPassEmpty: Boolean by mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var loginScreen: MutableLiveData<NetworkResult<LoginBodyResponse>> = MutableLiveData()

    fun login(loginBodyRequest: LoginBodyRequest) {
        viewModelScope.launch {
            loginScreen.value = NetworkResult.Loading()
            loginRepository.login(loginBodyRequest).collect {
                loginScreen.value = it
            }
        }
    }

}
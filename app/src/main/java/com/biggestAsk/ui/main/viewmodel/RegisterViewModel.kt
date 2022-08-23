package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.RegistrationBodyRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.repository.RegisterRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository,
    application: Application
) : AndroidViewModel(application) {
    var textFullName: String by mutableStateOf("")
    var textPass: String by mutableStateOf("")
    var textReEnterPass: String by mutableStateOf("")
    var termCheckedState: Boolean by mutableStateOf(false)
    var isNameEmpty: Boolean by mutableStateOf(false)
    var isEmailEmpty: Boolean by mutableStateOf(false)
    var isPassEmpty: Boolean by mutableStateOf(false)
    var isGenderSelected: Boolean by mutableStateOf(false)
    var isRePassEmpty: Boolean by mutableStateOf(false)
    val selectedValueIntendParentRb = mutableStateOf(true)
    val selectedValueSurrogateMotherRb = mutableStateOf(false)
    var isLoading: Boolean by mutableStateOf(false)
    var registerScreen: MutableLiveData<NetworkResult<CommonResponse>> = MutableLiveData()
    fun registration(registrationBodyRequest: RegistrationBodyRequest) {
        viewModelScope.launch {
            registerScreen.value = NetworkResult.Loading()
            registerRepository.registration(registrationBodyRequest).collect {
                registerScreen.value = it
            }
        }
    }
}
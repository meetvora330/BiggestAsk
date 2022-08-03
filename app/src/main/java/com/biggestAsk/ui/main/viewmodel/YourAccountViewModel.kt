package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetUserDetailsRequest
import com.biggestAsk.data.model.response.GetUserDetailsResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.repository.YourAccountRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class YourAccountViewModel @Inject constructor(
    private val yourAccountRepository: YourAccountRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isLoading: Boolean by mutableStateOf(false)
    var textEmailVerify: String by mutableStateOf("")
    var yourAccountFullName: String by mutableStateOf("")
    var profileImg: String by mutableStateOf("")
    var yourAccountType: String by mutableStateOf("surrogate")
    var yourAccountFullNameEmpty: Boolean by mutableStateOf(false)
    var yourAccountPhoneNumber: String by mutableStateOf("")
    var yourAccountPhoneNumberEmpty: Boolean by mutableStateOf(false)
    var yourAccountEmail: String by mutableStateOf("")
    var yourAccountEmailIsValid: Boolean by mutableStateOf(false)
    var yourAccountEmailEmpty: Boolean by mutableStateOf(false)
    var yourAccountHomeAddress: String by mutableStateOf("")
    var yourAccountHomeAddressEmpty: Boolean by mutableStateOf(false)
    var yourAccountDateOfBirth: String by mutableStateOf("")
    var yourAccountDateOfBirthEmpty: Boolean by mutableStateOf(false)
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    var yourAccountPartnerName: String by mutableStateOf("")
    var yourAccountPartnerNameEmpty: Boolean by mutableStateOf(false)
    var yourAccountPassword: String by mutableStateOf("................")
    var yourAccountPasswordEmpty: Boolean by mutableStateOf(false)
    var isEditable: MutableState<Boolean> = mutableStateOf(false)
    val isYourAccountScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    var getUserDetailResponse: MutableLiveData<NetworkResult<GetUserDetailsResponse>> =
        MutableLiveData()
    var updateUserProfileResponse: MutableLiveData<NetworkResult<UpdateUserProfileResponse>> =
        MutableLiveData()


    fun getUserDetails(getUserDetailsRequest: GetUserDetailsRequest) {
        getUserDetailResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.getUserDetails(getUserDetailsRequest).collect {
                getUserDetailResponse.value = it
            }
        }
    }

    fun updateUserProfile(
        userId: Int,
        name: MultipartBody.Part,
        email: MultipartBody.Part,
        password: MultipartBody.Part,
        number: MultipartBody.Part,
        address: MultipartBody.Part,
        dateOfBirth: MultipartBody.Part,
        partnerName: MultipartBody.Part,
        imgFileName1: MultipartBody.Part? = null,
        imgFileName2: MultipartBody.Part? = null,
        type: MultipartBody.Part,
    ) {
        updateUserProfileResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.updateUserProfile(
                userId,
                name,
                email,
                password,
                number,
                address,
                dateOfBirth,
                partnerName,
                imgFileName1,
                imgFileName2,
                type
            ).collect {
                updateUserProfileResponse.value = it
            }
        }
    }
}
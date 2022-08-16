package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.repository.YourAccountRepository
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Abhin.
 */
@HiltViewModel
class YourAccountViewModel @Inject constructor(
    private val yourAccountRepository: YourAccountRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isLoading: Boolean by mutableStateOf(false)
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    var isRational: Boolean by mutableStateOf(false)
    var isEditable: MutableState<Boolean> = mutableStateOf(false)
    val isYourAccountScreen: MutableLiveData<Boolean> = MutableLiveData(false)

    //  surrogate
    var textEmailVerify: String by mutableStateOf("")
    var surrogateFullName: String by mutableStateOf("")
    var surrogateImg: String by mutableStateOf("")
    var yourAccountFullNameEmpty: Boolean by mutableStateOf(false)
    var surrogatePhoneNumber: String by mutableStateOf("")
    var yourAccountPhoneNumberEmpty: Boolean by mutableStateOf(false)
    var surrogateEmail: String by mutableStateOf("")
    var yourAccountEmailIsValid: Boolean by mutableStateOf(false)
    var yourAccountEmailEmpty: Boolean by mutableStateOf(false)
    var surrogateHomeAddress: String by mutableStateOf("")
    var yourAccountHomeAddressEmpty: Boolean by mutableStateOf(false)
    var surrogateDateOfBirth: String by mutableStateOf("")
    var yourAccountDateOfBirthEmpty: Boolean by mutableStateOf(false)
    var surrogatePartnerName: String by mutableStateOf("")
    var yourAccountPartnerNameEmpty: Boolean by mutableStateOf(false)
    var yourAccountPassword: String by mutableStateOf("................")
    var yourAccountPasswordEmpty: Boolean by mutableStateOf(false)
    var getUserDetailResponseSurrogate: MutableLiveData<NetworkResult<GetUserDetailsSurrogateResponse>> =
        MutableLiveData()
    private var updateUserProfileResponse: MutableLiveData<NetworkResult<UpdateUserProfileResponse>> =
        MutableLiveData()
    var isSurrogateApiCalled: Boolean by mutableStateOf(false)

    //    parent
    var parentFullName: String by mutableStateOf("")
    var parentImg1: String by mutableStateOf("")
    var parentImg2: String by mutableStateOf("")
    var parentPhoneNumber: String by mutableStateOf("")
    var parentPartnerName: String by mutableStateOf("")
    var parentDateOfBirth: String by mutableStateOf("")
    var parentHomeAddress: String by mutableStateOf("")
    var parentEmail: String by mutableStateOf("")
    var getUserDetailResponseParent: MutableLiveData<NetworkResult<GetUserDetailsParentResponse>> =
        MutableLiveData()
    var isParentClicked: Boolean by mutableStateOf(true)
    var isMotherClicked: Boolean by mutableStateOf(false)
    var isParentApiCalled: Boolean by mutableStateOf(false)



    fun getUserDetailsSurrogate(getUserDetailsRequestSurrogate: GetUserDetailsSurrogateRequest) {
        getUserDetailResponseSurrogate.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.getUserDetailsSurrogate(getUserDetailsRequestSurrogate).collect {
                getUserDetailResponseSurrogate.value = it
            }
        }
    }

    fun getUserDetailsParent(getUserDetailsRequestParent: GetUserDetailsParentRequest) {
        getUserDetailResponseParent.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.getUserDetailsParent(getUserDetailsRequestParent).collect {
                getUserDetailResponseParent.value = it
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
                type).collect {
                updateUserProfileResponse.value = it
            }
        }
    }
}
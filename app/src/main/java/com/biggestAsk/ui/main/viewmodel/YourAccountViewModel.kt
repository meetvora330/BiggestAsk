package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.repository.YourAccountRepository
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.util.PathUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class YourAccountViewModel @Inject constructor(
    private val yourAccountRepository: YourAccountRepository,
    application: Application,
) : AndroidViewModel(application) {

    var isLoading: Boolean by mutableStateOf(false)
    var isSurrogateDataLoading: Boolean by mutableStateOf(false)
    var isIntendedProfileDataLoading: Boolean by mutableStateOf(false)
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    var isRational: Boolean by mutableStateOf(false)
    var isEditable: MutableState<Boolean> = mutableStateOf(false)
    val isYourAccountScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val bitmap = mutableStateOf<Bitmap?>(null)
    var imageData: Uri? = (null)
    var uriPath: String? = null
    val isImagePresent = mutableStateOf(false)
    var intendedProfileResponseQuestionList = mutableStateListOf<QuestionAn>()

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
    var updateUserProfileResponse: MutableLiveData<NetworkResult<UpdateUserProfileResponse>> =
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
    var getIntendedProfileResponse: MutableLiveData<NetworkResult<GetIntendedProfileResponse>> = MutableLiveData()


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

    fun getImage(context: Context) {
        viewModelScope.launch {
            imageData.let {
                val uri = it
                Log.e("uri", "AddCommunityDialog: $uri")
                uriPath = uri?.let { it1 -> PathUtil.getPath(context, it1) }
                Log.e("uriPath", "AddCommunityDialog: $uriPath")
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source =
                        uri?.let { it1 -> ImageDecoder.createSource(context.contentResolver, it1) }
                    bitmap.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                }
            }
        }
    }

    fun updateUserProfile(
        userId: Int,
        name: MultipartBody.Part,
        email: MultipartBody.Part,
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

    fun getIntendedParentProfile(type:String,userId: Int) {
        getIntendedProfileResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.getIntendedParentProfile(type = type, user_id = userId).collect{
                getIntendedProfileResponse.value = it
            }
        }
    }
}
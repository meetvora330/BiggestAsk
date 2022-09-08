package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.* // ktlint-disable no-wildcard-imports
import com.biggestAsk.data.repository.YourAccountRepository
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.util.PathUtil
import com.biggestAsk.util.PreferenceProvider
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
    var isSurrogateDataLoading: Boolean by mutableStateOf(false)
    var isParentDataLoading: Boolean by mutableStateOf(false)
    var isAnsweredQuestionLoading: Boolean by mutableStateOf(false)
    var isIntendedProfileDataLoading: Boolean by mutableStateOf(false)
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    var isRational: Boolean by mutableStateOf(false)
    var isEditable: MutableState<Boolean> = mutableStateOf(false)
    val isYourAccountScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    val bitmapImage1 = mutableStateOf<Bitmap?>(null)
    val bitmapImage2 = mutableStateOf<Bitmap?>(null)
    var imageData: Uri? = (null)
    var uriPathParent: String? = null
    var uriPathMother: String? = null
    val isImagePresent = mutableStateOf(false)
    var getAnsweredQuestionListResponse: MutableLiveData<NetworkResult<GetAnsweredQuestionListResponse>> =
        MutableLiveData()
    var questionAnsweredList = mutableStateListOf<DataXXX>()
    var questionAnsweredDaysList = mutableStateListOf<Int>()

    //  surrogate
    var surrogateFullName: String by mutableStateOf("")
    var surrogateImg: String by mutableStateOf("")
    var yourAccountFullNameEmpty: Boolean by mutableStateOf(false)
    var phoneNumberMinimumValidate: Boolean by mutableStateOf(false)
    var parentPartnerPhoneNumberMinimumValidate: Boolean by mutableStateOf(false)
    var phoneNumberMaximumValidate: Boolean by mutableStateOf(false)
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
    var surrogateGender: String by mutableStateOf("male")
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
    var parentPartnerPhoneNumber: String by mutableStateOf("")
    var parentPartnerName: String by mutableStateOf("")
    var parentDateOfBirth: String by mutableStateOf("")
    var parentPartnerDateOfBirth: String by mutableStateOf("")
    var parentHomeAddress: String by mutableStateOf("")
    var parentPartnerHomeAddress: String by mutableStateOf("")
    var parentEmail: String by mutableStateOf("")
    var getUserDetailResponseParent: MutableLiveData<NetworkResult<GetUserDetailsParentResponse>> =
        MutableLiveData()
    var parentGender: String by mutableStateOf("")
    var parentPartnerGender: String by mutableStateOf("")
    var isParentClicked: Boolean by mutableStateOf(true)
    var isGenderSelected: Boolean by mutableStateOf(false)
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

    fun getImage(context: Context) {
        viewModelScope.launch {
            imageData.let {
                val uri = it
                Log.e("uri", "AddCommunityDialog: $uri")
                if (PreferenceProvider(context).getValue("type", "") == "parent") {
                    if (isParentClicked) {
                        uriPathParent = uri?.let { it1 -> PathUtil.getPath(context, it1) }
                    }
                    if (isMotherClicked) {
                        uriPathMother = uri?.let { it1 -> PathUtil.getPath(context, it1) }
                    }
                } else {
                    uriPathParent = uri?.let { it1 -> PathUtil.getPath(context, it1) }
                }
                Log.e("uriPath", "AddCommunityDialog: $uriPathParent")
                if (Build.VERSION.SDK_INT < 29) {
                    if (isParentClicked) {
                        bitmapImage1.value =
                            getResizedBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver,
                                uri), 512, 512)
                    }
                    if (isMotherClicked) {
                        bitmapImage2.value =
                            getResizedBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver,
                                uri), 512, 512)
                    }
                } else {
                    val source =
                        uri?.let { it1 -> ImageDecoder.createSource(context.contentResolver, it1) }
                    val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    if (isParentClicked) {
                        bitmapImage1.value = bitmap?.let { it1 -> getResizedBitmap(it1, 512, 512) }
                    }
                    if (isMotherClicked) {
                        bitmapImage2.value = bitmap?.let { it1 -> getResizedBitmap(it1, 512, 512) }
                    }
                }
            }
        }
    }

    private fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        // create a matrix for the manipulation
        val matrix = Matrix()

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)

        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }

    fun updateUserProfile(
        userId: Int,
        name: MultipartBody.Part,
        email: MultipartBody.Part,
        number: MultipartBody.Part,
        address: MultipartBody.Part,
        dateOfBirth: MultipartBody.Part,
        imgFileName1: MultipartBody.Part? = null,
        imgFileName2: MultipartBody.Part? = null,
        type: MultipartBody.Part,
        gender: MultipartBody.Part? = null,
        partner_name: MultipartBody.Part? = null,
        partner_phone: MultipartBody.Part? = null,
        partner_dob: MultipartBody.Part? = null,
        partner_address: MultipartBody.Part? = null,
        partner_gender: MultipartBody.Part? = null,
    ) {
        updateUserProfileResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            yourAccountRepository.updateUserProfile(
                userId = userId,
                name = name,
                email = email,
                number = number,
                address = address,
                gender = gender,
                dateOfBirth = dateOfBirth,
                imgFileName1 = imgFileName1,
                imgFileName2 = imgFileName2,
                type = type,
                partner_name = partner_name,
                partner_phone = partner_phone,
                partner_dob = partner_dob,
                partner_address = partner_address,
                partner_gender = partner_gender
            ).collect {
                updateUserProfileResponse.value = it
            }
        }
    }

    fun getYourAccountAnsweredQuestionList(
        userId: Int,
        type: String,
    ) {
        viewModelScope.launch {
            getAnsweredQuestionListResponse.value = NetworkResult.Loading()
            yourAccountRepository.getYourAccountAnsweredQuestionList(
                userId = userId,
                type = type
            ).collect {
                getAnsweredQuestionListResponse.value = it
            }
        }
    }
}

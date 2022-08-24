package com.biggestAsk.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biggestAsk.data.model.request.GetContactRequest
import com.biggestAsk.data.model.response.CreateContactResponse
import com.biggestAsk.data.model.response.GetContactResponse
import com.biggestAsk.data.model.response.GetContactResponseData
import com.biggestAsk.data.repository.ContactYourProviderRepository
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.util.PathUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Abhin.
 */
@HiltViewModel
class ContactYourProviderViewModel @Inject constructor(
    private val contactYourProviderRepository: ContactYourProviderRepository,
    application: Application,
) : AndroidViewModel(application) {
    val isContactProvidersScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    var createContactResponse: MutableLiveData<NetworkResult<CreateContactResponse>> =
        MutableLiveData()
    var getContactResponse: MutableLiveData<NetworkResult<GetContactResponse>> = MutableLiveData()
    var isLoading: Boolean by mutableStateOf(false)
    var image: String by mutableStateOf("")
    var isLoginEmailValid: Boolean by mutableStateOf(false)
    var contactList = mutableStateListOf<GetContactResponseData>()
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    var phoneErrorVisible: Boolean by mutableStateOf(false)
    val bitmap = mutableStateOf<Bitmap?>(null)
    var imageData: Uri? = (null)
    var uriPath: String? = ""
    val isImagePresent = mutableStateOf(false)
    var isDataNull: Boolean by mutableStateOf(false)


    fun createContact(
        title: MultipartBody.Part,
        agency_name: MultipartBody.Part,
        agency_email: MultipartBody.Part,
        agency_number: MultipartBody.Part,
        image: MultipartBody.Part? = null,
        user_id: MultipartBody.Part,
        type: MultipartBody.Part,
    ) {
        createContactResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            contactYourProviderRepository.createContact(
                title, agency_name, agency_email, agency_number, image, user_id, type
            ).collect {
                createContactResponse.value = it
            }
        }
    }

    fun getContact(getContactRequest: GetContactRequest) {
        getContactResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            contactYourProviderRepository.getContact(getContactRequest).collect {
                getContactResponse.value = it
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
}
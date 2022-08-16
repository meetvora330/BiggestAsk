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
import com.biggestAsk.data.model.request.GetCommunityRequest
import com.biggestAsk.data.model.response.CreateCommunityResponse
import com.biggestAsk.data.model.response.GetCommunityResponse
import com.biggestAsk.data.model.response.GetCommunityResponseData
import com.biggestAsk.data.repository.CommunityRepository
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
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    application: Application,
) : AndroidViewModel(application) {

    val isCommunityScreen: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLoading: Boolean by mutableStateOf(false)
    var getCommunityResponse: MutableLiveData<NetworkResult<GetCommunityResponse>> =
        MutableLiveData()
    var createCommunityResponse: MutableLiveData<NetworkResult<CreateCommunityResponse>> =
        MutableLiveData()
    var communityList = mutableStateListOf<GetCommunityResponseData>()
    var isPermissionAllowed: Boolean by mutableStateOf(false)
    val bitmap = mutableStateOf<Bitmap?>(null)
    var imageData: Uri? = (null)
    var uriPath: String? = ""
    val isImagePresent = mutableStateOf(false)


    fun createCommunity(
        title: MultipartBody.Part,
        description: MultipartBody.Part,
        forum_link: MultipartBody.Part,
        insta_link: MultipartBody.Part,
        image: MultipartBody.Part? = null,
        user_id: MultipartBody.Part,
        type: MultipartBody.Part,
    ) {
        createCommunityResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            communityRepository.createCommunity(
                title, description, forum_link, insta_link, image, user_id, type).collect {
                createCommunityResponse.value = it
            }
        }
    }

    fun getCommunity(getCommunityRequest: GetCommunityRequest) {
        getCommunityResponse.value = NetworkResult.Loading()
        viewModelScope.launch {
            communityRepository.getCommunity(getCommunityRequest).collect {
                getCommunityResponse.value = it
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
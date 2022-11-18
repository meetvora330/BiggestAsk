package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetCommunityRequest
import com.biggestAsk.data.model.response.CreateCommunityResponse
import com.biggestAsk.data.model.response.GetCommunityResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Abhin.
 * common repository
 */
class CommunityRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun createCommunity(
        title: MultipartBody.Part,
        description: MultipartBody.Part,
        forum_link: MultipartBody.Part,
        insta_link: MultipartBody.Part,
        image: MultipartBody.Part? = null,
        user_id: MultipartBody.Part,
        type: MultipartBody.Part,
    ): Flow<NetworkResult<CreateCommunityResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.createCommunity(
                    title, description, forum_link, insta_link, image, user_id, type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCommunity(getCommunityRequest: GetCommunityRequest): Flow<NetworkResult<GetCommunityResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getCommunity(
                    type = getCommunityRequest.type,
                    user_id = getCommunityRequest.user_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}
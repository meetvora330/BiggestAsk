package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetUserDetailsRequest
import com.biggestAsk.data.model.response.GetUserDetailsResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject


@ActivityRetainedScoped
class YourAccountRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getUserDetails(getUserDetailsRequest: GetUserDetailsRequest): Flow<NetworkResult<GetUserDetailsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getUserDetails(getUserDetailsRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateUserProfile(
        userId: Int,
        name: MultipartBody.Part,
        email: MultipartBody.Part,
        password: MultipartBody.Part,
        number: MultipartBody.Part,
        address: MultipartBody.Part,
        dateOfBirth: MultipartBody.Part,
        partnerName: MultipartBody.Part,
        imgFileName1: MultipartBody.Part?=null,
        imgFileName2: MultipartBody.Part?=null,
        type: MultipartBody.Part,
    ): Flow<NetworkResult<UpdateUserProfileResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.updateUserProfile(
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
                )
            })
        }.flowOn(Dispatchers.IO)
    }

}
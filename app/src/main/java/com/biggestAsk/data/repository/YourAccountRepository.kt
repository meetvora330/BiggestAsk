package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
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

    suspend fun getUserDetailsSurrogate(getUserDetailsSurrogateRequest: GetUserDetailsSurrogateRequest): Flow<NetworkResult<GetUserDetailsSurrogateResponse>> {
        return flow {
            emit(safeApiCall { apiService.getUserDetailsSurrogate(getUserDetailsSurrogateRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserDetailsParent(getUserDetailsParentRequest: GetUserDetailsParentRequest): Flow<NetworkResult<GetUserDetailsParentResponse>> {
        return flow {
            emit(safeApiCall { apiService.getUserDetailsParent(getUserDetailsParentRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateUserProfile(
        userId: Int,
        name: MultipartBody.Part,
        email: MultipartBody.Part,
        number: MultipartBody.Part,
        address: MultipartBody.Part,
        dateOfBirth: MultipartBody.Part,
        imgFileName1: MultipartBody.Part? = null,
        imgFileName2: MultipartBody.Part? = null,
        type: MultipartBody.Part,
        partner_phone: MultipartBody.Part? = null,
        partner_dob: MultipartBody.Part? = null,
        partner_address: MultipartBody.Part? = null
    ): Flow<NetworkResult<UpdateUserProfileResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.updateUserProfile(
                    userId = userId,
                    name = name,
                    email = email,
                    number = number,
                    address = address,
                    date_of_birth = dateOfBirth,
                    imgFileName1 = imgFileName1,
                    imgFileName2 = imgFileName2,
                    type = type,
                    partner_phone = partner_phone,
                    partner_dob = partner_dob,
                    partner_address = partner_address
                )
            })
        }.flowOn(Dispatchers.IO)
    }


}
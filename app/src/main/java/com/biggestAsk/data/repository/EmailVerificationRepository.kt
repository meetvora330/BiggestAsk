package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class EmailVerificationRepository  @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context
) : BaseApiResponse(context){
    suspend fun sendOtp(sendOtpRequest: SendOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.sendOtp(sendOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }
}
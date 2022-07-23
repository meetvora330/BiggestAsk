package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.CheckOtpRequest
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyOtpRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {
    suspend fun resendOtp(sendOtpRequest: SendOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.resendOtp(sendOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun checkOtp(checkOtpRequest: CheckOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.checkOtp(checkOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }
}
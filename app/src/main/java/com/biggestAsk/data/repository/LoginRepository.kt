package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.LoginBodyRequest
import com.biggestAsk.data.model.response.LoginBodyResponse
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
class LoginRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context
) : BaseApiResponse(context) {
    suspend fun login(loginBodyRequest: LoginBodyRequest): Flow<NetworkResult<LoginBodyResponse>> {
        return flow {
            emit(safeApiCall { apiService.login(loginBodyRequest) })
        }.flowOn(Dispatchers.IO)
    }
}
package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.LogoutRequest
import com.biggestAsk.data.model.response.LogoutResponse
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
class LogoutRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {
    suspend fun logOut(logoutRequest: LogoutRequest): Flow<NetworkResult<LogoutResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.logOut(
                    type = logoutRequest.type,
                    user_id = logoutRequest.user_id)
            })
        }.flowOn(Dispatchers.IO)
    }
}
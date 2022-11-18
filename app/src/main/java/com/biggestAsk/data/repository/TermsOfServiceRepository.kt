package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.response.GetTermsOfServiceResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Abhin.
 * terms of service screen repository
 */
class TermsOfServiceRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getTermsOfService(): Flow<NetworkResult<GetTermsOfServiceResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getTermsOfService()
            })
        }.flowOn(Dispatchers.IO)
    }
}
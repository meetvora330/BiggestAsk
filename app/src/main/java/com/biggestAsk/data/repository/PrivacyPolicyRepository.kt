package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.response.GetPrivacyPolicyResponse
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
 * privacy policy screen repository
 */
class PrivacyPolicyRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getPrivacyPolicy(): Flow<NetworkResult<GetPrivacyPolicyResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getPrivacyPolicy()
            })
        }.flowOn(Dispatchers.IO)
    }
}
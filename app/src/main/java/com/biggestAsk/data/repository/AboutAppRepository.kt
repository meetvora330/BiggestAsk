package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.response.AboutAppResponse
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
 */
class AboutAppRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getAboutApp(): Flow<NetworkResult<AboutAppResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getAboutApp()
            })
        }.flowOn(Dispatchers.IO)
    }
}
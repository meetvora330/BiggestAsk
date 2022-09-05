package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.ResetMilestoneResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun resetMilestone(resetMilestoneRequest: ResetMilestoneRequest): Flow<NetworkResult<ResetMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.resetMilestone(
                    resetMilestoneRequest = resetMilestoneRequest
                )
            })
        }
    }

}
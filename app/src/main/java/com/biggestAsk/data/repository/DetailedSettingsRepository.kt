package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.NotificationStatusUpdateRequest
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetNotificationStatusResponse
import com.biggestAsk.data.model.response.ResetMilestoneResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailedSettingsRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun notificationStatusUpdate(
        notificationStatusUpdateRequest: NotificationStatusUpdateRequest
    ): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.notificationStatusUpdate(
                    notificationStatusUpdateRequest = notificationStatusUpdateRequest
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNotificationStatus(
        type: String,
        userId: Int
    ): Flow<NetworkResult<GetNotificationStatusResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getNotificationStatus(
                    user_id = userId,
                    type = type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetMilestone(
        resetMilestoneRequest: ResetMilestoneRequest
    ): Flow<NetworkResult<ResetMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.resetMilestone(resetMilestoneRequest = resetMilestoneRequest)
            })
        }
    }
}
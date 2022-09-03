package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetNotificationRequest
import com.biggestAsk.data.model.response.GetNotificationResponse
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
class NotificationRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getNotification(getNotificationRequest: GetNotificationRequest): Flow<NetworkResult<GetNotificationResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getNotification(
                    type = getNotificationRequest.type,
                    user_id = getNotificationRequest.user_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}
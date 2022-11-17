package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.InviteSurrogateRequest
import com.biggestAsk.data.model.response.InviteSurrogateResponse
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

/**
 * Created by Abhin.
 */
@ActivityRetainedScoped
class InviteSurrogateRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    fun inviteSurrogate(
        userId: Int,
        inviteSurrogateRequest: InviteSurrogateRequest,
    ): Flow<NetworkResult<InviteSurrogateResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.inviteSurrogate(
                    user_id = userId,
                    inviteSurrogateRequest = inviteSurrogateRequest
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}
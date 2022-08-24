package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.response.GetAnsweredQuestionListResponse
import com.biggestAsk.data.model.response.GetHomeScreenQuestionResponse
import com.biggestAsk.data.model.response.IntendedParentQuestionResponse
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
class QuestionRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context
) : BaseApiResponse(context) {
    suspend fun getHomeScreenQuestion(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Flow<NetworkResult<GetHomeScreenQuestionResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getHomeScreenQuestion(
                    user_id = getPregnancyMilestoneRequest.user_id,
                    type = getPregnancyMilestoneRequest.type
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}
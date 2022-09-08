package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.ScreenQuestionStatusRequest
import com.biggestAsk.data.model.request.StoreBaseScreenQuestionAnsRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetFrequencyResponse
import com.biggestAsk.data.model.response.GetHomeScreenQuestionResponse
import com.biggestAsk.data.model.response.QuestionBankContentResponse
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
    private val apiService: ApiService, @ApplicationContext context: Context,
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

    suspend fun getFrequency(
        user_id: Int,
        type: String,
    ): Flow<NetworkResult<GetFrequencyResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getFrequency(user_id = user_id, type = type)
            })
        }
    }

    suspend fun storeAnsQuestionScreen(
        storeBaseScreenQuestionAnsRequest: StoreBaseScreenQuestionAnsRequest,
    ): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.storeBaseScreenQuestionAns(
                    storeBaseScreenQuestionAnsRequest
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getQuestionBankContent(): Flow<NetworkResult<QuestionBankContentResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getQuestionBankContent()
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun screenQuestionStatus(screenQuestionStatusRequest: ScreenQuestionStatusRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.screenQuestionStatus(screenQuestionStatusRequest) })
        }.flowOn(Dispatchers.IO)
    }

}
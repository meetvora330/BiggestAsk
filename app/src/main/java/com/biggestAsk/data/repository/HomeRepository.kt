package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getIntroInfo(): Flow<NetworkResult<IntroInfoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getIntroInfo() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUpdatedStatus(
        userId: Int,
        type: String,
        fcmToken: String,
    ): Flow<NetworkResult<UpdatedStatusResponse>> {
        return flow {
            emit(safeApiCall { apiService.getUpdatedStatus(userId, type, fcmToken) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updatePaymentStatus(updatePaymentStatusRequest: UpdatePaymentStatusRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.updatePaymentStatus(updatePaymentStatusRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getBaseScreenQuestion(): Flow<NetworkResult<List<BaseScreenQuestionResponse>>> {
        return flow {
            emit(safeApiCall { apiService.getBaseScreenQuestion() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun screenQuestionStatus(screenQuestionStatusRequest: ScreenQuestionStatusRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.screenQuestionStatus(screenQuestionStatusRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPregnancyMilestone(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Flow<NetworkResult<GetImportantQuestionResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getPregnancyMilestone(
                    user_id = getPregnancyMilestoneRequest.user_id,
                    type = getPregnancyMilestoneRequest.type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

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

    suspend fun getIntendedParentQuestionAns(intendedParentQuestionAnsRequest: IntendedParentQuestionAnsRequest): Flow<NetworkResult<IntendedParentQuestionResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getIntendedParentQuestionAns(
                    user_id = intendedParentQuestionAnsRequest.user_id,
                    partner_id = intendedParentQuestionAnsRequest.partner_id,
                    type = intendedParentQuestionAnsRequest.type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNearestMilestone(getNearestMilestoneRequest: GetNearestMilestoneRequest): Flow<NetworkResult<GetNearestMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getNearestMilestone(
                    user_id = getNearestMilestoneRequest.user_id,
                    type = getNearestMilestoneRequest.type,
                    partner_id = getNearestMilestoneRequest.partner_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMilestones(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Flow<NetworkResult<GetMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getMilestones(
                    getPregnancyMilestoneRequest.user_id,
                    getPregnancyMilestoneRequest.type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createMilestone(createMilestoneRequest: CreateMilestoneRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.createMilestone(createMilestoneRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetMilestone(resetMilestoneRequest: ResetMilestoneRequest): Flow<NetworkResult<ResetMilestoneResponse>> {
        return flow {
            emit(safeApiCall { apiService.resetMilestone(resetMilestoneRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editMilestone(editMilestoneRequest: EditMilestoneRequest): Flow<NetworkResult<EditMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.editMilestone(
                    editMilestoneRequest.type,
                    editMilestoneRequest.user_id,
                    editMilestoneRequest.milestone_id,
                    editMilestoneRequest.partner_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest: UpdateMilestoneAnsInfoRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun saveNote(saveNoteRequest: SaveNoteRequest): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.saveNote(saveNoteRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun storeMilestoneAns(
        images: ArrayList<MultipartBody.Part?>,
        user_id: MultipartBody.Part?,
        type: MultipartBody.Part?,
        milestone_id: MultipartBody.Part?,
    ): Flow<NetworkResult<UpdateUserProfileResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.storeMilestoneAns(
                    images,
                    user_id,
                    type,
                    milestone_id,
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateMilestoneImage(
        image_id: MultipartBody.Part?,
        image: MultipartBody.Part?,
    ): Flow<NetworkResult<UpdateImageResponse>> {
        return flow {
            emit(safeApiCall { apiService.updateMilestoneImage(image_id, image) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteMilestoneImage(
        image_id: DeleteMilestoneImageRequest,
    ): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.deleteMilestoneImage(image_id = image_id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun storeBaseScreenQuestionAns(
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

    suspend fun storeAnsImportantQuestion(
        storeAnsImportantQuestionRequest: StoreAnsImportantQuestionRequest,
    ): Flow<NetworkResult<CommonResponse>> {
        return flow {
            emit(safeApiCall { apiService.storeAnsImportantQuestion(storeAnsImportantQuestionRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPregnancyStatus(
        getNotificationRequest: GetNotificationRequest,
    ): Flow<NetworkResult<GetPregnancyMilestoneStatusResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getPregnancyStatus(getNotificationRequest = getNotificationRequest)
            })
        }
    }

    suspend fun getNotificationCount(
        type: String,
        userId: Int,
    ): Flow<NetworkResult<GetNotificationCountResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getNotificationCount(type = type, user_id = userId)
            })
        }
    }

    suspend fun askSurrogate(
        askSurrogateRequest: AskSurrogateRequest
    ):Flow<NetworkResult<CommonResponse>>{
        return flow {
            emit(safeApiCall {
                apiService.askSurrogate(askSurrogateRequest = askSurrogateRequest)
            })
        }
    }
}
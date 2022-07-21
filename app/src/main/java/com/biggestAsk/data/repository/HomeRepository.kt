package com.biggestAsk.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context
) : BaseApiResponse(context) {

    companion object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
    }

    private val dataStore = context.dataStore

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[onBoardingKey] ?: false
                onBoardingState
            }
    }

    suspend fun getIntroInfo(): Flow<NetworkResult<IntroInfoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getIntroInfo() })
        }.flowOn(Dispatchers.IO)
    }


    suspend fun sendOtp(sendOtpRequest: SendOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.sendOtp(sendOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resendOtp(sendOtpRequest: SendOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.resendOtp(sendOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun checkOtp(checkOtpRequest: CheckOtpRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.checkOtp(checkOtpRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registration(registrationBodyRequest: RegistrationBodyRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.registration(registrationBodyRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun login(loginBodyRequest: LoginBodyRequest): Flow<NetworkResult<LoginBodyResponse>> {
        return flow {
            emit(safeApiCall { apiService.login(loginBodyRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getBaseScreenQuestion(): Flow<NetworkResult<List<BaseScreenQuestionResponse>>> {
        return flow {
            emit(safeApiCall { apiService.getBaseScreenQuestion() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun screenQuestionStatus(screenQuestionStatusRequest: ScreenQuestionStatusRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.screenQuestionStatus(screenQuestionStatusRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPregnancyMilestone(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Flow<NetworkResult<GetPregnancyMilestoneResponse>> {
        return flow {
            emit(safeApiCall { apiService.getPregnancyMilestone(getPregnancyMilestoneRequest) })
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

    suspend fun getNearestMilestone(getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Flow<NetworkResult<GetNearestMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getNearestMilestone(
                    user_id = getPregnancyMilestoneRequest.user_id,
                    type = getPregnancyMilestoneRequest.type
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

    suspend fun createMilestone(createMilestoneRequest: CreateMilestoneRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.createMilestone(createMilestoneRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editMilestone(editMilestoneRequest: EditMilestoneRequest): Flow<NetworkResult<EditMilestoneResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.editMilestone(
                    editMilestoneRequest.type,
                    editMilestoneRequest.user_id,
                    editMilestoneRequest.milestone_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest: UpdateMilestoneAnsInfoRequest): Flow<NetworkResult<SendOtpResponse>> {
        return flow {
            emit(safeApiCall { apiService.updateMilestoneAnsInfo(updateMilestoneAnsInfoRequest) })
        }.flowOn(Dispatchers.IO)
    }

}
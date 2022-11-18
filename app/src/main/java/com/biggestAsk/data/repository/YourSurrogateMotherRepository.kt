package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.response.GetIntendedProfileResponse
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
 * your surrogate mother screen repository
 */
@ActivityRetainedScoped
class YourSurrogateMotherRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun getIntendedParentProfile(
        type: String,
        user_id: Int,
    ): Flow<NetworkResult<GetIntendedProfileResponse>> {
        return flow<NetworkResult<GetIntendedProfileResponse>> {
            emit(safeApiCall {
                apiService.getIntendedParentProfile(type = type, user_id = user_id)
            })
        }.flowOn(Dispatchers.IO)
    }
}
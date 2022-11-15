package com.biggestAsk.data.repository

import android.content.Context
import com.biggestAsk.data.model.request.GetContactRequest
import com.biggestAsk.data.model.request.UpdateContactRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.CreateContactResponse
import com.biggestAsk.data.model.response.GetContactResponse
import com.biggestAsk.data.source.network.ApiService
import com.biggestAsk.data.source.network.BaseApiResponse
import com.biggestAsk.data.source.network.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Abhin.
 */
class ContactYourProviderRepository @Inject constructor(
    private val apiService: ApiService, @ApplicationContext context: Context,
) : BaseApiResponse(context) {

    suspend fun createContact(
        title: MultipartBody.Part,
        agency_name: MultipartBody.Part,
        agency_email: MultipartBody.Part,
        agency_number: MultipartBody.Part,
        image: MultipartBody.Part? = null,
        user_id: MultipartBody.Part,
        type: MultipartBody.Part,
    ): Flow<NetworkResult<CreateContactResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.createContact(
                    title, agency_name, agency_email, agency_number, image, user_id, type
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getContact(getContactRequest: GetContactRequest): Flow<NetworkResult<GetContactResponse>> {
        return flow {
            emit(safeApiCall {
                apiService.getContact(
                    type = getContactRequest.type,
                    user_id = getContactRequest.user_id
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateContact(updateContactRequest: UpdateContactRequest):Flow<NetworkResult<CommonResponse>>{
        return flow {
            emit(safeApiCall {
                apiService.updateContact(
                    updateContactRequest = updateContactRequest
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}
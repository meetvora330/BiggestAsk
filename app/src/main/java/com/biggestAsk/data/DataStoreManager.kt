package com.biggestAsk.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.biggestAsk.data.model.response.LoginBodyResponse
import com.biggestAsk.data.repository.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {
    companion object PreferencesKey {
        val loginResponse = stringPreferencesKey("LOGIN_RESPONSE")
        val loginIsPaymentDone = booleanPreferencesKey("LOGIN_IS_PAYMENT_DONE")
        val loginIsQuestionAnswered = booleanPreferencesKey("LOGIN_IS_QUESTION_ANSWERED")
        val loginMessage = stringPreferencesKey("LOGIN_MESSAGE")
        val loginPartnerId = intPreferencesKey("LOGIN_PARTNER_ID")
        val loginStatus = stringPreferencesKey("LOGIN_STATUS")
        val loginType = stringPreferencesKey("LOGIN_TYPE")
        val loginUserId = intPreferencesKey("LOGIN_USER_ID")
    }

    suspend fun saveLoginDetails(loginBodyResponse: LoginBodyResponse) {
        context.dataStore.edit {
            it[loginIsPaymentDone] = loginBodyResponse.is_payment_done
            it[loginIsQuestionAnswered] = loginBodyResponse.is_question_answered
            it[loginMessage] = loginBodyResponse.message
            it[loginPartnerId] = loginBodyResponse.partner_id
            it[loginStatus] = loginBodyResponse.status
            it[loginType] = loginBodyResponse.type
            it[loginUserId] = loginBodyResponse.user_id
        }
    }

    fun getDetails(): Flow<LoginBodyResponse> {
        return context.dataStore.data.map {
            LoginBodyResponse(
                is_payment_done = it[loginIsPaymentDone] as Boolean,
                is_question_answered = it[loginIsQuestionAnswered] as Boolean,
                message = it[loginMessage] as String,
                partner_id = it[loginPartnerId] as Int,
                status = it[loginStatus] as String,
                type = it[loginType] as String,
                user_id = it[loginUserId] as Int
            )
        }
    }

    suspend fun storeLoginDetails(loginBodyResponse: LoginBodyResponse) {
        context.dataStore.edit {
            it[loginResponse] = loginBodyResponse.toString()
        }
    }

    fun getLoginDetails(): Flow<String> {
        return context.dataStore.data
            .map {
                it[loginResponse].toString()
            }
    }
}
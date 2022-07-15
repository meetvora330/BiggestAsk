package com.biggestAsk.data.source.network

import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.response.BaseScreenQuestionResponse
import com.biggestAsk.data.model.response.LoginBodyResponse
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST(Constants.SEND_OTP)
    suspend fun sendOtp(@Body email: SendOtpRequest): Response<SendOtpResponse>

    @POST(Constants.CHECK_OTP)
    suspend fun checkOtp(@Body checkOtpRequest: CheckOtpRequest): Response<SendOtpResponse>

    @POST(Constants.RESEND_OTP)
    suspend fun resendOtp(@Body email: SendOtpRequest): Response<SendOtpResponse>

    @POST(Constants.REGISTRATION)
    suspend fun registration(@Body registrationBodyRequest: RegistrationBodyRequest): Response<SendOtpResponse>

    @POST(Constants.LOGIN)
    suspend fun login(@Body loginBodyRequest: LoginBodyRequest): Response<LoginBodyResponse>

    @GET(Constants.GET_MONTH_QUESTION)
    suspend fun getBaseScreenQuestion(): Response<List<BaseScreenQuestionResponse>>

    @POST(Constants.STORE_QUESTION_ANS)
    suspend fun storeQuestionAns(@Body storeQuestionAnsRequest: StoreQuestionAnsRequest): Response<SendOtpResponse>

    @POST(Constants.SCREEN_QUESTION_STATUS)
    suspend fun screenQuestionStatus(@Body screenQuestionStatusRequest: ScreenQuestionStatusRequest): Response<SendOtpResponse>
}
package com.biggestAsk.data.source.network

import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.response.*
import com.biggestAsk.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.INTRO_SCREEN)
    suspend fun getIntroInfo(): Response<IntroInfoResponse>

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

    @POST(Constants.SCREEN_QUESTION_STATUS)
    suspend fun screenQuestionStatus(@Body screenQuestionStatusRequest: ScreenQuestionStatusRequest): Response<SendOtpResponse>

    @POST(Constants.GET_PREGNANCY_MILESTONE)
    suspend fun getPregnancyMilestone(@Body getPregnancyMilestoneRequest: GetPregnancyMilestoneRequest): Response<GetPregnancyMilestoneResponse>

    @GET(Constants.GET_HOME_SCREEN_QUESTION)
    suspend fun getHomeScreenQuestion(
        @Query("user_id") user_id: Int,
        @Query("type") type: String
    ): Response<GetHomeScreenQuestionResponse>

    @GET(Constants.INTENDED_PARTNER_QUESTION_ANS)
    suspend fun getIntendedParentQuestionAns(
        @Query("user_id") user_id: Int,
        @Query("partner_id") partner_id: Int,
        @Query("type") type: String
    ): Response<IntendedParentQuestionResponse>

    @GET(Constants.GET_NEAREST_MILESTONE)
    suspend fun getNearestMilestone(
        @Query("user_id") user_id: Int,
        @Query("type") type: String
    ): Response<GetNearestMilestoneResponse>

    @GET(Constants.GET_MILESTONE)
    suspend fun getMilestones(
        @Query("user_id") user_id: Int,
        @Query("type") type: String
    ): Response<GetMilestoneResponse>

    @POST(Constants.CREATE_MILESTONE)
    suspend fun createMilestone(@Body createMilestoneRequest: CreateMilestoneRequest): Response<SendOtpResponse>

    @GET(Constants.EDIT_MILESTONE)
    suspend fun editMilestone(
        @Query("type") type: String,
        @Query("user_id") user_id: Int,
        @Query("milestone_id") milestone_id: Int
    ): Response<EditMilestoneResponse>

    @POST(Constants.UPDATE_MILESTONE_ANS_INFO)
    suspend fun updateMilestoneAnsInfo(@Body updateMilestoneAnsInfoRequest: UpdateMilestoneAnsInfoRequest): Response<SendOtpResponse>
}
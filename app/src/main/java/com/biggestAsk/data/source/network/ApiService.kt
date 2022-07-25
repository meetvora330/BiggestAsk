package com.biggestAsk.data.source.network

import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.response.*
import com.biggestAsk.util.Constants
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

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

    @POST(Constants.SAVE_NOTE)
    suspend fun saveNote(@Body saveNoteRequest: SaveNoteRequest): Response<SendOtpResponse>

    @POST(Constants.INVITE_SURROGATE + "{id}")
    suspend fun inviteSurrogate(
        @Path("id") user_id: Int,
        @Body inviteSurrogateRequest: InviteSurrogateRequest
    ):Response<InviteSurrogateResponse>


    @POST(Constants.GET_USER_DETAIL)
    suspend fun getUserDetails(@Body user: GetUserDetailsRequest): Response<GetUserDetailsResponse>

    @Multipart
    @POST(Constants.USER_PROFILE_UPDATE + "{id}")
    suspend fun updateUserProfile(
        @Path("id") userId: Int?,
        @Part name: MultipartBody.Part?,
        @Part email: MultipartBody.Part?,
        @Part password: MultipartBody.Part?,
        @Part number: MultipartBody.Part?,
        @Part address: MultipartBody.Part?,
        @Part date_of_birth: MultipartBody.Part?,
        @Part partner_name: MultipartBody.Part?,
        @Part imgFileName1: MultipartBody.Part?,
        @Part imgFileName2: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
    ): Response<UpdateUserProfileResponse>

    @Multipart
    @POST(Constants.STORE_MILESTONE_ANS)
    suspend fun storeMilestoneAns(
        @Part note: MultipartBody.Part?,
        @Part images: List<MultipartBody.Part?>,
        @Part user_id: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
        @Part milestone_id: MultipartBody.Part?,
        @Part note_status: MultipartBody.Part?,
        @Part note_biggest: MultipartBody.Part?
    ): Response<UpdateUserProfileResponse>
}
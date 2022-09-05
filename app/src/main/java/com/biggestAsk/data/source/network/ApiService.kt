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

    @GET(Constants.GET_UPDATED_STATUS)
    suspend fun getUpdatedStatus(
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.TYPE) type: String,
    ): Response<UpdatedStatusResponse>

    @POST(Constants.SEND_OTP)
    suspend fun sendOtp(@Body email: SendOtpRequest): Response<CommonResponse>

    @POST(Constants.CHECK_OTP)
    suspend fun checkOtp(@Body checkOtpRequest: CheckOtpRequest): Response<CommonResponse>

    @POST(Constants.RESEND_OTP)
    suspend fun resendOtp(@Body email: SendOtpRequest): Response<CommonResponse>

    @POST(Constants.REGISTRATION)
    suspend fun registration(@Body registrationBodyRequest: RegistrationBodyRequest): Response<CommonResponse>

    @POST(Constants.LOGIN)
    suspend fun login(@Body loginBodyRequest: LoginBodyRequest): Response<LoginBodyResponse>

    @GET(Constants.GET_MONTH_QUESTION)
    suspend fun getBaseScreenQuestion(): Response<List<BaseScreenQuestionResponse>>

    @POST(Constants.SCREEN_QUESTION_STATUS)
    suspend fun screenQuestionStatus(@Body screenQuestionStatusRequest: ScreenQuestionStatusRequest): Response<CommonResponse>

    @GET(Constants.GET_IMPORTANT_QUESTION)
    suspend fun getPregnancyMilestone(
        @Query(Constants.TYPE) type: String,
        @Query(Constants.USER_ID) user_id: Int,
    ): Response<GetImportantQuestionResponse>

    @GET(Constants.GET_HOME_SCREEN_QUESTION)
    suspend fun getHomeScreenQuestion(
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.TYPE) type: String,
    ): Response<GetHomeScreenQuestionResponse>

    @GET(Constants.INTENDED_PARTNER_QUESTION_ANS)
    suspend fun getIntendedParentQuestionAns(
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.PARTNER_ID) partner_id: Int,
        @Query(Constants.TYPE) type: String,
    ): Response<IntendedParentQuestionResponse>

    @GET(Constants.GET_NEAREST_MILESTONE)
    suspend fun getNearestMilestone(
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.TYPE) type: String,
    ): Response<GetNearestMilestoneResponse>

    @GET(Constants.GET_MILESTONE)
    suspend fun getMilestones(
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.TYPE) type: String,
    ): Response<GetMilestoneResponse>

    @POST(Constants.CREATE_MILESTONE)
    suspend fun createMilestone(@Body createMilestoneRequest: CreateMilestoneRequest): Response<CommonResponse>

    @POST(Constants.RESET_MILESTONE)
    suspend fun resetMilestone(@Body resetMilestoneRequest: ResetMilestoneRequest): Response<ResetMilestoneResponse>

    @GET(Constants.EDIT_MILESTONE)
    suspend fun editMilestone(
        @Query(Constants.TYPE) type: String,
        @Query(Constants.USER_ID) user_id: Int,
        @Query(Constants.MILESTONE_ID) milestone_id: Int,
        @Query(Constants.PARTNER_ID) partner_id: Int,
    ): Response<EditMilestoneResponse>

    @POST(Constants.UPDATE_MILESTONE_ANS_INFO)
    suspend fun updateMilestoneAnsInfo(@Body updateMilestoneAnsInfoRequest: UpdateMilestoneAnsInfoRequest): Response<CommonResponse>

    @POST(Constants.SAVE_NOTE)
    suspend fun saveNote(@Body saveNoteRequest: SaveNoteRequest): Response<CommonResponse>

    @POST(Constants.INVITE_SURROGATE + "{id}")
    suspend fun inviteSurrogate(
        @Path(Constants.ID) user_id: Int,
        @Body inviteSurrogateRequest: InviteSurrogateRequest,
    ): Response<InviteSurrogateResponse>


    @POST(Constants.GET_USER_DETAIL)
    suspend fun getUserDetailsSurrogate(@Body user: GetUserDetailsSurrogateRequest): Response<GetUserDetailsSurrogateResponse>

    @POST(Constants.GET_USER_DETAIL)
    suspend fun getUserDetailsParent(@Body user: GetUserDetailsParentRequest): Response<GetUserDetailsParentResponse>

    @GET(Constants.GET_INTENDED_PROFILE)
    suspend fun getIntendedParentProfile(
        @Query("type") type: String,
        @Query("user_id") user_id: Int
    ): Response<GetIntendedProfileResponse>

    @Multipart
    @POST(Constants.USER_PROFILE_UPDATE + "{id}")
    suspend fun updateUserProfile(
        @Path(Constants.ID) userId: Int?,
        @Part name: MultipartBody.Part?,
        @Part email: MultipartBody.Part?,
        @Part number: MultipartBody.Part?,
        @Part address: MultipartBody.Part?,
        @Part gender: MultipartBody.Part?,
        @Part date_of_birth: MultipartBody.Part?,
        @Part imgFileName1: MultipartBody.Part?,
        @Part imgFileName2: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
        @Part parent_partner_name: MultipartBody.Part? = null,
        @Part parent_partner_phone: MultipartBody.Part?,
        @Part parent_partner_dob: MultipartBody.Part?,
        @Part parent_partner_address: MultipartBody.Part?,
        @Part parent_partner_gender: MultipartBody.Part?,
    ): Response<UpdateUserProfileResponse>

    @Multipart
    @POST(Constants.CREATE_CONTACT)
    suspend fun createContact(
        @Part title: MultipartBody.Part?,
        @Part agency_name: MultipartBody.Part?,
        @Part agency_email: MultipartBody.Part?,
        @Part agency_number: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part user_id: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
    ): Response<CreateContactResponse>

    @GET(Constants.GET_CONTACT)
    suspend fun getContact(
        @Query(Constants.TYPE) type: String,
        @Query(Constants.USER_ID) user_id: Int,
    ): Response<GetContactResponse>

    @GET(Constants.GET_COMMUNITY)
    suspend fun getCommunity(
        @Query(Constants.TYPE) type: String,
        @Query(Constants.USER_ID) user_id: Int,
    ): Response<GetCommunityResponse>

    @GET(Constants.GET_NOTIFICATION)
    suspend fun getNotification(
        @Query(Constants.TYPE) type: String,
        @Query(Constants.USER_ID) user_id: Int,
    ): Response<GetNotificationResponse>

    @Multipart
    @POST(Constants.CREATE_COMMUNITY)
    suspend fun createCommunity(
        @Part title: MultipartBody.Part?,
        @Part description: MultipartBody.Part?,
        @Part forum_link: MultipartBody.Part?,
        @Part insta_link: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
        @Part user_id: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
    ): Response<CreateCommunityResponse>

    @Multipart
    @POST(Constants.STORE_MILESTONE_ANS)
    suspend fun storeMilestoneAns(
        @Part images: List<MultipartBody.Part?>,
        @Part user_id: MultipartBody.Part?,
        @Part type: MultipartBody.Part?,
        @Part milestone_id: MultipartBody.Part?,
    ): Response<UpdateUserProfileResponse>

    @Multipart
    @POST(Constants.UPDATE_MILESTONE_IMAGE)
    suspend fun updateMilestoneImage(
        @Part image_id: MultipartBody.Part?,
        @Part image: MultipartBody.Part?,
    ): Response<UpdateImageResponse>

    @POST(Constants.DELETE_MILESTONE_IMAGE)
    suspend fun deleteMilestoneImage(@Body image_id: DeleteMilestoneImageRequest): Response<CommonResponse>

    @POST(Constants.UPDATE_PAYMENT_STATUS)
    suspend fun updatePaymentStatus(@Body updatePaymentStatusRequest: UpdatePaymentStatusRequest): Response<CommonResponse>

    @POST(Constants.STORE_BASE_SCREEN_QUESTION_ANS)
    suspend fun storeBaseScreenQuestionAns(@Body storeBaseScreenQuestionAnsRequest: StoreBaseScreenQuestionAnsRequest): Response<CommonResponse>

    @POST(Constants.STORE_ANS_IMPORTANT_QUESTION)
    suspend fun storeAnsImportantQuestion(@Body storeBaseScreenQuestionAnsRequest: StoreAnsImportantQuestionRequest): Response<CommonResponse>

    @GET(Constants.GET_QUESTION_SCREEN_QUESTION)
    suspend fun getYourAccountAnsweredQuestionList(
        @Query("type") type: String,
        @Query("user_id") user_id: Int
    ): Response<GetAnsweredQuestionListResponse>

    @GET(Constants.GET_FREQUENCY)
    suspend fun getFrequency(
        @Query("user_id") user_id: Int,
        @Query("type") type: String
    ): Response<GetFrequencyResponse>

    @GET(Constants.GET_QUESTION_BANK_CONTENT)
    suspend fun getQuestionBankContent(): Response<QuestionBankContentResponse>

    @GET(Constants.GET_ABOUT_APP)
    suspend fun getAboutApp(): Response<GetAboutAppResponse>

    @GET(Constants.GET_PRIVACY_POLICY)
    suspend fun getPrivacyPolicy(): Response<GetPrivacyPolicyResponse>

    @GET(Constants.GET_TERMS_OF_SERVICE)
    suspend fun getTermsOfService(): Response<GetTermsOfServiceResponse>

    @POST(Constants.GET_PREGNANCY_STATUS)
    suspend fun getPregnancyStatus(
        @Body getNotificationRequest: GetNotificationRequest
    ): Response<GetPregnancyMilestoneStatusResponse>

    @GET(Constants.GET_NOTIFICATION_COUNT)
    suspend fun getNotificationCount(
        @Query("type") type: String,
        @Query("user_id") user_id: Int
    ): Response<GetNotificationCountResponse>

    @POST(Constants.NOTIFICATION_STATUS_UPDATE)
    suspend fun notificationStatusUpdate(
        @Body notificationStatusUpdateRequest: NotificationStatusUpdateRequest
    ): Response<CommonResponse>

    @GET(Constants.GET_NOTIFICATION_STATUS)
    suspend fun getNotificationStatus(
        @Query("type") type: String,
        @Query("user_id") user_id: Int
    ): Response<GetNotificationStatusResponse>

    @GET(Constants.LOGOUT)
    suspend fun logOut(
        @Query("type") type: String,
        @Query("user_id") user_id: Int
    ):Response<LogoutResponse>
}
package com.biggestAsk.util


object Constants {
    const val UNAUTHORISED = 401
    const val SERVER_ERROR = 500

    const val REGEX_EMAIL =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}"
    const val REGEX_PHONE_PATTERN = "[\$&:;=\\\\?@#|/'<>.^*()%!_-]"

    const val PASSWORD_PATTERN_WITH_ONE_SPECIAL_CHARS =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"
    const val BASE_URL_BIGGEST_ASK = "https://biggestaskbackend.justcodenow.com/api/"
    const val INSTAGRAM_URL_PATTERN = "(?<=instagram.com\\/)[A-Za-z0-9_.]+"

    //API END POINTS
    const val INTRO_SCREEN = "get_intro_info"
    const val GET_UPDATED_STATUS = "get_updated_status"
    const val SEND_OTP = "sendotp"
    const val CHECK_OTP = "checkotp"
    const val RESEND_OTP = "resend_otp"
    const val REGISTRATION = "registration"
    const val LOGIN = "login"
    const val LOGOUT = "user_logout"
    const val GET_MONTH_QUESTION = "get_month_question"
    const val STORE_QUESTION_ANS = "store_question_ans"
    const val SCREEN_QUESTION_STATUS = "screen_question_status"
    const val GET_PREGNANCY_MILESTONE = "get_pregnancy_milestone"
    const val GET_IMPORTANT_QUESTION = "get_important_question"
    const val GET_HOME_SCREEN_QUESTION = "get_home_screen_question"
    const val INTENDED_PARTNER_QUESTION_ANS = "intended_partner_que_ans"
    const val GET_NEAREST_MILESTONE = "get_nearest_milestone"
    const val CREATE_MILESTONE = "create_milestone"
    const val RESET_MILESTONE = "reset_milsestone"
    const val GET_MILESTONE = "get_milestone"
    const val EDIT_MILESTONE = "edit_milestone"
    const val UPDATE_MILESTONE_ANS_INFO = "update_milestone_ans_info"
    const val SAVE_NOTE = "save_note"
    const val INVITE_SURROGATE = "invite_surrogate/"
    const val GET_USER_DETAIL = "get_user_detail"
    const val USER_PROFILE_UPDATE = "user_profile_update/"
    const val CREATE_CONTACT = "contact_create"
    const val CREATE_COMMUNITY = "community_create"
    const val GET_CONTACT = "get_contact"
    const val GET_COMMUNITY = "get_community"
    const val GET_NOTIFICATION = "get_notification"
    const val GET_ABOUT_APP = "about_app"
    const val GET_PRIVACY_POLICY = "privacy_policy"
    const val GET_TERMS_OF_SERVICE = "terms_of_service"
    const val GET_NOTIFICATION_DETAIL = "get_notification_detail"
    const val STORE_MILESTONE_ANS = "store_milestone_ans"
    const val UPDATE_MILESTONE_IMAGE = "update_milestone_image"
    const val DELETE_MILESTONE_IMAGE = "delete_milestone_image"
    const val UPDATE_PAYMENT_STATUS = "update_payment_status"
    const val STORE_BASE_SCREEN_QUESTION_ANS = "store_question_ans"
    const val STORE_ANS_IMPORTANT_QUESTION = "store_ans_important_question"
    const val GET_INTENDED_PROFILE = "get_intended_profile"
    const val GET_QUESTION_SCREEN_QUESTION = "get_qestion_screen_question"
    const val GET_FREQUENCY = "get_frequency"
    const val GET_QUESTION_BANK_CONTENT = "question_bank"
    const val GET_PREGNANCY_STATUS = "pregnancy_milestone_status"
    const val GET_NOTIFICATION_COUNT = "get_notification_count"
    const val NOTIFICATION_STATUS_UPDATE = "notification_status_update"
    const val GET_NOTIFICATION_STATUS = "get_notification_status"

    //preference data
    const val LOGIN_STATUS = "login_status"
    const val IS_INTRO_DONE = "isIntroDone"
    const val USER_LOGOUT = "user_logout"

    //login user type
    const val PARENT = "parent"
    const val PARENTS = "Parents"
    const val SURROGATE = "surrogate"
    const val SURROGATE_MOTHER = "Surrogate Mother"

    // error body
    const val ERROR_BODY_MESSAGE = "message"

    // HomeRepository
    const val ON_BOARDING_PREF = "on_boarding_pref"
    const val ON_BOARDING_COMPLETED = "on_boarding_completed"

    //ApiService
    const val USER_ID = "user_id"
    const val TYPE = "type"
    const val PARTNER_ID = "partner_id"
    const val MILESTONE_ID = "milestone_id"
    const val ID = "id"
    const val FCM_TOKEN = "fcm_token"
    const val NOTIFICATION_TOKEN = "notification_token"

    //Screen
    const val ROUTE_INTRO_SCREEN = "intro_screen"
    const val ROUTE_VERIFY_EMAIL_SCREEN = "verify_email"
    const val ROUTE_LOGIN_SCREEN = "login_screen"
    const val ROUTE_PAYMENT_SCREEN = "payment_screen"
    const val ROUTE_QUESTION_SCREEN = "question_screen"
    const val ROUTE_HOME_SCREEN = "home_screen"
    const val ROUTE_REGISTER_SCREEN = "register_screen"
    const val BOTTOM_NAV_MILESTONES_SCREEN = "milestones"
    const val BOTTOM_NAV_HOME_SCREEN = "home"
    const val BOTTOM_NAV_QUESTION_SCREEN = "question"
    const val BOTTOM_NAV_SURROGATE_PARENT_NOT_ASSIGN_SCREEN = "surrogate_parent_not_assign"

    //BottomNavItems
    const val MILESTONE = "Milestone"
    const val HOME = "Home"
    const val QUESTIONS = "Questions"

    //Route
    const val YOUR_SURROGATE_MOTHER = "your_surrogate_mother"
    const val INTENDED_PARENTS = "intended_parents"
    const val COMMUNITY = "community"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val CONTACT_YOUR_PROVIDER = "contact_your_providers"
    const val YOUR_SURROGATE_MOTHER_TITTLE = "Your Surrogate Mother"
    const val INTENDED_PARENTS_TITTLE = "Intended Parents"
    const val COMMUNITY_TITTLE = "Community"
    const val SETTINGS_TITTLE = "Settings"
    const val NOTIFICATIONS_TITTLE = "Notifications"
    const val CONTACT_YOUR_PROVIDER_TITTLE = "Contact Your Provider"
    const val MY_ACCOUNT = "my_account"

    //Common
    const val IMAGE_ID = "image_id"
    const val IMAGE_ARRAY = "image[]"
    const val IMAGE = "image"
    const val HELP_URL = "https://thebiggestask.com/the-biggest-ask-surrogacy-service/"
    const val HELP = "Help"
    const val ABOUT = "About"
    const val PRIVACY_POLICY = "Privacy Policy"
    const val TERMS_OF_SERVICE = "Terms of Service"
    const val DETAILED_SETTINGS = "Detailed Settings"
    const val ROUTE_ABOUT_APP = "about_app"
    const val ROUTE_PRIVACY_POLICY = "privacy_policy"
    const val ROUTE_TERMS_OF_SERVICE = "terms_of_service"
    const val ROUTE_DETAILED_SETTINGS = "detailed_setting"
    const val PLEASE_ADD_LOGO = "Please Add Logo"
    const val UPDATED_IMAGE = "updated_image"
    const val PREGNANCY_MILESTONE_STATUS = "pregnancy_milestone_status"
    const val USER_NAME = "user_name"
    const val PACKAGE = "package"
    const val NOTIFICATION_COUNT = "notification_count"
    const val ACTIVE = "active"
    const val IN_ACTIVE = "inactive"


    //EditMileStone Screen
    const val LONGITUDE = "00000000002"
    const val LATITUDE = "00005000505"
    const val IMAGE_LAUNCHER = "image/*"
    const val COMMON = "common"
    const val ENTER_NOTE = "Enter note"
    const val SELECT_IMAGE_FIRST = "Select image first"
    const val MAX_5_IMAGE = "You can select maximum 5 images."

    //YourSurrogateMother
    const val IS_SURROGATE_CONNECTED = "is_surrogate_connected"

    //Create Contact
    const val TITLE = "title"
    const val AGENCY_NAME_CREATE_CONTACT = "agency_name"
    const val AGENCY_EMAIL_CREATE_CONTACT = "agency_email"
    const val AGENCY_NUMBER_CREATE_CONTACT = "agency_number"

    //Create Community
    const val INST_LINK = "insta_link"
    const val FORUM_LINK = "forum_link"
    const val DESCRIPTION = "description"


}


package com.biggestAsk.util


object Constants {
    const val UNAUTHORISED = 401
    const val SERVER_ERROR = 500

    const val REGEX_EMAIL =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}"
    const val PASSWORD_PATTERN_WITH_ONE_SPECIAL_CHARS =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"

    const val BASE_URL_BIGGEST_ASK = "https://biggestaskbackend.justcodenow.com/api/"
    const val INTRO_SCREEN = "get_intro_info"
    const val SEND_OTP = "sendotp"
    const val CHECK_OTP = "checkotp"
    const val RESEND_OTP = "resend_otp"
    const val REGISTRATION = "registration"
    const val LOGIN = "login"
    const val GET_MONTH_QUESTION = "get_month_question"
    const val STORE_QUESTION_ANS = "store_question_ans"
    const val SCREEN_QUESTION_STATUS = "screen_question_status"
    const val GET_PREGNANCY_MILESTONE = "get_pregnancy_milestone"
    const val GET_HOME_SCREEN_QUESTION = "get_home_screen_question"
    const val INTENDED_PARTNER_QUESTION_ANS = "intended_partner_que_ans"
    const val GET_NEAREST_MILESTONE = "get_nearest_milestone"
    const val CREATE_MILESTONE = "create_milestone"
    const val GET_MILESTONE = "get_milestone"
    const val EDIT_MILESTONE = "edit_milestone"
    const val UPDATE_MILESTONE_ANS_INFO="update_milestone_ans_info"


    // error body
    const val ERROR_BODY_MESSAGE = "message"
}


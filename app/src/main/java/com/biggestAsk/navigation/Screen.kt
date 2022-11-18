package com.biggestAsk.navigation

import com.biggestAsk.util.Constants
import com.biggestAsk.util.Constants.ROUTE_LOGIN_SCREEN

const val EMAIL_VERIFICATION = "email_verification"
const val REGISTERED_EMAIL = "registered_email"

/**
 * sealed class with route defined inside
 */
sealed class Screen(val route: String) {
    object Intro : Screen(route = Constants.ROUTE_INTRO_SCREEN)
    object VerifyEmail : Screen(route = Constants.ROUTE_VERIFY_EMAIL_SCREEN)
    object Register : Screen(route = "register_screen/{$EMAIL_VERIFICATION}")
    object Verify : Screen(route = "verify_screen/{$EMAIL_VERIFICATION}")
    object Login : Screen(route = "login_screen/{$REGISTERED_EMAIL}")
    object PaymentScreen : Screen(route = Constants.ROUTE_PAYMENT_SCREEN)
    object QuestionScreen : Screen(route = Constants.ROUTE_QUESTION_SCREEN)

    fun emailVerification(
        email: String,
    ): String {
        return "verify_screen/$email"
    }

    fun login(
        email: String,
    ): String {
        return "$ROUTE_LOGIN_SCREEN/$email"
    }

    fun registerRoute(): String {
        return Constants.ROUTE_REGISTER_SCREEN
    }

    fun emailVerified(
        email: String,
    ): String {
        return "register_screen/$email"
    }
}

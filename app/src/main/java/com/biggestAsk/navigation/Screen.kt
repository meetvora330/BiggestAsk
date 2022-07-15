package com.biggestAsk.navigation
const val EMAIL_VERIFICATION = "email_verification"
sealed class Screen(val route: String) {
    object Intro : Screen(route = "intro_screen")
    object VerifyEmail : Screen(route = "verify_email")
    object Register : Screen(route = "register_screen/{$EMAIL_VERIFICATION}")
    object Verify : Screen(route = "verify_screen/{$EMAIL_VERIFICATION}")
    object Login : Screen(route = "login_screen")
    object PaymentScreen : Screen(route = "payment_screen")
    object QuestionScreen : Screen(route = "question_screen")
    object HomeScreen : Screen(route = "home_screen")

    fun emailVerification(
        email:String
    ):String{
        return "verify_screen/$email"
    }

    fun registerRoute():String{
        return "register_screen"
    }

    fun emailVerified(
        email:String
    ):String{
        return "register_screen/$email"
    }
}

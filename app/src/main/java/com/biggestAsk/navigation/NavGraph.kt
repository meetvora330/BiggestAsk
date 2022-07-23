@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.biggestAsk.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.EmailVerification
import com.biggestAsk.ui.introScreen.IntroScreen
import com.biggestAsk.ui.introScreen.onBoardItem
import com.biggestAsk.ui.loginScreen.LoginScreen
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.ui.paymentScreen.PaymentScreen
import com.biggestAsk.ui.questionScreen.QuestionScreenF
import com.biggestAsk.ui.registerScreen.RegisterScreen
import com.biggestAsk.ui.verifyOtpScreen.VerifyOtpScreen
import com.biggestAsk.util.PreferenceProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SetUpNavGraph(
    navHostController: NavHostController,
    viewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity,
    introViewModel: IntroViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    emailVerificationViewModel: EmailVerificationViewModel,
    verifyOtpViewModel: VerifyOtpViewModel
) {
    var startDestination = Screen.Intro.route
    val provider = PreferenceProvider(mainActivity.applicationContext)
    val isIntroDone = provider.getBooleanValue("isIntroDone", false)
    val paymentDone = provider.getValue("is_payment_screen", false)
    val questionDone = provider.getValue("question_screen", false)
    if (isIntroDone) {
        startDestination = Screen.VerifyEmail.route
    }
    if (paymentDone) {
        startDestination = Screen.PaymentScreen.route
    }
    if (questionDone) {
        startDestination = Screen.QuestionScreen.route
    }
    val pagerState = rememberPagerState()

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(
            Screen.Intro.route
        ) {
            val configuration = LocalConfiguration.current
            Log.i("TAG", "${configuration.screenHeightDp}")
            if (configuration.screenHeightDp > 700) {
                IntroScreen(
                    state = pagerState,
                    introViewModel = introViewModel,
                    items = onBoardItem,
                    scope = rememberCoroutineScope(),
                    modifier = Modifier.fillMaxWidth(),
                    modifierBox = Modifier.padding(bottom = 56.dp),
                    modifier_indicator = Modifier.padding(bottom = 80.dp),
                    modifier_img = Modifier.fillMaxHeight(0.6f),
                    navController = navHostController,
                    context = mainActivity.applicationContext,
                    mainActivity = mainActivity
                )
            } else {
                IntroScreen(
                    state = pagerState,
                    items = onBoardItem,
                    introViewModel = introViewModel,
                    scope = rememberCoroutineScope(),
                    modifier = Modifier.fillMaxWidth(),
                    modifierBox = Modifier.padding(bottom = 50.dp),
                    modifier_indicator = Modifier.padding(bottom = 70.dp),
                    modifier_img = Modifier.fillMaxHeight(0.5f),
                    navController = navHostController,
                    context = mainActivity.applicationContext,
                    mainActivity = mainActivity
                )
            }
        }
        composable(Screen.VerifyEmail.route) {
            EmailVerification(
                navHostController = navHostController,
                mainActivity = mainActivity,
                emailVerificationViewModel = emailVerificationViewModel
            )
        }
        composable(
            Screen.Register.route, arguments = listOf(
                navArgument(EMAIL_VERIFICATION) { type = NavType.StringType }
            )
        ) {
            RegisterScreen(
                navHostController,
                email = it.arguments?.getString(EMAIL_VERIFICATION).toString(),
                mainActivity = mainActivity,
                registerViewModel = registerViewModel
            )
        }
        composable(Screen.Verify.route, arguments = listOf(
            navArgument(name = EMAIL_VERIFICATION) { type = NavType.StringType }
        )) {
            val configuration = LocalConfiguration.current
            if (configuration.screenHeightDp > 700) {
                VerifyOtpScreen(
                    email = it.arguments?.getString(EMAIL_VERIFICATION).toString(),
                    navHostController, modifierTimerText = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(top = 24.dp, bottom = 130.dp),
                    viewModel = homeViewModel,
                    mainActivity = mainActivity,
                    verifyOtpViewModel = verifyOtpViewModel
                )
            } else {
                VerifyOtpScreen(
                    email = it.arguments?.getString(EMAIL_VERIFICATION).toString(),
                    navHostController, modifierTimerText = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(top = 24.dp, bottom = 30.dp),
                    viewModel = homeViewModel,
                    mainActivity = mainActivity,
                    verifyOtpViewModel = verifyOtpViewModel
                )
            }
        }
        composable(
            Screen.Login.route
        ) {
            LoginScreen(
                navHostController = navHostController,
                mainActivity = mainActivity,
                context = mainActivity.applicationContext,
                loginViewModel = loginViewModel
            )
        }
        composable(
            Screen.PaymentScreen.route
        ) {
            PaymentScreen(
                navHostController = navHostController,
                mainViewModel = viewModel,
                homeViewModel = homeViewModel,
                context = mainActivity.applicationContext
            )
        }
        composable(
            Screen.QuestionScreen.route
        ) {
            QuestionScreenF(
                homeViewModel = homeViewModel,
                mainActivity = mainActivity,
            )
        }
    }
}
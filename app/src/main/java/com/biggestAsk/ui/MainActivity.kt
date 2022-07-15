package com.biggestAsk.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.data.DataStoreManager
import com.biggestAsk.navigation.Screen
import com.biggestAsk.navigation.SetUpNavGraph
import com.biggestAsk.ui.base.BaseActivity
import com.biggestAsk.ui.introScreen.LockScreenOrientation
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.BasicStructureTheme
import com.biggestAsk.util.PreferenceProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Suppress("OPT_IN_IS_NOT_ENABLED")
class MainActivity : BaseActivity() {
    lateinit var navController: NavHostController
    private val homeViewModel: HomeViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()
    lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        homeViewModel.isLoadingIntro
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            dataStoreManager = DataStoreManager(this)
            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            val focusManager = LocalFocusManager.current
            ProvideWindowInsets(
                windowInsetsAnimationsEnabled = true
            ) {
                val isIntroDone = PreferenceProvider(this).getBooleanValue("isIntroDone", false)
                val paymentDone = PreferenceProvider(this).getValue("is_payment_screen", false)
                val questionDone = PreferenceProvider(this).getValue("question_screen", false)
                val isQuestionScreen =
                    PreferenceProvider(this).getValue("isQuestionAnswered", false)
                var startDestination = Screen.Intro.route
                if (isIntroDone) {
                    startDestination = Screen.VerifyEmail.route
                }
                if (paymentDone) {
                    startDestination = Screen.PaymentScreen.route
                }
                if (questionDone) {
                    startDestination = Screen.QuestionScreen.route
                }
                if (isQuestionScreen) {
                    finish()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    BasicStructureTheme {
                        navController = rememberNavController()
                        LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        focusManager.clearFocus()
                        SetUpNavGraph(
                            navHostController = navController,
                            viewModel = viewModel,
                            homeViewModel = homeViewModel,
                            this,
                            startDestination = startDestination,
                            dataStoreManager = dataStoreManager,
                            context = this
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalPagerApi::class)
fun DefaultPreview() {
    BasicStructureTheme {
//        IntroScreen(
//            state = rememberPagerState(),
//            items = onBoardItem,
//            scope = rememberCoroutineScope(),
//            modifier = Modifier.fillMaxWidth(),
//            modifierBox = Modifier.padding(bottom = 56.dp),
//            modifier_indicator = Modifier.padding(bottom = 85.dp),
//            modifier_img = Modifier.fillMaxHeight(0.6f),
//            navController = rememberNavController()
//        )
    }

}

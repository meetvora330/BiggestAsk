package com.biggestAsk.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.data.model.response.IntroInfoResponse
import com.biggestAsk.data.model.response.UpdatedStatusResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.navigation.SetUpNavGraph
import com.biggestAsk.ui.base.BaseActivity
import com.biggestAsk.ui.introScreen.LockScreenOrientation
import com.biggestAsk.ui.main.viewmodel.*
import com.biggestAsk.ui.ui.theme.BasicStructureTheme
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

class MainActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()
    private val introViewModel: IntroViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val emailVerificationViewModel: EmailVerificationViewModel by viewModels()
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels()
    private val frequencyViewModel: FrequencyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContent {
            val focusManager = LocalFocusManager.current
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            val navController = rememberNavController()
            val isInternetAvailable = remember {
                mutableStateOf(true)
            }
            val context = this
            focusManager.clearFocus()
            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
            LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            isInternetAvailable.value = isInternetAvailable(this)
            if (isInternetAvailable.value) {
                ProvideWindowInsets(
                    windowInsetsAnimationsEnabled = true
                ) {
                    val provider = PreferenceProvider(this)
                    val isIntroDone = provider.getBooleanValue(Constants.IS_INTRO_DONE, false)
                    if (!isIntroDone) {
                        LaunchedEffect(Unit) {
                            introViewModel.getIntroInfo()
                            introViewModel.getIntroInfoResponse.observe(this@MainActivity) {
                                if (it != null) {
                                    handleUserData(
                                        result = it,
                                        context = this@MainActivity,
                                        introViewModel = introViewModel
                                    )
                                }
                            }
                        }
                        if (!introViewModel.isIntroDataLoaded) {
                            IntroLoader()
                        }
                    } else {
                        when (provider.getValue(Constants.LOGIN_STATUS, "")) {
                            LoginStatus.PARTNER_NOT_ASSIGN.name.lowercase(Locale.getDefault()),
                            LoginStatus.MILESTONE_DATE_NOT_ADDED.name.lowercase(Locale.getDefault()),
                            LoginStatus.ON_BOARDING.name.lowercase(Locale.getDefault()) -> {
                                IntroLoader()
                            }
                            else -> {
                                if (!introViewModel.isUserStatusDataLoaded) {
                                    IntroLoader()
                                }
                            }
                        }
                        val userId = provider.getIntValue(Constants.USER_ID, 0)
                        val type = provider.getValue(Constants.TYPE, "")
                        if (userId != 0 && !type.isNullOrEmpty()) {
                            LaunchedEffect(Unit) {
                                introViewModel.getUpdatedStatus(userId, type)
                                introViewModel.getUpdatedStatusResponse.observe(this@MainActivity) {
                                    if (it != null) {
                                        handleUpdatedStatusData(
                                            result = it,
                                            context = this@MainActivity,
                                            introViewModel = introViewModel,
                                        )
                                    }
                                }
                            }
                        } else {
                            introViewModel.isIntroDataLoaded = true
                            introViewModel.isUserStatusDataLoaded = true
                        }

                    }
                    if (introViewModel.isIntroDataLoaded || introViewModel.isUserStatusDataLoaded) {
                        when (provider.getValue(Constants.LOGIN_STATUS, "")) {
                            LoginStatus.PARTNER_NOT_ASSIGN.name.lowercase(Locale.getDefault()),
                            LoginStatus.MILESTONE_DATE_NOT_ADDED.name.lowercase(Locale.getDefault()),
                            LoginStatus.ON_BOARDING.name.lowercase(Locale.getDefault()) -> {
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                BasicStructureTheme {
                                    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    focusManager.clearFocus()
                                    SetUpNavGraph(
                                        navHostController = navController,
                                        viewModel = viewModel,
                                        homeViewModel = homeViewModel,
                                        this,
                                        introViewModel = introViewModel,
                                        loginViewModel = loginViewModel,
                                        registerViewModel = registerViewModel,
                                        emailVerificationViewModel = emailVerificationViewModel,
                                        verifyOtpViewModel = verifyOtpViewModel,
                                        frequencyViewModel = frequencyViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp),
                        painter = painterResource(id = R.drawable.icon_internet_not_available),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        text = "Whoops!!",
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 28.sp,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        text = "No Internet connection was found.Check your connection or try again.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    Button(
                        onClick = {
                            isInternetAvailable.value = isInternetAvailable(context)
                        },
                        modifier = Modifier
                            .padding(top = 35.dp, start = 24.dp, end = 24.dp, bottom = 54.dp)
                            .fillMaxWidth()
                            .height(46.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        ),
                        shape = RoundedCornerShape(
                            topEnd = 0.dp,
                            bottomEnd = 10.dp,
                            topStart = 10.dp,
                            bottomStart = 0.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Custom_Blue,
                        )
                    ) {
                        Text(
                            text = "Try again",
                            color = Color.White,
                            style = MaterialTheme.typography.body2,
                            lineHeight = 28.sp,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W900
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun IntroLoader() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                painter = painterResource(id = R.drawable.ic_img_question_card_logo),
                contentDescription = ""
            )
            Text(
                text = stringResource(id = R.string.the_biggest_ask),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            )
            if (!introViewModel.isAPILoadingFailed) {
                CircularProgressIndicator(
                    // below line is use to add padding
                    // to our progress bar.
                    modifier = Modifier.padding(top = 15.dp),
                    // below line is use to add color
                    // to our progress bar.
                    color = colorResource(id = R.color.custom_blue),

                    // below line is use to add stroke
                    // width to our progress bar.
                    strokeWidth = Dp(value = 3F)
                )
            }
        }
    }
}


@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

private fun handleUserData(
    result: NetworkResult<IntroInfoResponse>,
    context: Context,
    introViewModel: IntroViewModel
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            introViewModel.isIntroDataLoaded = false
            introViewModel.isAPILoadingFailed = false
            introViewModel.isUserStatusDataLoaded = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.d("TAG", "handleUserData: ${result.data?.data!![0].title}")
            introViewModel.introInfoDetailList = result.data.data.toMutableList()
            introViewModel.isIntroDataLoaded = true
            introViewModel.isUserStatusDataLoaded = true
            introViewModel.isAPILoadingFailed = false
            Log.d(
                "TAG",
                "handleUserData: from viewModel ${introViewModel.introInfoDetailList[0].title}"
            )
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            introViewModel.isIntroDataLoaded = false
            introViewModel.isAPILoadingFailed = true
            introViewModel.isUserStatusDataLoaded = false
        }
    }
}

private fun handleUpdatedStatusData(
    result: NetworkResult<UpdatedStatusResponse>,
    context: Context,
    introViewModel: IntroViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            introViewModel.isUserStatusDataLoaded = false
            introViewModel.isIntroDataLoaded = false
            introViewModel.isAPILoadingFailed = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            val provider = PreferenceProvider(context)
            result.data?.let {
                provider.setValue(
                    Constants.LOGIN_STATUS,
                    it.status
                )
            }
            result.data?.partner_id?.let {
                provider.setValue(Constants.PARTNER_ID, it)
            }
            result.data?.image.let {
                if (it != null) {
                    PreferenceProvider(context).setValue("updated_image", it)
                    introViewModel.updatedImage = it
                }
            }
            result.data?.pregnancy_milestone_status.let {
                if (it != null) {
                    PreferenceProvider(context).setValue("pregnancy_milestone_status", it)
                    introViewModel.pregnancyMilestoneStatus = it
                }
            }
            introViewModel.isUserStatusDataLoaded = true
            introViewModel.isIntroDataLoaded = true
            introViewModel.isAPILoadingFailed = false
        }
        is NetworkResult.Error -> {
            // show error message
            introViewModel.isUserStatusDataLoaded = false
            introViewModel.isIntroDataLoaded = false
            introViewModel.isAPILoadingFailed = true
        }
    }
}

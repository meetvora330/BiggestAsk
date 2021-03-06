package com.biggestAsk.ui.verifyScreen

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.CheckOtpRequest
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun VerifyScreen(
    email: String,
    navHostController: NavHostController,
    modifierTimerText: Modifier,
    viewModel: HomeViewModel,
    mainActivity: MainActivity,
    mainViewModel: MainViewModel
) {
    var otpValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    var loadingText by remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        while (mainViewModel.ticks != 0) {
            delay(1.seconds)
            mainViewModel.ticks--
        }
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(6f),
            painter = painterResource(id = R.drawable.img_verify_screen),
            contentDescription = "",
            contentScale = ContentScale.Fit,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 24.dp, end = 24.dp),
            text = stringResource(id = R.string.tv_text_verify_screen_tittle),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W800,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 24.dp, end = 24.dp),
            text = stringResource(id = R.string.tv_text_verify_screen_sub_tittle),
            color = Color(0xFF75818F),
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 39.dp, start = 28.dp, end = 28.dp)
                .height(88.dp)
                .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(15.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = otpValue,
                onValueChange = { value ->
                    if (value.length <= 4) {
                        otpValue = value.filter { it.isDigit() }
                        mainViewModel.isOtpValueVerified = false
                    }
                },
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ), keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }), textStyle = MaterialTheme.typography.body2.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 40.sp
                )
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    repeat(4) { index ->
                        Spacer(modifier = Modifier.width(2.dp))
                        CharView(
                            index = index,
                            text = otpValue,
                            charColor = Color.Black,
                            charSize = 30.sp,
                            containerSize = 40.dp,
                            charBackground = Color.Transparent,
                            password = false,
                            passwordChar = "",
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
        if (mainViewModel.isOtpValueVerified) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.verify_screen_valid_otp_text),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                ),
            )
        }
        Text(
            modifier = modifierTimerText
                .alpha(if (mainViewModel.ticks < 1) 0f else 1f)
                .align(CenterHorizontally),
            text = "Resend Code: 0:${mainViewModel.ticks}",
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            color = Custom_Blue
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = {
                    if (TextUtils.isEmpty(otpValue) || otpValue.length < 4) {
                        mainViewModel.isOtpValueVerified = true
                        if (mainViewModel.ticks <= 0) {
                            mainViewModel.ticks = 60
                            mainViewModel.isOtpValueVerified = false
                            viewModel.resendOtp(SendOtpRequest(email = email))
                            otpValue = ""
                            viewModel.reSendOtpResponse.observe(mainActivity) {
                                if (it != null) {
                                    handleUserResendOtp(
                                        result = it,
                                        viewModel, mainViewModel, context, coroutineScope
                                    )
                                }
                            }
                            loadingText = "Re-sending OTP"
                            mainViewModel.ticks = 60
                        }

                    } else if (mainViewModel.ticks <= 0) {
                        viewModel.resendOtp(SendOtpRequest(email = email))
                        otpValue = ""
                        viewModel.reSendOtpResponse.observe(mainActivity) {
                            if (it != null) {
                                handleUserResendOtp(
                                    result = it,
                                    viewModel, mainViewModel, context, coroutineScope
                                )
                            }
                        }
                        loadingText = "Re-sending OTP"
                        mainViewModel.ticks = 60
                    } else {
                        viewModel.checkOtp(CheckOtpRequest(otp = otpValue, email = email))
                        viewModel.checkOtpResponse.observe(mainActivity) {
                            if (it != null) {
                                handleUserData(
                                    navHostController = navHostController,
                                    result = it,
                                    email = email,
                                    viewModel = viewModel,
                                    mainViewModel = mainViewModel,
                                    context = context
                                )
                            }
                            loadingText = "Verifying OTP"
                        }
                    }
                },
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Custom_Blue,
                )
            ) {
                Text(
                    text = if (mainViewModel.ticks < 1) stringResource(id = R.string.btn_text_verify_screen_resend) else stringResource(
                        id = R.string.btn_text_verify_screen_continue
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W900,
                    fontSize = 16.sp
                )
            }
        }
    }
    if (viewModel.isLoading) {
        ProgressBarTransparentBackground(loadingText)
    }
}

private fun handleUserResendOtp(
    result: NetworkResult<SendOtpResponse>,
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    context: Context,
    coroutineScope: CoroutineScope
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            viewModel.isLoading = true
            mainViewModel.isOtpValueVerified = false
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Toast
                .makeText(context, "OTP Resend Successfully", Toast.LENGTH_SHORT)
                .show()
            coroutineScope.launch {
                while (mainViewModel.ticks != 0) {
                    delay(1.seconds)
                    mainViewModel.ticks--
                }
            }
            viewModel.isLoading = false
            mainViewModel.isOtpValueVerified = false
        }
        is NetworkResult.Error -> {
            // show error message
            viewModel.isLoading = false
            mainViewModel.isOtpValueVerified = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

private fun handleUserData(
    navHostController: NavHostController,
    result: NetworkResult<SendOtpResponse>,
    email: String,
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            viewModel.isLoading = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            viewModel.isLoading = false
            navHostController.popBackStack(Screen.VerifyEmail.route, true)
            navHostController.navigate(Screen.Register.emailVerified(email = email))
            navHostController.popBackStack(Screen.Register.registerRoute(), true)
        }
        is NetworkResult.Error -> {
            // show error message
            viewModel.isLoading = false
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    charColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    charBackground: Color = Color.Transparent,
    password: Boolean = false,
    passwordChar: String = ""
) {
    val modifier = Modifier
        .width(containerSize)
        .background(charBackground)

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val char = when {
            index >= text.length -> "*"
            password -> passwordChar
            else -> text[index].toString()
        }
        Text(
            text = char,
            color = charColor,
            modifier = modifier.wrapContentHeight(),
            style = MaterialTheme.typography.body2.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 40.sp
            ),
            fontSize = charSize,
            textAlign = TextAlign.Center,
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyScreenPreview() {
//    VerifyScreen(email = "", rememberNavController(), modifierTimerText = Modifier, viewModel = HomeViewModel())
}
@file:Suppress("SameParameterValue")

package com.biggestAsk.ui.verifyOtpScreen

import android.content.Context
import android.text.TextUtils
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.CheckOtpRequest
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.activity.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.VerifyOtpViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

var ticker: Job? = null

@Composable
fun VerifyOtpScreen(
    email: String,
    navHostController: NavHostController,
    modifier: Modifier,
    mainActivity: MainActivity,
    verifyOtpViewModel: VerifyOtpViewModel,
) {
    var otpValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var loadingText by remember {
        mutableStateOf("")
    }
    val isSmall = if (verifyOtpViewModel.ticks < 10) "0" else ""
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        verifyOtpViewModel.minute = 120
        repeat(2) {
            verifyOtpViewModel.ticks = 60
            while (verifyOtpViewModel.ticks != 0) {
                delay(1.seconds)
                verifyOtpViewModel.ticks--
                verifyOtpViewModel.minute--
            }
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
            contentDescription = stringResource(id = R.string.content_description),
            contentScale = ContentScale.Fit
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
                        verifyOtpViewModel.isOtpValueVerified = false
                    }
                },
                enabled = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                textStyle = MaterialTheme.typography.body2.copy(
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
                            passwordChar = ""
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
        if (verifyOtpViewModel.isOtpValueVerified) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.verify_screen_valid_otp_text),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Text(
            modifier = modifier
                .alpha(if (verifyOtpViewModel.ticks < 1) 0f else 1f)
                .width(200.dp)
                .align(CenterHorizontally),
            text = if (verifyOtpViewModel.minute > 60) "Resend Code: 1:$isSmall${verifyOtpViewModel.ticks}" else "Resend Code: 0:$isSmall${verifyOtpViewModel.ticks}",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
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
                        verifyOtpViewModel.isOtpValueVerified = true
                        if (verifyOtpViewModel.ticks <= 0) {
                            verifyOtpViewModel.isOtpValueVerified = false
                            verifyOtpViewModel.resendOtp(SendOtpRequest(email = email))
                            otpValue = ""
                            verifyOtpViewModel.reSendOtpResponse.observe(mainActivity) {
                                if (it != null) {
                                    handleUserResendOtp(
                                        result = it,
                                        verifyOtpViewModel,
                                        context,
                                        coroutineScope
                                    )
                                }
                            }
                            loadingText = context.getString(R.string.resending_otp)
                        }
                    } else if (verifyOtpViewModel.ticks <= 0) {
                        verifyOtpViewModel.resendOtp(SendOtpRequest(email = email))
                        otpValue = ""
                        verifyOtpViewModel.reSendOtpResponse.observe(mainActivity) {
                            if (it != null) {
                                handleUserResendOtp(
                                    result = it,
                                    verifyOtpViewModel,
                                    context,
                                    coroutineScope
                                )
                            }
                        }
                        loadingText = context.getString(R.string.resending_otp)
                        verifyOtpViewModel.ticks = 60
                    } else {
                        verifyOtpViewModel.checkOtp(CheckOtpRequest(otp = otpValue, email = email))
                        verifyOtpViewModel.checkOtpResponse.observe(mainActivity) {
                            if (it != null) {
                                handleUserData(
                                    navHostController = navHostController,
                                    result = it,
                                    email = email,
                                    context = context,
                                    verifyOtpViewModel = verifyOtpViewModel
                                )
                            }
                            loadingText = context.getString(R.string.verify_otp)
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
                    backgroundColor = Custom_Blue
                )
            ) {
                Text(
                    text = if (verifyOtpViewModel.ticks < 1) stringResource(id = R.string.btn_text_verify_screen_resend) else stringResource(
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
    if (verifyOtpViewModel.isLoading) {
        ProgressBarTransparentBackground(loadingText)
    }
}

private fun handleUserResendOtp(
    result: NetworkResult<CommonResponse>,
    verifyOtpViewModel: VerifyOtpViewModel,
    context: Context,
    coroutineScope: CoroutineScope,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            verifyOtpViewModel.isLoading = true
            verifyOtpViewModel.isOtpValueVerified = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Toast
                .makeText(
                    context,
                    context.getString(R.string.otp_send_display_message),
                    Toast.LENGTH_SHORT
                )
                .show()
            ticker?.cancel()
            ticker = coroutineScope.launch {
                verifyOtpViewModel.minute = 120
                repeat(2) {
                    verifyOtpViewModel.ticks = 60
                    while (verifyOtpViewModel.ticks != 0) {
                        delay(1.seconds)
                        verifyOtpViewModel.ticks--
                        verifyOtpViewModel.minute--
                    }
                }
            }
            verifyOtpViewModel.isLoading = false
            verifyOtpViewModel.isOtpValueVerified = false
        }
        is NetworkResult.Error -> {
            // show error message
            verifyOtpViewModel.isLoading = false
            verifyOtpViewModel.isOtpValueVerified = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserData(
    navHostController: NavHostController,
    result: NetworkResult<CommonResponse>,
    email: String,
    verifyOtpViewModel: VerifyOtpViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            verifyOtpViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            verifyOtpViewModel.isLoading = false
            navHostController.popBackStack(Screen.Verify.route, true)
            navHostController.popBackStack(Screen.VerifyEmail.route, true)
            navHostController.navigate(Screen.Register.emailVerified(email = email))
            navHostController.popBackStack(Screen.Register.registerRoute(), true)
        }
        is NetworkResult.Error -> {
            // show error message
            verifyOtpViewModel.isLoading = false
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
    passwordChar: String = "",
) {
    val modifier = Modifier
        .width(containerSize)
        .background(charBackground)

    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            textAlign = TextAlign.Center
        )
    }
}

package com.biggestAsk.ui.emailVerification

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.SendOtpRequest
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Login_Sub_Tittle
import com.biggestAsk.ui.ui.theme.Text_Color
import com.example.biggestAsk.R

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EmailVerification(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity
) {
    val focusManager = LocalFocusManager.current
    var isEmailVerified: Boolean by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(Unit){
        homeViewModel.textEmailVerify = ""
    }
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img_login_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Text(
            modifier = Modifier
                .padding(top = 40.dp, end = 12.dp)
                .align(Alignment.TopEnd)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    navHostController.popBackStack()
                    navHostController.navigate(Screen.Login.route)
                },
            text = stringResource(id = R.string.email_verification_text_login),
            style = MaterialTheme.typography.body2,
            color = Color.Black,
            fontSize = 16.sp,
        )
        Box(
            modifier = Modifier
                .padding(top = 144.dp)
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_login_tittle),
                contentDescription = "login_img_up",
                modifier = Modifier
                    .width(92.dp)
                    .height(104.dp)
            )
        }
        Text(
            modifier = Modifier
                .padding(
                    top = 270.dp,
                )
                .align(Alignment.TopCenter),
            text = stringResource(id = R.string.login_tv_text_sub_tittle),
            textAlign = TextAlign.Center,
            color = Login_Sub_Tittle,
            style = MaterialTheme.typography.body2,
            fontSize = 32.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 40.sp
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 20.dp),
                    painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 52.dp, end = 52.dp, top = 18.dp),
                    text = stringResource(id = R.string.email_verification_tittle),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W700,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 44.dp, end = 44.dp, top = 8.dp),
                    text = stringResource(id = R.string.email_verification_sub_tittle),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF7F7D7C),
                    textAlign = TextAlign.Center
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 42.dp,
                            start = 24.dp,
                            end = 24.dp,
                        ),
                    value = homeViewModel.textEmailVerify,
                    onValueChange = {
                        homeViewModel.textEmailVerify = it
                        if (isEmailVerified) {
                            isEmailVerified = false
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.login_tv_email_text),
                            color = Text_Color
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = ET_Bg,
                        cursorColor = Custom_Blue,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        textColor = Color.Black,
                        trailingIconColor = Custom_Blue
                    ),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.body2,
                    isError = isEmailVerified,
                    trailingIcon = {
                        if (isEmailVerified) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colors.error
                            )
                        }
                    }
                )
                if (isEmailVerified) {
                    Text(
                        text = stringResource(id = R.string.email_verification_valid_email_text),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 26.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left
                    )
                }
                Button(
                    onClick = {
                        isEmailVerified =
                            if (TextUtils.isEmpty(homeViewModel.textEmailVerify)) {
                                true
                            } else if (!Patterns.EMAIL_ADDRESS.matcher(
                                    homeViewModel.textEmailVerify.trim()
                                ).matches()
                            ) {
                                true
                            } else {
                                homeViewModel.sendOtp(SendOtpRequest(homeViewModel.textEmailVerify))
                                homeViewModel.sendOtpResponse.observe(mainActivity) {
                                    if (it != null) {
                                        handleUserData(
                                            navHostController = navHostController,
                                            result = it,
                                            homeViewModel = homeViewModel,
                                            context = context
                                        )
                                    }
                                }
                                false
                            }
                    },
                    modifier = Modifier
                        .padding(top = 35.dp, start = 24.dp, end = 24.dp, bottom = 54.dp)
                        .fillMaxWidth()
                        .height(56.dp),
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
                        text = stringResource(id = R.string.email_verification_btn_verify_text),
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        lineHeight = 28.sp,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900
                    )
                }
            }
        }
        if (homeViewModel.isLoading) {
            ProgressBarTransparentBackground("Please wait....")
        }
    }
}

@Composable
fun ProgressBarTransparentBackground(loadingText: String, id: Int = R.color.custom_white) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = id))
            .clickable(indication = null, interactionSource = MutableInteractionSource()) {},
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                // below line is use to add padding
                // to our progress bar.
                modifier = Modifier.padding(top = 5.dp),
                // below line is use to add color
                // to our progress bar.
                color = colorResource(id = R.color.custom_blue),

                // below line is use to add stroke
                // width to our progress bar.
                strokeWidth = Dp(value = 4F)
            )
            Text(
                text = loadingText,
                color = Color.Black,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}

private fun handleUserData(
    navHostController: NavHostController,
    result: NetworkResult<SendOtpResponse>,
    homeViewModel: HomeViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            homeViewModel.isLoading = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            homeViewModel.isLoading = false
            navHostController.navigate(Screen.Verify.emailVerification(email = homeViewModel.textEmailVerify))
        }
        is NetworkResult.Error -> {
            // show error message
            homeViewModel.isLoading = false
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmailVerificationPreview() {
//    val context = LocalContext.current.applicationContext
//    EmailVerification(
//        navHostController = rememberNavController(),HomeViewModel(
//            homeRepository = HomeRepository(apiService  = )
//        )
//    )
}
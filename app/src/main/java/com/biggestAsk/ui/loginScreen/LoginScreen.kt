package com.biggestAsk.ui.loginScreen

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.data.DataStoreManager
import com.biggestAsk.data.model.request.LoginBodyRequest
import com.biggestAsk.data.model.response.LoginBodyResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.*
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navHostController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    viewModel: MainViewModel,
    mainActivity: MainActivity,
    context: Context,
    dataStoreManager: DataStoreManager
) {
    val focusManager = LocalFocusManager.current
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.img_login_bg),
        contentDescription = "",
        contentScale = ContentScale.FillBounds
    )
    LazyColumn {
        item {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 40.dp, end = 12.dp)
                        .align(Alignment.End)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navHostController.popBackStack()
                            navHostController.navigate(Screen.VerifyEmail.route)
                        },
                    text = stringResource(id = R.string.login_screen_text_signup),
                    style = MaterialTheme.typography.body2,
                    color = Color.Black,
                    fontSize = 16.sp,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp),
                    text = stringResource(id = R.string.login_tv_text_tittle),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 40.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.img_login_tittle),
                    contentDescription = "login_img_up",
                    modifier = Modifier
                        .width(92.dp)
                        .height(104.dp)
                        .align(CenterHorizontally)
                        .padding(top = 24.dp)
                )
                Text(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(alignment = CenterHorizontally),
                    text = stringResource(id = R.string.login_tv_text_sub_tittle),
                    textAlign = TextAlign.Center,
                    color = Login_Sub_Tittle,
                    style = MaterialTheme.typography.body2,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 40.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 53.dp, start = 24.dp),
                    text = stringResource(id = R.string.login_tv_email_text),
                    fontSize = 14.sp,
                    color = Text_Accept_Terms
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                    value = viewModel.loginTextEmail.trim(),
                    onValueChange = {
                        viewModel.loginTextEmail = it
                        viewModel.isLoginEmailEmpty = false
                        viewModel.isLoginEmailValid = false
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
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
                        textColor = Color.Black
                    ),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.body2,
                    isError = viewModel.isLoginEmailEmpty,
                    trailingIcon = {
                        if (viewModel.isLoginEmailEmpty) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colors.error
                            )
                        }
                    }
                )
                if (viewModel.isLoginEmailEmpty) {
                    Text(
                        text = stringResource(id = R.string.register_error_text_email),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 26.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left
                    )
                }
                if (viewModel.isLoginEmailValid) {
                    Text(
                        text = stringResource(id = R.string.register_error_text_valid_email),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, start = 26.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp),
                    text = stringResource(id = R.string.login_tv_password_text),
                    color = Text_Accept_Terms,
                    fontSize = 14.sp
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                    value = viewModel.loginTextPass,
                    onValueChange = {
                        viewModel.loginTextPass = it
                        viewModel.isLoginPassEmpty = false
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    textStyle = MaterialTheme.typography.body2,
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.login_tv_password_text),
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
                        textColor = Color.Black
                    ),
                    maxLines = 1,
                    isError = viewModel.isLoginPassEmpty,
                    trailingIcon = {
                        if (viewModel.isLoginPassEmpty) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colors.error
                            )
                        }
                    }
                )
                if (viewModel.isLoginPassEmpty) {
                    Text(
                        text = stringResource(id = R.string.register_error_text_pass),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 26.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Left
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp, end = 33.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                        },
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xFF3870C9),
                                fontSize = 16.sp
                            )
                        ) {
                            append(stringResource(id = R.string.login_tv_forgot_password_text))
                        }
                    },
                    fontSize = 16.sp,
                    textAlign = TextAlign.Right
                )
                Button(
                    onClick = {
                        when {
                            TextUtils.isEmpty(viewModel.loginTextEmail) && TextUtils.isEmpty(
                                viewModel.loginTextPass
                            ) -> {
                                viewModel.isLoginEmailEmpty = true
                                viewModel.isLoginPassEmpty = true
                            }
                            TextUtils.isEmpty(viewModel.loginTextEmail) -> {
                                viewModel.isLoginEmailEmpty = true
                            }
                            TextUtils.isEmpty(viewModel.loginTextPass) -> {
                                viewModel.isLoginPassEmpty = true
                            }
                            !Patterns.EMAIL_ADDRESS.matcher(viewModel.loginTextEmail).matches() -> {
                                viewModel.isLoginEmailValid = true
                            }
                            else -> {
                                val loginDetails = LoginBodyRequest(
                                    email = viewModel.loginTextEmail.trim(),
                                    password = viewModel.loginTextPass.trim()
                                )
                                homeViewModel.login(loginBodyRequest = loginDetails)
                                homeViewModel.loginScreen.observe(mainActivity) {
                                    if (it != null) {
                                        handleUserData(
                                            navHostController = navHostController,
                                            result = it,
                                            homeViewModel = homeViewModel,
                                            viewModel = viewModel,
                                            context = context,
                                            dataStoreManager = dataStoreManager
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 45.dp, start = 24.dp, end = 24.dp, bottom = 50.dp)
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
                        text = stringResource(id = R.string.login_btn_login_text),
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
    if (homeViewModel.isLoading) {
        ProgressBarTransparentBackground("Please wait....")
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun handleUserData(
    navHostController: NavHostController,
    result: NetworkResult<LoginBodyResponse>,
    homeViewModel: HomeViewModel,
    viewModel: MainViewModel,
    context: Context,
    dataStoreManager: DataStoreManager
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
            Log.i("TAG", result.message.toString())
            homeViewModel.isLoading = false
            Log.d("TAG", "handleUserData: ${result.data?.type}")
            GlobalScope.launch {
                result.data?.let {
                    val provider = PreferenceProvider(context)
                    provider.setValue("user_id", result.data.user_id)
                    provider.setValue("type", result.data.type)
                    provider.setValue("partner_id",result.data.partner_id)
                    Log.d("TAG", "handleUserData: ${result.data}")
                }
            }
            if (result.data?.type == "parent") {
                if (result.data.is_payment_done) {
                    if (result.data.is_question_answered) {
                        context.findActivity()?.finish()
                        context.startActivity(
                            Intent(
                                context,
                                HomeActivity::class.java
                            )
                        )
                    } else {
                        navHostController.popBackStack()
                        navHostController.navigate(Screen.QuestionScreen.route)
                    }
                } else {
                    navHostController.popBackStack()
                    navHostController.navigate(Screen.PaymentScreen.route)
                }
            } else {
                if (result.data!!.is_question_answered) {
                    context.findActivity()?.finish()
                    context.startActivity(
                        Intent(
                            context,
                            HomeActivity::class.java
                        )
                    )
                } else {
                    navHostController.popBackStack()
                    navHostController.navigate(Screen.QuestionScreen.route)
                }
            }

        }
        is NetworkResult.Error -> {
            // show error message
            homeViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
//    LoginScreen(navHostController = rememberNavController())
}
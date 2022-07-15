package com.biggestAsk.ui.registerScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.RegistrationBodyRequest
import com.biggestAsk.data.model.response.SendOtpResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.*
import com.example.biggestAsk.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    email: String,
    viewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, darkIcons = useDarkIcons
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(top = 56.dp)) {
            Text(
                text = stringResource(id = R.string.register_tittle),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W800,
                color = Color.Black,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            )
            Text(
                text = stringResource(id = R.string.register_sub_tittle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF75818F),
            )
        }
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (radio_btn_intended_parent, tv_intend_parent, radio_btn_surrogate_mother, tv_surrogate_mother) = createRefs()
            RadioButton(
                modifier = Modifier.constrainAs(radio_btn_intended_parent) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                selected = viewModel.selectedValueIntendParentRb.value,
                onClick = {
                    viewModel.selectedValueIntendParentRb.value = true
                    viewModel.selectedValueSurrogateMotherRb.value = false
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Custom_Blue
                )
            )
            Text(modifier = Modifier.constrainAs(tv_intend_parent) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, text = stringResource(id = R.string.register_radio_btn_intend_parents_text))
            RadioButton(
                modifier = Modifier.constrainAs(radio_btn_surrogate_mother) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                selected = viewModel.selectedValueSurrogateMotherRb.value,
                onClick = {
                    viewModel.selectedValueIntendParentRb.value = false
                    viewModel.selectedValueSurrogateMotherRb.value = true
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Custom_Blue
                )
            )
            Text(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .constrainAs(tv_surrogate_mother) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }, text = stringResource(id = R.string.register_radio_btn_surrogate_mother_text)
            )
            createHorizontalChain(
                radio_btn_intended_parent,
                tv_intend_parent,
                radio_btn_surrogate_mother,
                tv_surrogate_mother,
                chainStyle = ChainStyle.Packed
            )
        }
        ConstraintLayout {
            val (tvFullName, et_fullName, nameError, tv_email, et_email, emailError, tv_password, et_password, passError, tv_re_enter_pass, et_re_enter_pass, re_enter_pass_error, row_cb_term, btn_signUp) = createRefs()
            Text(
                text = stringResource(id = R.string.register_tv_name),
                modifier = Modifier
                    .padding(top = 32.dp, start = 24.dp)
                    .constrainAs(tvFullName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }, color = Color.Black
            )
            TextField(
                value = viewModel.textFullName,
                onValueChange = {
                    viewModel.textFullName = it
                    viewModel.isNameEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(et_fullName) {
                        start.linkTo(parent.start)
                        top.linkTo(tvFullName.bottom)
                        end.linkTo(parent.end)
                    },
                textStyle = MaterialTheme.typography.body2,
                isError = viewModel.isNameEmpty,
                trailingIcon = {
                    if (viewModel.isNameEmpty) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            "error",
                            tint = MaterialTheme.colors.error
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.register_et_hint_full_name),
                        color = Text_Color
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
            )
            if (viewModel.isNameEmpty) {
                Text(
                    text = stringResource(id = R.string.register_error_text_name),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(nameError) {
                            start.linkTo(et_fullName.start)
                            top.linkTo(et_fullName.bottom)
                        },
                    fontSize = 12.sp
                )
            }
            Text(
                text = stringResource(id = R.string.register_tv_email_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_email) {
                        top.linkTo(et_fullName.bottom)
                        start.linkTo(parent.start)
                    }, color = Color.Black
            )
            TextField(
                value = email,
                onValueChange = {
                    email
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(et_email) {
                        start.linkTo(parent.start)
                        top.linkTo(tv_email.bottom)
                        end.linkTo(parent.end)
                    },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.register_tv_email_text),
                        color = Text_Color
                    )
                },
                readOnly = true,
                textStyle = MaterialTheme.typography.body2,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
            )
            Text(
                text = stringResource(id = R.string.register_tv_password_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_password) {
                        top.linkTo(et_email.bottom)
                        start.linkTo(parent.start)
                    }, color = Color.Black
            )
            TextField(
                value = viewModel.textPass,
                onValueChange = {
                    viewModel.textPass = it
                    viewModel.isPassEmpty = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(et_password) {
                        start.linkTo(parent.start)
                        top.linkTo(tv_password.bottom)
                        end.linkTo(parent.end)
                    },
                isError = viewModel.isPassEmpty,
                trailingIcon = {
                    if (viewModel.isPassEmpty) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            "error",
                            tint = MaterialTheme.colors.error
                        )
                    }
                },
                textStyle = MaterialTheme.typography.body2,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.register_tv_password_text),
                        color = Text_Color
                    )
                },
                maxLines = 1,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )
            if (viewModel.isPassEmpty) {
                Text(
                    text = stringResource(id = R.string.register_error_text_pass),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(passError) {
                            start.linkTo(et_password.start)
                            top.linkTo(et_password.bottom)
                        },
                    fontSize = 12.sp
                )
            }
            Text(
                text = stringResource(id = R.string.register_tv_re_password_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_re_enter_pass) {
                        top.linkTo(et_password.bottom)
                        start.linkTo(parent.start)
                    }, color = Color.Black
            )
            TextField(
                value = viewModel.textReEnterPass,
                onValueChange = {
                    viewModel.textReEnterPass = it
                    viewModel.isRePassEmpty = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(et_re_enter_pass) {
                        start.linkTo(parent.start)
                        top.linkTo(tv_re_enter_pass.bottom)
                        end.linkTo(parent.end)
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                textStyle = MaterialTheme.typography.body2,
                isError = viewModel.isRePassEmpty,
                trailingIcon = {
                    if (viewModel.isRePassEmpty) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            "error",
                            tint = MaterialTheme.colors.error
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.register_tv_re_password_text),
                        color = Text_Color
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )
            if (viewModel.isRePassEmpty) {
                Text(
                    text = stringResource(id = R.string.register_error_text_re_enter_pass),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(re_enter_pass_error) {
                            start.linkTo(et_re_enter_pass.start)
                            top.linkTo(et_re_enter_pass.bottom)
                        },
                    fontSize = 12.sp
                )
            }
            Row(modifier = Modifier
                .constrainAs(row_cb_term) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(et_re_enter_pass.bottom)
                }
                .padding(top = 25.dp)) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    Checkbox(modifier = Modifier.padding(
                        start = 25.54.dp, top = 18.dp, bottom = 10.dp, end = 5.dp
                    ),
                        checked = viewModel.termCheckedState,
                        colors = CheckboxDefaults.colors(
                            checkedColor = CheckBox_Check, uncheckedColor = Color.DarkGray
                        ),
                        onCheckedChange = {
                            viewModel.termCheckedState = it
                        })
                }
                AnnotatedClickableText(context = context)
            }
            Button(onClick = {
                when {
                    TextUtils.isEmpty(viewModel.textFullName) && TextUtils.isEmpty(
                        viewModel.textPass
                    ) && TextUtils.isEmpty(
                        viewModel.textReEnterPass
                    ) -> {
                        viewModel.isNameEmpty = true
                        viewModel.isPassEmpty = true
                        viewModel.isRePassEmpty = true
                    }
                    TextUtils.isEmpty(viewModel.textFullName) -> {
                        viewModel.isNameEmpty = true
                    }
                    TextUtils.isEmpty(viewModel.textPass) -> {
                        viewModel.isPassEmpty = true
                    }
                    TextUtils.isEmpty(viewModel.textReEnterPass) -> {
                        viewModel.isRePassEmpty = true
                    }
                    !viewModel.termCheckedState -> {
                        Toast.makeText(
                            context, R.string.accept_terms, Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        if (viewModel.textPass == viewModel.textReEnterPass) {
                            val value =
                                if (viewModel.selectedValueIntendParentRb.value) "parent" else "surrogate"
                            val registrationBodyRequest = RegistrationBodyRequest(
                                type = value,
                                name = viewModel.textFullName,
                                email = email,
                                password = viewModel.textPass
                            )
                            homeViewModel.registration(registrationBodyRequest = registrationBodyRequest)
                            homeViewModel.registerScreen.observe(mainActivity) {
                                if (it != null) {
                                    handleUserData(
                                        navHostController = navHostController,
                                        result = it,
                                        homeViewModel = homeViewModel,
                                        context = context
                                    )
                                }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                R.string.password_not_match,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }, modifier = Modifier
                .constrainAs(btn_signUp) {
                    top.linkTo(row_cb_term.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(start = 24.dp, end = 24.dp, top = 25.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(56.dp), elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ), shape = RoundedCornerShape(30), colors = ButtonDefaults.buttonColors(
                backgroundColor = Custom_Blue,
            )) {
                Text(
                    text = stringResource(id = R.string.register_btn_signup_text),
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
            navHostController.popBackStack()
            navHostController.navigate(
                Screen.Login.route
            )
            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
        }
        is NetworkResult.Error -> {
            // show error message
            homeViewModel.isLoading = false
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

@Composable
fun AnnotatedClickableText(context: Context) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Text_Accept_Terms, fontSize = 16.sp
            )
        ) {
            append("Accept  ")
        }
        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = "URL", annotation = "https://thebiggestask.com/"
        )
        withStyle(
            style = SpanStyle(
                color = Custom_Blue,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            )
        ) {
            append("Term of Service")
        }
        pop()
    }

    ClickableText(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 15.dp, end = 40.dp, top = 20.dp),
        text = annotatedText,
        onClick = { offset ->
            // We check if there is an *URL* annotation attached to the text
            // at the clicked position
            annotatedText.getStringAnnotations(
                tag = "URL", start = offset, end = offset
            ).firstOrNull()?.let {
                val urlTerms = "https://thebiggestask.com/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlTerms)
                context.startActivity(intent)
            }
        })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
//    RegisterScreen(rememberNavController(), "")
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun RegisterSmallScreenPreview() {
//    RegisterScreen(rememberNavController(), "")
}

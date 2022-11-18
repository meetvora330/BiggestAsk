package com.biggestAsk.ui.registerScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
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
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.RegistrationBodyRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.activity.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.bottomNavScreen.SimpleDropDown
import com.biggestAsk.ui.main.viewmodel.EmailVerificationViewModel
import com.biggestAsk.ui.main.viewmodel.RegisterViewModel
import com.biggestAsk.ui.ui.theme.*
import com.biggestAsk.util.Constants
import com.example.biggestAsk.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

/**
 * register screen
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    email: String,
    registerViewModel: RegisterViewModel,
    mainActivity: MainActivity,
    emailVerificationViewModel: EmailVerificationViewModel,
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val suggestions = listOf("male", "female", "other")
    var selectedText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        registerViewModel.textFullName = ""
        registerViewModel.textPass = ""
        registerViewModel.textReEnterPass = ""
        registerViewModel.termCheckedState = false
    }

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
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
            RadioButton(modifier = Modifier.constrainAs(radio_btn_intended_parent) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, selected = registerViewModel.selectedValueIntendParentRb.value, onClick = {
                registerViewModel.selectedValueIntendParentRb.value = true
                registerViewModel.selectedValueSurrogateMotherRb.value = false
            }, colors = RadioButtonDefaults.colors(selectedColor = Custom_Blue))
            Text(modifier = Modifier.constrainAs(tv_intend_parent) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, text = stringResource(id = R.string.register_radio_btn_intend_parents_text))
            RadioButton(modifier = Modifier.constrainAs(radio_btn_surrogate_mother) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, selected = registerViewModel.selectedValueSurrogateMotherRb.value, onClick = {
                registerViewModel.selectedValueIntendParentRb.value = false
                registerViewModel.selectedValueSurrogateMotherRb.value = true
            }, colors = RadioButtonDefaults.colors(selectedColor = Custom_Blue))
            Text(modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(tv_surrogate_mother) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }, text = stringResource(id = R.string.register_radio_btn_surrogate_mother_text))
            createHorizontalChain(radio_btn_intended_parent,
                tv_intend_parent,
                radio_btn_surrogate_mother,
                tv_surrogate_mother,
                chainStyle = ChainStyle.Packed)
        }
        ConstraintLayout {
            val (tvFullName, et_fullName, tv_gender, drop_down_gender, nameError, tv_email, et_email, genderError, tv_password, et_password, passError, tv_re_enter_pass, et_re_enter_pass, re_enter_pass_error, row_cb_term, btn_signUp) = createRefs()
            Text(text = stringResource(id = R.string.register_tv_name),
                modifier = Modifier
                    .padding(top = 32.dp, start = 24.dp)
                    .constrainAs(tvFullName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                color = Color.Black)
            TextField(
                value = registerViewModel.textFullName,
                onValueChange = {
                    registerViewModel.textFullName = it
                    registerViewModel.isNameEmpty = false
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
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
                isError = registerViewModel.isNameEmpty,
                trailingIcon = {
                    if (registerViewModel.isNameEmpty) {
                        Icon(imageVector = Icons.Filled.Error,
                            contentDescription = stringResource(id = R.string.content_description),
                            tint = MaterialTheme.colors.error)
                    }
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.register_et_hint_full_name),
                        color = Text_Color)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent),
                maxLines = 1,
            )
            if (registerViewModel.isNameEmpty) {
                Text(text = stringResource(id = R.string.register_error_text_name),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(nameError) {
                            start.linkTo(et_fullName.start)
                            top.linkTo(et_fullName.bottom)
                        },
                    fontSize = 12.sp)
            }
            Text(text = stringResource(id = R.string.select_gender),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_gender) {
                        top.linkTo(et_fullName.bottom)
                        start.linkTo(parent.start)
                    },
                color = Color.Black)
            selectedText = SimpleDropDown(suggestions = suggestions,
                hint = stringResource(id = R.string.select_gender),
                modifier = Modifier
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(drop_down_gender) {
                        top.linkTo(tv_gender.bottom)
                        start.linkTo(parent.start)
                    },
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = Color.Black))
            if (selectedText == "" && registerViewModel.isGenderSelected) {
                Text(text = stringResource(id = R.string.select_gender),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(genderError) {
                            start.linkTo(drop_down_gender.start)
                            top.linkTo(drop_down_gender.bottom)
                        },
                    fontSize = 12.sp)
            }
            Text(text = stringResource(id = R.string.register_tv_email_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_email) {
                        top.linkTo(drop_down_gender.bottom)
                        start.linkTo(parent.start)
                    },
                color = Color.Black)
            TextField(
                value = email,
                onValueChange = {
                    email
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next),
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
                    Text(text = stringResource(id = R.string.register_tv_email_text),
                        color = Text_Color)
                },
                readOnly = true,
                textStyle = MaterialTheme.typography.body2,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent),
                maxLines = 1,
            )
            Text(text = stringResource(id = R.string.register_tv_password_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_password) {
                        top.linkTo(et_email.bottom)
                        start.linkTo(parent.start)
                    },
                color = Color.Black)
            TextField(
                value = registerViewModel.textPass,
                onValueChange = {
                    registerViewModel.textPass = it
                    registerViewModel.isPassEmpty = false
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
                isError = registerViewModel.isPassEmpty,
                trailingIcon = {
                    if (registerViewModel.isPassEmpty) {
                        Icon(imageVector = Icons.Filled.Error,
                            contentDescription = stringResource(id = R.string.content_description),
                            tint = MaterialTheme.colors.error)
                    }
                },
                textStyle = MaterialTheme.typography.body2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text(text = stringResource(id = R.string.register_tv_password_text),
                        color = Text_Color)
                },
                maxLines = 1,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent),
            )
            if (registerViewModel.isPassEmpty) {
                Text(text = stringResource(id = R.string.register_error_text_pass),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(passError) {
                            start.linkTo(et_password.start)
                            top.linkTo(et_password.bottom)
                        },
                    fontSize = 12.sp)
            }
            Text(text = stringResource(id = R.string.register_tv_re_password_text),
                modifier = Modifier
                    .padding(top = 20.dp, start = 24.dp)
                    .constrainAs(tv_re_enter_pass) {
                        top.linkTo(et_password.bottom)
                        start.linkTo(parent.start)
                    },
                color = Color.Black)
            TextField(
                value = registerViewModel.textReEnterPass,
                onValueChange = {
                    registerViewModel.textReEnterPass = it
                    registerViewModel.isRePassEmpty = false
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                textStyle = MaterialTheme.typography.body2,
                isError = registerViewModel.isRePassEmpty,
                trailingIcon = {
                    if (registerViewModel.isRePassEmpty) {
                        Icon(imageVector = Icons.Filled.Error,
                            contentDescription = stringResource(id = R.string.content_description),
                            tint = MaterialTheme.colors.error)
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                placeholder = {
                    Text(text = stringResource(id = R.string.register_tv_re_password_text),
                        color = Text_Color)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = ET_Bg,
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent),
            )
            if (registerViewModel.isRePassEmpty) {
                Text(text = stringResource(id = R.string.register_error_text_re_enter_pass),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 26.dp)
                        .constrainAs(re_enter_pass_error) {
                            start.linkTo(et_re_enter_pass.start)
                            top.linkTo(et_re_enter_pass.bottom)
                        },
                    fontSize = 12.sp)
            }
            Row(modifier = Modifier
                .constrainAs(row_cb_term) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(et_re_enter_pass.bottom)
                }
                .padding(top = 25.dp)) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    Checkbox(modifier = Modifier.padding(start = 25.54.dp,
                        top = 18.dp,
                        bottom = 10.dp,
                        end = 5.dp),
                        checked = registerViewModel.termCheckedState,
                        colors = CheckboxDefaults.colors(checkedColor = CheckBox_Check,
                            uncheckedColor = Color.DarkGray),
                        onCheckedChange = {
                            registerViewModel.termCheckedState = it
                        })
                }
                AnnotatedClickableText(context = context)
            }
            Button(onClick = {
                when {
                    TextUtils.isEmpty(registerViewModel.textFullName) && TextUtils.isEmpty(
                        selectedText) && TextUtils.isEmpty(registerViewModel.textPass) && TextUtils.isEmpty(
                        registerViewModel.textReEnterPass) -> {
                        registerViewModel.isNameEmpty = true
                        registerViewModel.isPassEmpty = true
                        registerViewModel.isRePassEmpty = true
                        registerViewModel.isGenderSelected = true
                    }
                    TextUtils.isEmpty(registerViewModel.textFullName) -> {
                        registerViewModel.isNameEmpty = true
                    }
                    TextUtils.isEmpty(selectedText) -> {
                        registerViewModel.isGenderSelected = true
                    }
                    TextUtils.isEmpty(registerViewModel.textPass) -> {
                        registerViewModel.isPassEmpty = true
                    }
                    TextUtils.isEmpty(registerViewModel.textReEnterPass) -> {
                        registerViewModel.isRePassEmpty = true
                    }
                    !registerViewModel.termCheckedState -> {
                        Toast.makeText(context, R.string.accept_terms, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        if (registerViewModel.textPass == registerViewModel.textReEnterPass) {
                            val value =
                                if (registerViewModel.selectedValueIntendParentRb.value) Constants.PARENT else Constants.SURROGATE
                            val registrationBodyRequest = RegistrationBodyRequest(type = value,
                                name = registerViewModel.textFullName,
                                email = email,
                                password = registerViewModel.textPass,
                                gender = selectedText.lowercase(Locale.ROOT))
                            registerViewModel.registration(registrationBodyRequest = registrationBodyRequest)
                            registerViewModel.registerScreen.observe(mainActivity) {
                                if (it != null) {
                                    handleUserData(
                                        navHostController = navHostController,
                                        result = it,
                                        registerViewModel = registerViewModel,
                                        emailVerificationViewModel = emailVerificationViewModel,
                                        email = email
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
    if (registerViewModel.isLoading) {
        ProgressBarTransparentBackground(stringResource(id = R.string.please_wait))
    }
}

private fun handleUserData(
    navHostController: NavHostController,
    result: NetworkResult<CommonResponse>,
    registerViewModel: RegisterViewModel,
    emailVerificationViewModel: EmailVerificationViewModel,
    email: String,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            registerViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            registerViewModel.isLoading = false
            navHostController.popBackStack()
            navHostController.navigate(Screen.Login.login(email = email))
            navHostController.popBackStack(Screen.Register.route, true)
            emailVerificationViewModel.textEmailVerify = ""
        }
        is NetworkResult.Error -> {
            // show error message
            registerViewModel.isLoading = false
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
            append(stringResource(id = R.string.accept))
        }
        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = stringResource(id = R.string.url),
            annotation = stringResource(id = R.string.url_string)
        )
        withStyle(
            style = SpanStyle(
                color = Custom_Blue,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            )
        ) {
            append(stringResource(id = R.string.terms_of_service))
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
        }
    )
}

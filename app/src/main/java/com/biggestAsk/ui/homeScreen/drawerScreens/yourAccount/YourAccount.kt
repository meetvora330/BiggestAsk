package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.example.biggestAsk.R

@Composable
fun YourAccountScreen(mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    var imageData by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        imageData = uri
    }
    imageData.let {
        val uri = it
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 65.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
        ) {
            val (img_camera, img_user) = createRefs()
            if (bitmap.value == null) {
                Image(
                    modifier = Modifier
                        .width(88.dp)
                        .height(88.dp)
                        .constrainAs(img_user) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                        },
                    painter = painterResource(id = R.drawable.img_nav_drawer),
                    contentDescription = ""
                )
            } else {
                bitmap.value?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(img_user) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .width(88.dp)
                                .height(88.dp), shape = RoundedCornerShape(10.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                    },
                                bitmap = it.asImageBitmap(),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
            Image(
                modifier = Modifier
                    .constrainAs(img_camera) {
                        start.linkTo(img_user.start)
                        end.linkTo(img_user.end)
                        top.linkTo(img_user.top)
                        bottom.linkTo(img_user.bottom)
                    }
                    .alpha(if (mainViewModel.isEditable.value) 1f else 0f)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        if (mainViewModel.isEditable.value) {
                            launcher.launch("image/*")
                        }
                    },
                painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                contentDescription = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                text = stringResource(id = R.string.register_tv_name),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountFullName.value,
                onValueChange = {
                    mainViewModel.yourAccountFullName.value = it
                    mainViewModel.yourAccountFullNameEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_name_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountFullNameEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter your name",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = "Phone number",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountPhoneNumber.value,
                onValueChange = {
                    mainViewModel.yourAccountPhoneNumber.value = it
                    mainViewModel.yourAccountPhoneNumberEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_phone_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountPhoneNumberEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text =stringResource(id = R.string.enter_phone_number),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = stringResource(id = R.string.register_tv_email_text),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountEmail.value,
                onValueChange = {
                    mainViewModel.yourAccountEmail.value = it
                    mainViewModel.yourAccountEmailEmpty = false
                    mainViewModel.yourAccountEmailIsValid = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_email_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountEmailEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter your email",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            if (mainViewModel.yourAccountEmailIsValid) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter valid email",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = "Home address",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountHomeAddress.value,
                onValueChange = {
                    mainViewModel.yourAccountHomeAddress.value = it
                    mainViewModel.yourAccountHomeAddressEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountHomeAddressEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter your address",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = "Your date of birth",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountDateOfBirth.value,
                onValueChange = {
                    mainViewModel.yourAccountDateOfBirth.value = it
                    mainViewModel.yourAccountDateOfBirthEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ), readOnly = !mainViewModel.isEditable.value,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_calendar_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountDateOfBirthEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter your date of birth",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = "Your partner’s name",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountPartnerName.value,
                onValueChange = {
                    mainViewModel.yourAccountPartnerName.value = it
                    mainViewModel.yourAccountPartnerNameEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_parent_name_your_account),
                        "error",
                    )
                }
            )
            if (mainViewModel.yourAccountPartnerNameEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter your partner name",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp),
                text = "Password",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                color = Text_Accept_Terms
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                value = mainViewModel.yourAccountPassword.value,
                onValueChange = {
                    mainViewModel.yourAccountPassword.value = it
                    mainViewModel.yourAccountPasswordEmpty = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = if (!mainViewModel.isEditable.value) ET_Bg
                    else Color(0xFFD0E1FA),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ), readOnly = !mainViewModel.isEditable.value,
                maxLines = 1,
                textStyle = MaterialTheme.typography.body2,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_et_password_your_account),
                        "error",
                    )
                }, keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
            if (mainViewModel.yourAccountPasswordEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    text = "Enter password",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            if (mainViewModel.isEditable.value) {
                Button(
                    onClick = {
                        when {
                            TextUtils.isEmpty(mainViewModel.yourAccountFullName.value) && TextUtils.isEmpty(
                                mainViewModel.yourAccountPhoneNumber.value
                            ) && TextUtils.isEmpty(
                                mainViewModel.yourAccountEmail.value
                            ) && TextUtils.isEmpty(
                                mainViewModel.yourAccountHomeAddress.value
                            ) && TextUtils.isEmpty(
                                mainViewModel.yourAccountDateOfBirth.value
                            ) && TextUtils.isEmpty(
                                mainViewModel.yourAccountPartnerName.value
                            ) && TextUtils.isEmpty(
                                mainViewModel.yourAccountPassword.value
                            ) -> {
                                mainViewModel.yourAccountFullNameEmpty = true
                                mainViewModel.yourAccountPhoneNumberEmpty = true
                                mainViewModel.yourAccountEmailEmpty = true
                                mainViewModel.yourAccountHomeAddressEmpty = true
                                mainViewModel.yourAccountDateOfBirthEmpty = true
                                mainViewModel.yourAccountPartnerNameEmpty = true
                                mainViewModel.yourAccountPasswordEmpty = true
                                Log.i("TAG", "All Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountFullName.value) -> {
                                mainViewModel.yourAccountFullNameEmpty = true
                                Log.i("TAG", "Full Name Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountPhoneNumber.value) -> {
                                mainViewModel.yourAccountPhoneNumberEmpty = true
                                Log.i("TAG", "Phone Number Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountEmail.value) -> {
                                mainViewModel.yourAccountEmailEmpty = true
                                Log.i("TAG", "Email Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountHomeAddress.value) -> {
                                mainViewModel.yourAccountHomeAddressEmpty = true
                                Log.i("TAG", "Home Address Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountDateOfBirth.value) -> {
                                mainViewModel.yourAccountDateOfBirthEmpty = true
                                Log.i("TAG", "DOB Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountPartnerName.value) -> {
                                mainViewModel.yourAccountPartnerNameEmpty = true
                                Log.i("TAG", "Partner Name Empty")
                            }
                            TextUtils.isEmpty(mainViewModel.yourAccountPassword.value) -> {
                                mainViewModel.yourAccountPasswordEmpty = true
                                Log.i("TAG", "Password Empty")
                            }
                            !Patterns.EMAIL_ADDRESS.matcher(mainViewModel.yourAccountEmail.value)
                                .matches() -> {
                                Log.i("TAG", "Invalid Email Empty")
                                mainViewModel.yourAccountEmailIsValid = true
                            }
                            else -> {
                                mainViewModel.isEditable.value = false
                            }
                        }

                    },
                    modifier = Modifier
                        .padding(top = 25.dp, start = 24.dp, end = 24.dp)
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
                        text = "Save Editing",
                        color = Color.White,
                        style = MaterialTheme.typography.body1,
                        lineHeight = 28.sp,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val logout =context.resources.getString(R.string.logout)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 32.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    lineHeight = 16.sp,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W400,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xFFAE4B2B),
                                fontSize = 14.sp,
                            )
                        ) {
                            append(logout)
                        }
                    })
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun YourAccountScreenPreview() {
    YourAccountScreen(MainViewModel())
}
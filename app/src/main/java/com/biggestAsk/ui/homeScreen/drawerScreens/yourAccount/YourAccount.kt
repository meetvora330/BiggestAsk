package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetAnsweredQuestionListResponse
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.simpleDropDown
import com.biggestAsk.ui.main.viewmodel.YourAccountViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.util.Constants.PARENT
import com.biggestAsk.util.Constants.SURROGATE
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun YourAccountScreen(
    navHostController: NavHostController,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
    context: Context
) {
    val type = PreferenceProvider(context).getValue("type", "")
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    val focusManager = LocalFocusManager.current
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val suggestions =
        listOf("Male", "Female", "Other")
    LaunchedEffect(Unit) {
        yourAccountViewModel.isEditable.value = false
        getAnsweredQuestionList(userId, type, yourAccountViewModel, homeActivity)
        when (type) {
            SURROGATE -> {
                yourAccountViewModel.isSurrogateApiCalled = true
                yourAccountViewModel.surrogateFullName = ""
                yourAccountViewModel.surrogatePhoneNumber = ""
                yourAccountViewModel.surrogateEmail = ""
                yourAccountViewModel.surrogateHomeAddress = ""
                yourAccountViewModel.surrogateDateOfBirth = ""
                yourAccountViewModel.surrogateHomeAddress = ""
                yourAccountViewModel.surrogatePartnerName = ""
                yourAccountViewModel.surrogateImg = ""
                getUserDetailsSurrogate(userId, type, homeActivity, yourAccountViewModel, context)
            }
            PARENT -> {
                yourAccountViewModel.isParentApiCalled = true
                yourAccountViewModel.parentFullName = ""
                yourAccountViewModel.parentPhoneNumber = ""
                yourAccountViewModel.parentEmail = ""
                yourAccountViewModel.parentHomeAddress = ""
                yourAccountViewModel.parentDateOfBirth = ""
                yourAccountViewModel.parentImg1 = ""
                yourAccountViewModel.parentImg2 = ""
                yourAccountViewModel.parentPartnerHomeAddress = ""
                yourAccountViewModel.parentPartnerDateOfBirth = ""
                yourAccountViewModel.parentPartnerPhoneNumber = ""
                yourAccountViewModel.parentPartnerName = ""
                getUserDetailsParent(userId, type, homeActivity, yourAccountViewModel, context)

            }
            else -> {
                Log.e("TAG", "YourAccountScreen: no surrogate no parent")
            }
        }
    }
    Log.d("TAG", "YourAccountScreen: surrogate gender ${yourAccountViewModel.surrogateGender}")
    Log.d("TAG", "YourAccountScreen: parent gender ${yourAccountViewModel.parentGender}")
    Log.d(
        "TAG",
        "YourAccountScreen: parent partner gender ${yourAccountViewModel.parentPartnerGender}"
    )
    val openLogoutDialog = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        yourAccountViewModel.imageData = uri
        yourAccountViewModel.getImage(context)
        yourAccountViewModel.isImagePresent.value = uri != null
    }
    when (type) {
        SURROGATE -> {
            if (yourAccountViewModel.isSurrogateDataLoading || yourAccountViewModel.isAnsweredQuestionLoading) {
                YourAccountSurrogateShimmerAnimation()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 24.dp, bottom = 50.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            32.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 28.dp)
                        ) {
                            val (img_camera, img_user) = createRefs()
                            if (yourAccountViewModel.bitmapImage1.value == null) {
                                val painter = rememberImagePainter(
                                    yourAccountViewModel.surrogateImg,
                                    builder = { placeholder(R.drawable.ic_placeholder_your_account) }
                                )
                                Image(
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(88.dp)
                                        .height(88.dp)
                                        .constrainAs(img_user) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            top.linkTo(parent.top)
                                        },
                                    painter = if (yourAccountViewModel.surrogateImg != "") painter else painterResource(
                                        id = R.drawable.ic_placeholder_your_account
                                    )
                                )
                            } else {
                                yourAccountViewModel.bitmapImage1.value?.let {
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
                            Icon(
                                modifier = Modifier
                                    .constrainAs(img_camera) {
                                        start.linkTo(img_user.start)
                                        end.linkTo(img_user.end)
                                        top.linkTo(img_user.top)
                                        bottom.linkTo(img_user.bottom)
                                    }
                                    .alpha(if (yourAccountViewModel.isEditable.value) 1f else 0f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        if (yourAccountViewModel.isEditable.value) {
                                            if (ActivityCompat.checkSelfPermission(
                                                    homeActivity,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                                ) != PackageManager.PERMISSION_GRANTED
                                            ) {
                                                homeActivity.callPermissionRequestLauncher(launcher)
                                                yourAccountViewModel.isPermissionAllowed =
                                                    false
                                            } else {
                                                launcher.launch("image/*")
                                                yourAccountViewModel.isPermissionAllowed =
                                                    false
                                            }
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                    if (yourAccountViewModel.isEditable.value) {
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
                                value = yourAccountViewModel.surrogateFullName,
                                onValueChange = {
                                    yourAccountViewModel.surrogateFullName = it
                                    yourAccountViewModel.yourAccountFullNameEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                    else Color(0xFFD0E1FA),
                                    cursorColor = Custom_Blue,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ), readOnly = !yourAccountViewModel.isEditable.value,
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_name_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountFullNameEmpty) {
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
                                text = stringResource(id = R.string.select_gender),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W400,
                                fontSize = 14.sp,
                                color = Text_Accept_Terms
                            )
                            yourAccountViewModel.surrogateGender = simpleDropDown(
                                suggestions = suggestions,
                                hint = stringResource(id = R.string.select_gender),
                                modifier = Modifier
                                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.W600,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                ),
                                color = Color(0xFFD0E1FA),
                                text = yourAccountViewModel.surrogateGender
                            )
                            if (yourAccountViewModel.surrogateGender == "" && yourAccountViewModel.isGenderSelected) {
                                Text(
                                    text = stringResource(id = R.string.select_gender),
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(start = 26.dp),
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
                                value = yourAccountViewModel.surrogatePhoneNumber,
                                onValueChange = {
                                    yourAccountViewModel.surrogatePhoneNumber = it
                                    yourAccountViewModel.yourAccountPhoneNumberEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                    else Color(0xFFD0E1FA),
                                    cursorColor = Custom_Blue,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ),
                                readOnly = !yourAccountViewModel.isEditable.value,
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_phone_your_account),
                                        "error",
                                    )
                                }
                            )
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
                                value = yourAccountViewModel.surrogateEmail,
                                onValueChange = {
                                    yourAccountViewModel.surrogateEmail = it
                                    yourAccountViewModel.yourAccountEmailEmpty = false
                                    yourAccountViewModel.yourAccountEmailIsValid = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                    else Color(0xFFD0E1FA),
                                    cursorColor = Custom_Blue,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ), readOnly = true,
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_email_your_account),
                                        "error",
                                    )
                                }
                            )
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
                                value = yourAccountViewModel.surrogateHomeAddress,
                                onValueChange = {
                                    yourAccountViewModel.surrogateHomeAddress = it
                                    yourAccountViewModel.yourAccountHomeAddressEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                    else Color(0xFFD0E1FA),
                                    cursorColor = Custom_Blue,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ), readOnly = !yourAccountViewModel.isEditable.value,
                                maxLines = 3,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                        "error",
                                    )
                                }
                            )
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
                                    .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        val datePickerDialog = DatePickerDialog(
                                            context,
                                            R.style.CalenderViewCustom,
                                            { _: DatePicker, year: Int, month: Int, day: Int ->
                                                yourAccountViewModel.surrogateDateOfBirth =
                                                    "$year/" + "%02d".format(month + 1) + "/" + "%02d".format(
                                                        day
                                                    )
                                            }, year, month, day
                                        )
                                        datePickerDialog.datePicker.maxDate = Date().time - 86400000
                                        datePickerDialog.show()
                                        datePickerDialog
                                            .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                            .setTextColor(
                                                ContextCompat.getColor(context, R.color.custom_blue)
                                            )
                                        datePickerDialog
                                            .getButton(DatePickerDialog.BUTTON_POSITIVE)
                                            .setTextColor(
                                                ContextCompat.getColor(context, R.color.custom_blue)
                                            )
                                        datePickerDialog
                                            .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                            .setOnClickListener {
                                                datePickerDialog.dismiss()
                                            }
                                        focusManager.clearFocus()
                                        yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                    },
                                value = yourAccountViewModel.surrogateDateOfBirth,
                                onValueChange = {
                                    yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                    else Color(0xFFD0E1FA),
                                    cursorColor = Custom_Blue,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ), readOnly = true, enabled = false, placeholder = {
                                    Text(
                                        text = "Date of birth",
                                        style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                    )
                                },
                                textStyle = MaterialTheme.typography.body2.copy(
                                    color = Color.Black
                                )
                            )
                            Button(
                                onClick = {
                                    when {
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateFullName) -> {
                                            yourAccountViewModel.yourAccountFullNameEmpty = true
                                            Log.i("TAG", "Full Name Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateGender) -> {
                                            yourAccountViewModel.isGenderSelected = true
                                        }
                                        else -> {
                                            yourAccountViewModel.isEditable.value = false
                                            val image = yourAccountViewModel.uriPathParent?.let {
                                                convertImageMultiPart(it)
                                            }
                                            yourAccountViewModel.updateUserProfile(
                                                userId = userId,
                                                name = MultipartBody.Part.createFormData(
                                                    "name",
                                                    yourAccountViewModel.surrogateFullName
                                                ),
                                                email = MultipartBody.Part.createFormData(
                                                    "email",
                                                    yourAccountViewModel.surrogateEmail
                                                ),
                                                number = MultipartBody.Part.createFormData(
                                                    "number",
                                                    yourAccountViewModel.surrogatePhoneNumber
                                                ),
                                                address = MultipartBody.Part.createFormData(
                                                    "address",
                                                    yourAccountViewModel.surrogateHomeAddress
                                                ),
                                                dateOfBirth = MultipartBody.Part.createFormData(
                                                    "date_of_birth",
                                                    yourAccountViewModel.surrogateDateOfBirth
                                                ),
                                                imgFileName1 = image,
                                                imgFileName2 = null,
                                                type = MultipartBody.Part.createFormData(
                                                    "type",
                                                    type
                                                ),
                                                gender = MultipartBody.Part.createFormData(
                                                    "gender",
                                                    yourAccountViewModel.surrogateGender
                                                )
                                            )
                                            yourAccountViewModel.updateUserProfileResponse.observe(
                                                homeActivity
                                            ) {
                                                if (it != null) {
                                                    handleUserUpdateData(
                                                        result = it,
                                                        yourAccountViewModel = yourAccountViewModel,
                                                        context = context,
                                                        type = type,
                                                        userId = userId,
                                                        homeActivity = homeActivity
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(
                                        top = 25.dp,
                                        start = 24.dp,
                                        end = 24.dp,
                                        bottom = 32.dp
                                    )
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
                            if (openLogoutDialog.value) {
                                Dialog(
                                    onDismissRequest = { openLogoutDialog.value = false },
                                    properties = DialogProperties(
                                        dismissOnBackPress = true,
                                        dismissOnClickOutside = false,
                                        usePlatformDefaultWidth = true,
                                    )
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        LogoutDialog(
                                            openLogoutDialog = openLogoutDialog,
                                            context = context,
                                            homeActivity = homeActivity
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (yourAccountViewModel.surrogateFullName != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth(),
                                        text =
                                        yourAccountViewModel.surrogateFullName,
                                        style = MaterialTheme.typography.h2.copy(
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.W600,
                                            lineHeight = 32.sp
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (yourAccountViewModel.surrogateDateOfBirth != "") {
                                        val dateOfBirth =
                                            yourAccountViewModel.surrogateDateOfBirth.replace(
                                                "/",
                                                ""
                                            )
                                        Log.d("TAG", "YourAccountScreen: $dateOfBirth")
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .padding(end = 2.dp),
                                            text = yourAccountViewModel.surrogateDateOfBirth,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFF7F7D7C),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth(),
                                            text = "(37 Year)",
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                    }
                                }
                                Image(
                                    modifier = Modifier.padding(top = 10.dp),
                                    painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                                    contentDescription = ""
                                )
                                if (yourAccountViewModel.surrogateHomeAddress != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 18.dp, start = 12.dp, end = 12.dp),
                                        text = yourAccountViewModel.surrogateHomeAddress,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                                if (yourAccountViewModel.surrogatePhoneNumber != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 11.dp),
                                        text =
                                        yourAccountViewModel.surrogatePhoneNumber,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Custom_Blue,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                if (yourAccountViewModel.surrogateEmail != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 16.dp),
                                        text = yourAccountViewModel.surrogateEmail,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                        yourAccountViewModel.questionAnsweredList.forEachIndexed { index, item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                elevation = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp, end = 23.dp, top = 34.dp)
                            ) {
                                Column {
                                    item.question?.let {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                            text = it,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 24.sp
                                            ),
                                        )
                                    }
                                    Row {
                                        item.user_name?.let {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 24.dp,
                                                    top = 10.dp,
                                                ),
                                                text = it,
                                                style = MaterialTheme.typography.body2.copy(
                                                    color = Custom_Blue,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W600,
                                                    lineHeight = 22.sp
                                                )
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp, end = 24.dp),
                                            text = if (yourAccountViewModel.questionAnsweredDaysList[index]==0) "Today" else "${yourAccountViewModel.questionAnsweredDaysList[index]} Day ago",
                                            color = Color(0xFF9F9D9B),
                                            style = MaterialTheme.typography.body1,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    item.answer?.let {
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 24.dp,
                                                top = 4.dp,
                                                bottom = 22.dp
                                            ),
                                            text = it,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 22.sp
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (yourAccountViewModel.isPermissionAllowed) {
                AlertDialog(
                    onDismissRequest = {
                        yourAccountViewModel.isPermissionAllowed = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri = Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            context.startActivity(intent)
                        })
                        { Text(text = "APP SETTINGS", color = Color.Red) }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            yourAccountViewModel.isPermissionAllowed = false
                        })
                        { Text(text = "CANCEL", color = Color.Red) }
                    },
                    title = { Text(text = "Permission Denied") },
                    text = { Text(text = "Permission is denied, Please allow permission from App Settings") }
                )
            }
        }
        PARENT -> {
            if (yourAccountViewModel.isParentDataLoading || yourAccountViewModel.isAnsweredQuestionLoading) {
                YourAccountParentShimmerAnimation()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 34.dp, bottom = 50.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            32.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Box {
                            if (yourAccountViewModel.bitmapImage1.value == null) {
                                val painter1 = rememberImagePainter(yourAccountViewModel.parentImg1,
                                    builder = { placeholder(R.drawable.ic_placeholder_your_account) })
                                Image(
                                    modifier = Modifier
                                        .width(88.dp)
                                        .height(88.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            yourAccountViewModel.isParentClicked = true
                                            yourAccountViewModel.isMotherClicked = false
                                        },
                                    painter = if (yourAccountViewModel.parentImg1 != "") painter1 else painterResource(
                                        id = R.drawable.ic_placeholder_your_account
                                    ),
                                    contentDescription = "",
                                )
                            } else {
                                yourAccountViewModel.bitmapImage1.value?.let {
                                    Image(
                                        modifier = Modifier
                                            .width(88.dp)
                                            .height(88.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = MutableInteractionSource()
                                            ) {
                                                yourAccountViewModel.isParentClicked = true
                                                yourAccountViewModel.isMotherClicked = false
                                            },
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "",
                                    )
                                }
                            }
                            if (yourAccountViewModel.isEditable.value && yourAccountViewModel.isParentClicked) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            if (yourAccountViewModel.isEditable.value && yourAccountViewModel.isParentClicked) {
                                                if (ActivityCompat.checkSelfPermission(
                                                        homeActivity,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    homeActivity.callPermissionRequestLauncher(
                                                        launcher
                                                    )
                                                    yourAccountViewModel.isPermissionAllowed =
                                                        false
                                                } else {
                                                    launcher.launch("image/*")
                                                    yourAccountViewModel.isPermissionAllowed =
                                                        false
                                                }
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                        Box {
                            if (yourAccountViewModel.bitmapImage2.value == null) {
                                val painter2 = rememberImagePainter(
                                    yourAccountViewModel.parentImg2,
                                    builder = { placeholder(R.drawable.ic_placeholder_your_account) }
                                )
                                Image(
                                    modifier = Modifier
                                        .width(88.dp)
                                        .height(88.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            yourAccountViewModel.isParentClicked = false
                                            yourAccountViewModel.isMotherClicked = true
                                        },
                                    painter = if (yourAccountViewModel.parentImg2 != "") painter2 else painterResource(
                                        id = R.drawable.ic_placeholder_your_account
                                    ),
                                    contentDescription = "",
                                )
                            } else {
                                yourAccountViewModel.bitmapImage2.value?.let {
                                    Image(
                                        modifier = Modifier
                                            .width(88.dp)
                                            .height(88.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = MutableInteractionSource()
                                            ) {
                                                yourAccountViewModel.isParentClicked = false
                                                yourAccountViewModel.isMotherClicked = true
                                            },
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "",
                                    )
                                }
                            }
                            if (yourAccountViewModel.isEditable.value && yourAccountViewModel.isMotherClicked) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            if (yourAccountViewModel.isEditable.value && yourAccountViewModel.isMotherClicked) {
                                                if (ActivityCompat.checkSelfPermission(
                                                        homeActivity,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    homeActivity.callPermissionRequestLauncher(
                                                        launcher
                                                    )
                                                    yourAccountViewModel.isPermissionAllowed =
                                                        false
                                                } else {
                                                    launcher.launch("image/*")
                                                    yourAccountViewModel.isPermissionAllowed =
                                                        false
                                                }
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            92.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Image(
                            modifier = Modifier,
                            painter = painterResource(
                                id = R.drawable.ic_baseline_arrow_drop_up_24
                            ),
                            contentDescription = "",
                            alpha = if (!yourAccountViewModel.isParentClicked) 0f else 1f
                        )
                        Image(
                            modifier = Modifier,
                            painter = painterResource(
                                id = R.drawable.ic_baseline_arrow_drop_up_24
                            ),
                            contentDescription = "",
                            alpha = if (yourAccountViewModel.isMotherClicked) 1f else 0f
                        )
                    }
                    if (!yourAccountViewModel.isEditable.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (yourAccountViewModel.parentFullName != "" || yourAccountViewModel.parentPartnerFullName != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth(),
                                        text = if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentFullName else yourAccountViewModel.parentPartnerName,
                                        style = MaterialTheme.typography.h2.copy(
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.W600,
                                            lineHeight = 32.sp
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (yourAccountViewModel.parentDateOfBirth != "") {
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .padding(end = 2.dp),
                                            text = if (yourAccountViewModel.isParentClicked) yourAccountViewModel.parentDateOfBirth else yourAccountViewModel.parentPartnerDateOfBirth,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFF7F7D7C),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth(),
                                            text = if (yourAccountViewModel.isParentClicked) "(37 Year)" else "(30 Year)",
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                    }
                                }
                                Image(
                                    modifier = Modifier.padding(top = 10.dp),
                                    painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                                    contentDescription = ""
                                )
                                if (yourAccountViewModel.parentHomeAddress != "" && yourAccountViewModel.isParentClicked) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 18.dp),
                                        text = yourAccountViewModel.parentHomeAddress,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                } else if (yourAccountViewModel.parentPartnerHomeAddress != "" && yourAccountViewModel.isMotherClicked) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 18.dp),
                                        text = yourAccountViewModel.parentPartnerHomeAddress,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                if (yourAccountViewModel.parentPhoneNumber != "" && yourAccountViewModel.isParentClicked) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 11.dp),
                                        text = yourAccountViewModel.parentPhoneNumber,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Custom_Blue,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                } else if (yourAccountViewModel.parentPhoneNumber != "" && yourAccountViewModel.isMotherClicked) {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 11.dp),
                                        text = yourAccountViewModel.parentPartnerPhoneNumber,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Custom_Blue,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                if (yourAccountViewModel.parentEmail != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 16.dp),
                                        text = yourAccountViewModel.parentEmail,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    if (yourAccountViewModel.isEditable.value) {
                        if (yourAccountViewModel.isParentClicked) {
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
                                    value = yourAccountViewModel.parentFullName,
                                    onValueChange = {
                                        yourAccountViewModel.parentFullName = it
                                        yourAccountViewModel.yourAccountFullNameEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = false,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_name_your_account),
                                            "error",
                                        )
                                    }
                                )
                                if (yourAccountViewModel.yourAccountFullNameEmpty) {
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
                                    text = stringResource(id = R.string.select_gender),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 14.sp,
                                    color = Text_Accept_Terms
                                )
                                yourAccountViewModel.parentGender = simpleDropDown(
                                    suggestions = suggestions,
                                    hint = stringResource(id = R.string.select_gender),
                                    modifier = Modifier
                                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.W600,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    color = Color(0xFFD0E1FA),
                                    text = yourAccountViewModel.parentGender
                                )
                                if (yourAccountViewModel.parentGender == "" && yourAccountViewModel.isGenderSelected) {
                                    Text(
                                        text = stringResource(id = R.string.select_gender),
                                        color = MaterialTheme.colors.error,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier
                                            .padding(start = 26.dp),
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
                                    value = yourAccountViewModel.parentPhoneNumber,
                                    onValueChange = {
                                        yourAccountViewModel.parentPhoneNumber = it
                                        yourAccountViewModel.yourAccountPhoneNumberEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ),
                                    readOnly = !yourAccountViewModel.isEditable.value,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_phone_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                    value = yourAccountViewModel.parentEmail,
                                    onValueChange = {
                                        yourAccountViewModel.parentEmail = it
                                        yourAccountViewModel.yourAccountEmailEmpty = false
                                        yourAccountViewModel.yourAccountEmailIsValid = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = true,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_email_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                    value = yourAccountViewModel.parentHomeAddress,
                                    onValueChange = {
                                        yourAccountViewModel.parentHomeAddress = it
                                        yourAccountViewModel.yourAccountHomeAddressEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = !yourAccountViewModel.isEditable.value,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                        .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            val datePickerDialog = DatePickerDialog(
                                                context,
                                                R.style.CalenderViewCustom,
                                                { _: DatePicker, year: Int, month: Int, day: Int ->
                                                    yourAccountViewModel.parentDateOfBirth =
                                                        "$year/" + "%02d".format(month + 1) + "/" + "%02d".format(
                                                            day
                                                        )
                                                }, year, month, day
                                            )
                                            datePickerDialog.datePicker.maxDate =
                                                Date().time - 86400000
                                            datePickerDialog.show()
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                                .setTextColor(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.custom_blue
                                                    )
                                                )
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_POSITIVE)
                                                .setTextColor(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.custom_blue
                                                    )
                                                )
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                                .setOnClickListener {
                                                    datePickerDialog.dismiss()
                                                }
                                            focusManager.clearFocus()
                                            yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                        },
                                    value = yourAccountViewModel.parentDateOfBirth,
                                    onValueChange = {
                                        yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ), readOnly = true, enabled = false, placeholder = {
                                        Text(
                                            text = "Date of birth",
                                            style = MaterialTheme.typography.body2.copy(
                                                Color(
                                                    0xFF7F7D7C
                                                )
                                            )
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.body2.copy(
                                        color = Color.Black
                                    )
                                )
                                if (yourAccountViewModel.isEditable.value) {
                                    Button(
                                        onClick = {
                                            when {
                                                TextUtils.isEmpty(yourAccountViewModel.parentFullName) -> {
                                                    yourAccountViewModel.yourAccountFullNameEmpty =
                                                        true
                                                    Log.i("TAG", "Full Name Empty")
                                                }
                                                TextUtils.isEmpty(yourAccountViewModel.parentGender) -> {
                                                    yourAccountViewModel.isGenderSelected = true
                                                }
                                                else -> {
                                                    val image =
                                                        yourAccountViewModel.uriPathParent?.let {
                                                            convertImageMultiPart(it)
                                                        }
                                                    yourAccountViewModel.updateUserProfile(
                                                        userId = userId,
                                                        name = MultipartBody.Part.createFormData(
                                                            "name",
                                                            yourAccountViewModel.parentFullName
                                                        ),
                                                        email = MultipartBody.Part.createFormData(
                                                            "email",
                                                            yourAccountViewModel.parentEmail
                                                        ),
                                                        number = MultipartBody.Part.createFormData(
                                                            "number",
                                                            yourAccountViewModel.parentPhoneNumber
                                                        ),
                                                        address = MultipartBody.Part.createFormData(
                                                            "address",
                                                            yourAccountViewModel.parentHomeAddress
                                                        ),
                                                        dateOfBirth = MultipartBody.Part.createFormData(
                                                            "date_of_birth",
                                                            yourAccountViewModel.parentDateOfBirth
                                                        ),
                                                        imgFileName1 = image,
                                                        imgFileName2 = null,
                                                        type = MultipartBody.Part.createFormData(
                                                            "type",
                                                            type
                                                        ),
                                                        gender = MultipartBody.Part.createFormData(
                                                            "gender",
                                                            yourAccountViewModel.parentGender
                                                        ),
                                                    )
                                                    yourAccountViewModel.updateUserProfileResponse.observe(
                                                        homeActivity
                                                    ) {
                                                        if (it != null) {
                                                            handleUserUpdateData(
                                                                result = it,
                                                                yourAccountViewModel = yourAccountViewModel,
                                                                context = context,
                                                                type = type,
                                                                userId = userId,
                                                                homeActivity = homeActivity
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(
                                                top = 25.dp,
                                                start = 24.dp,
                                                end = 24.dp,
                                                bottom = 32.dp
                                            )
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
                            }
                        } else if (yourAccountViewModel.isMotherClicked) {
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
                                    value = yourAccountViewModel.parentPartnerName,
                                    onValueChange = {
                                        yourAccountViewModel.parentPartnerName = it
                                        yourAccountViewModel.yourAccountFullNameEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = false,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_name_your_account),
                                            "error",
                                        )
                                    }
                                )
                                if (yourAccountViewModel.yourAccountFullNameEmpty) {
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
                                    text = stringResource(id = R.string.select_gender),
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 14.sp,
                                    color = Text_Accept_Terms
                                )
                                yourAccountViewModel.parentPartnerGender = simpleDropDown(
                                    suggestions = suggestions,
                                    hint = stringResource(id = R.string.select_gender),
                                    modifier = Modifier
                                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.W600,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    color = Color(0xFFD0E1FA),
                                    text = yourAccountViewModel.parentPartnerGender
                                )
                                if (yourAccountViewModel.parentPartnerGender == "" && yourAccountViewModel.isGenderSelected) {
                                    Text(
                                        text = stringResource(id = R.string.select_gender),
                                        color = MaterialTheme.colors.error,
                                        style = MaterialTheme.typography.caption,
                                        modifier = Modifier
                                            .padding(start = 26.dp),
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
                                    value = yourAccountViewModel.parentPartnerPhoneNumber,
                                    onValueChange = {
                                        yourAccountViewModel.parentPartnerPhoneNumber = it
                                        yourAccountViewModel.yourAccountPhoneNumberEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ),
                                    readOnly = !yourAccountViewModel.isEditable.value,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_phone_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                    value = yourAccountViewModel.parentEmail,
                                    onValueChange = {
                                        yourAccountViewModel.parentEmail = it
                                        yourAccountViewModel.yourAccountEmailEmpty = false
                                        yourAccountViewModel.yourAccountEmailIsValid = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = true,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_email_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                    value = yourAccountViewModel.parentPartnerHomeAddress,
                                    onValueChange = {
                                        yourAccountViewModel.parentPartnerHomeAddress = it
                                        yourAccountViewModel.yourAccountHomeAddressEmpty = false
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        textColor = Color.Black
                                    ), readOnly = !yourAccountViewModel.isEditable.value,
                                    maxLines = 1,
                                    textStyle = MaterialTheme.typography.body2,
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                            "error",
                                        )
                                    }
                                )
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
                                        .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            val datePickerDialog = DatePickerDialog(
                                                context,
                                                R.style.CalenderViewCustom,
                                                { _: DatePicker, year: Int, month: Int, day: Int ->
                                                    yourAccountViewModel.parentPartnerDateOfBirth =
                                                        "$year/" + "%02d".format(month + 1) + "/" + "%02d".format(
                                                            day
                                                        )
                                                }, year, month, day
                                            )
                                            datePickerDialog.datePicker.setMaxDate(Date().time - 86400000)
                                            datePickerDialog.show()
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                                .setTextColor(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.custom_blue
                                                    )
                                                )
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_POSITIVE)
                                                .setTextColor(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.custom_blue
                                                    )
                                                )
                                            datePickerDialog
                                                .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                                .setOnClickListener {
                                                    datePickerDialog.dismiss()
                                                }
                                            focusManager.clearFocus()
                                            yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                        },
                                    value = yourAccountViewModel.parentPartnerDateOfBirth,
                                    onValueChange = {
                                        yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = if (!yourAccountViewModel.isEditable.value) ET_Bg
                                        else Color(0xFFD0E1FA),
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ), readOnly = true, enabled = false, placeholder = {
                                        Text(
                                            text = "Date of birth",
                                            style = MaterialTheme.typography.body2.copy(
                                                Color(
                                                    0xFF7F7D7C
                                                )
                                            )
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.body2.copy(
                                        color = Color.Black
                                    )
                                )
                                if (yourAccountViewModel.isEditable.value) {
                                    Button(
                                        onClick = {
                                            when {
                                                TextUtils.isEmpty(yourAccountViewModel.parentPartnerName) -> {
                                                    yourAccountViewModel.yourAccountFullNameEmpty =
                                                        true
                                                    Log.i("TAG", "Full Name Empty")
                                                }
                                                TextUtils.isEmpty(yourAccountViewModel.parentPartnerGender) -> {
                                                    yourAccountViewModel.isGenderSelected =
                                                        true
                                                    Log.i("TAG", "Full Name Empty")
                                                }
                                                else -> {
                                                    val image =
                                                        yourAccountViewModel.uriPathMother?.let {
                                                            convertImageMultiPart(it)
                                                        }
                                                    yourAccountViewModel.updateUserProfile(
                                                        userId = userId,
                                                        name = MultipartBody.Part.createFormData(
                                                            "name",
                                                            yourAccountViewModel.parentPartnerName
                                                        ),
                                                        email = MultipartBody.Part.createFormData(
                                                            "email",
                                                            yourAccountViewModel.parentEmail
                                                        ),
                                                        number = MultipartBody.Part.createFormData(
                                                            "number",
                                                            yourAccountViewModel.parentPartnerPhoneNumber
                                                        ),
                                                        address = MultipartBody.Part.createFormData(
                                                            "address",
                                                            yourAccountViewModel.parentPartnerHomeAddress
                                                        ),
                                                        dateOfBirth = MultipartBody.Part.createFormData(
                                                            "date_of_birth",
                                                            yourAccountViewModel.parentPartnerDateOfBirth
                                                        ),
                                                        imgFileName1 = null,
                                                        imgFileName2 = image,
                                                        type = MultipartBody.Part.createFormData(
                                                            "type",
                                                            type
                                                        ),
                                                        partner_phone = MultipartBody.Part.createFormData(
                                                            "partner_phone",
                                                            yourAccountViewModel.parentPartnerPhoneNumber
                                                        ),
                                                        partner_dob = MultipartBody.Part.createFormData(
                                                            "partner_dob",
                                                            yourAccountViewModel.parentPartnerDateOfBirth
                                                        ),
                                                        partner_address = MultipartBody.Part.createFormData(
                                                            "parent_address",
                                                            yourAccountViewModel.parentPartnerHomeAddress
                                                        ),
                                                        partner_gender = MultipartBody.Part.createFormData(
                                                            "partner_gender",
                                                            yourAccountViewModel.parentPartnerGender
                                                        ),
                                                    )
                                                    yourAccountViewModel.updateUserProfileResponse.observe(
                                                        homeActivity
                                                    ) {
                                                        if (it != null) {
                                                            handleUserUpdateData(
                                                                result = it,
                                                                yourAccountViewModel = yourAccountViewModel,
                                                                context = context,
                                                                type = type,
                                                                userId = userId,
                                                                homeActivity = homeActivity
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(
                                                top = 25.dp,
                                                start = 24.dp,
                                                end = 24.dp,
                                                bottom = 32.dp
                                            )
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
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (yourAccountViewModel.surrogateFullName != "") {
                                    Text(
                                        modifier = Modifier.wrapContentWidth(),
                                        text =
                                        yourAccountViewModel.surrogateFullName,
                                        style = MaterialTheme.typography.h2.copy(
                                            color = Color.Black,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.W600,
                                            lineHeight = 32.sp
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (yourAccountViewModel.surrogateDateOfBirth != "") {
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .padding(end = 2.dp),
                                            text =
                                            yourAccountViewModel.surrogateDateOfBirth,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFF7F7D7C),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                        Text(
                                            modifier = Modifier.wrapContentWidth(),
                                            text = "(37 Year)",
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 22.sp
                                            )
                                        )
                                    }
                                }
                                if (yourAccountViewModel.surrogateHomeAddress != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 18.dp),
                                        text =
                                        yourAccountViewModel.surrogateHomeAddress,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                if (yourAccountViewModel.surrogatePhoneNumber != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 11.dp),
                                        text =
                                        yourAccountViewModel.surrogatePhoneNumber,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Custom_Blue,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                if (yourAccountViewModel.surrogateEmail != "") {
                                    Text(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(top = 16.dp),
                                        text = yourAccountViewModel.surrogateEmail,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                        yourAccountViewModel.questionAnsweredList.forEachIndexed { index, item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                elevation = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp, end = 23.dp, top = 34.dp)
                            ) {
                                Column {
                                    item.question?.let {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                            text = it,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 24.sp
                                            ),
                                        )
                                    }
                                    Row {
                                        item.user_name?.let {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 24.dp,
                                                    top = 10.dp,
                                                ),
                                                text = it,
                                                style = MaterialTheme.typography.body2.copy(
                                                    color = Custom_Blue,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W600,
                                                    lineHeight = 22.sp
                                                )
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp, end = 24.dp),
                                            text = if (yourAccountViewModel.questionAnsweredDaysList[index]==0) "Today" else "${yourAccountViewModel.questionAnsweredDaysList[index]} Day ago",
                                            color = Color(0xFF9F9D9B),
                                            style = MaterialTheme.typography.body1,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    item.answer?.let {
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 24.dp,
                                                top = 4.dp,
                                                bottom = 22.dp
                                            ),
                                            text = it,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 22.sp
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (yourAccountViewModel.isPermissionAllowed) {
                AlertDialog(
                    onDismissRequest = {
                        yourAccountViewModel.isPermissionAllowed = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri = Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            context.startActivity(intent)
                        })
                        { Text(text = "APP SETTINGS", color = Color.Red) }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            yourAccountViewModel.isPermissionAllowed = false
                        })
                        { Text(text = "CANCEL", color = Color.Red) }
                    },
                    title = { Text(text = "Permission Denied") },
                    text = { Text(text = "Permission is denied, Please allow permission from App Settings") }
                )
            }
        }
    }
}

fun getAnsweredQuestionList(
    userId: Int,
    type: String?,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
) {
    if (type != null) {
        yourAccountViewModel.getYourAccountAnsweredQuestionList(userId = userId, type = type)
    }
    yourAccountViewModel.getAnsweredQuestionListResponse.observe(homeActivity) {
        if (it != null) {
            if (type != null) {
                handleQuestionAnsweredList(
                    result = it,
                    yourAccountViewModel = yourAccountViewModel,
                    type = type,
                    userId = userId,
                    homeActivity = homeActivity
                )
            }
        }
    }
}

private fun handleQuestionAnsweredList(
    result: NetworkResult<GetAnsweredQuestionListResponse>,
    yourAccountViewModel: YourAccountViewModel,
    type: String,
    userId: Int,
    homeActivity: HomeActivity
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            yourAccountViewModel.isAnsweredQuestionLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            yourAccountViewModel.isAnsweredQuestionLoading = false
            yourAccountViewModel.questionAnsweredList.clear()
            yourAccountViewModel.questionAnsweredDaysList.clear()
            if (result.data?.data?.isNotEmpty() == true) {
                result.data.data.let { yourAccountViewModel.questionAnsweredList.addAll(it) }
            }
            if (result.data?.days?.isNotEmpty() == true) {
                result.data.days.let { yourAccountViewModel.questionAnsweredDaysList.addAll(it) }
            }

        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            yourAccountViewModel.isAnsweredQuestionLoading = false
        }
    }
}

fun getUserDetailsParent(
    userId: Int,
    type: String,
    homeActivity: HomeActivity,
    yourAccountViewModel: YourAccountViewModel,
    context: Context
) {
    yourAccountViewModel.getUserDetailsParent(GetUserDetailsParentRequest(userId, type))
    yourAccountViewModel.getUserDetailResponseParent.observe(homeActivity) {
        if (it != null) {
            handleUserDataParent(
                result = it,
                yourAccountViewModel = yourAccountViewModel,
                context = context
            )
        }
    }
}

fun getUserDetailsSurrogate(
    userId: Int,
    type: String,
    homeActivity: HomeActivity,
    yourAccountViewModel: YourAccountViewModel,
    context: Context
) {
    yourAccountViewModel.getUserDetailsSurrogate(
        GetUserDetailsSurrogateRequest(
            userId,
            type
        )
    )
    yourAccountViewModel.getUserDetailResponseSurrogate.observe(homeActivity) {
        if (it != null) {
            handleUserDataSurrogate(
                result = it,
                yourAccountViewModel = yourAccountViewModel,
                context = context
            )
        }
    }
}


private fun handleUserDataSurrogate(
    result: NetworkResult<GetUserDetailsSurrogateResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isSurrogateDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view

            yourAccountViewModel.isSurrogateDataLoading = false
            if ((result.data?.name != null)) {
                yourAccountViewModel.surrogateFullName = result.data.name
            }
            if ((result.data?.number != null)) {
                yourAccountViewModel.surrogatePhoneNumber = result.data.number
            }
            if (result.data?.email != null) {
                yourAccountViewModel.surrogateEmail = result.data.email
            }
            if (result.data?.address != null) {
                yourAccountViewModel.surrogateHomeAddress = result.data.address
            }
            if (result.data?.date_of_birth != null) {
                yourAccountViewModel.surrogateDateOfBirth = result.data.date_of_birth
            }
            if (result.data?.partner_name != null) {
                yourAccountViewModel.surrogatePartnerName = result.data.partner_name
            }
            if (result.data?.partner_name != null) {
                yourAccountViewModel.surrogatePartnerName = result.data.partner_name
            }
            if (result.data?.image1 != null) {
                yourAccountViewModel.surrogateImg = result.data.image1
            }
            if (result.data?.gender != null) {
                yourAccountViewModel.surrogateGender = result.data.gender
            }
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isSurrogateDataLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserDataParent(
    result: NetworkResult<GetUserDetailsParentResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isParentDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isParentDataLoading = false

            if (result.data?.parent_name != null) {
                yourAccountViewModel.parentFullName = result.data.parent_name
            }
            if (result.data?.parent_number != null) {
                yourAccountViewModel.parentPhoneNumber = result.data.parent_number.toString()
            }
            if (result.data?.parent_email != null) {
                yourAccountViewModel.parentEmail = result.data.parent_email
            }
            if (result.data?.parent_address != null) {
                yourAccountViewModel.parentHomeAddress = result.data.parent_address
            }
            if (result.data?.parent_date_of_birth != null) {
                yourAccountViewModel.parentDateOfBirth = result.data.parent_date_of_birth
            }
            if (result.data?.parent_image1 != null) {
                yourAccountViewModel.parentImg1 = result.data.parent_image1
            }
            if (result.data?.parent_image2 != null) {
                yourAccountViewModel.parentImg2 = result.data.parent_image2
            }
            if (result.data?.parent_partner_address != null) {
                yourAccountViewModel.parentPartnerHomeAddress = result.data.parent_partner_address
            }
            if (result.data?.parent_partner_dob != null) {
                yourAccountViewModel.parentPartnerDateOfBirth = result.data.parent_partner_dob
            } else {
                yourAccountViewModel.parentPartnerDateOfBirth = ""
            }
            if (result.data?.parent_gender != null) {
                yourAccountViewModel.parentGender = result.data.parent_gender
            } else {
                yourAccountViewModel.parentGender = ""
            }
            if (result.data?.parent_partner_gender != null) {
                yourAccountViewModel.parentPartnerGender = result.data.parent_partner_gender
            } else {
                yourAccountViewModel.parentPartnerGender = ""
            }
            if (result.data?.parent_partner_phone != null) {
                yourAccountViewModel.parentPartnerPhoneNumber =
                    result.data.parent_partner_phone.toString()
            }
            if (result.data?.parent_partner_name != null) {
                yourAccountViewModel.parentPartnerName = result.data.parent_partner_name
            }
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isParentDataLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserUpdateData(
    result: NetworkResult<UpdateUserProfileResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
    type: String,
    userId: Int,
    homeActivity: HomeActivity
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            yourAccountViewModel.isSurrogateDataLoading = true
            yourAccountViewModel.isParentDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            yourAccountViewModel.isEditable.value = false
            yourAccountViewModel.isSurrogateDataLoading = false
            yourAccountViewModel.isParentDataLoading = false
            when (type) {
                SURROGATE -> {
                    getUserDetailsSurrogate(
                        userId = userId,
                        type = type,
                        homeActivity = homeActivity,
                        yourAccountViewModel = yourAccountViewModel,
                        context = context
                    )
                }
                PARENT -> {
                    getUserDetailsParent(
                        userId = userId,
                        homeActivity = homeActivity,
                        type = type,
                        yourAccountViewModel = yourAccountViewModel,
                        context = context
                    )
                }
            }
            getAnsweredQuestionList(
                userId = userId,
                yourAccountViewModel = yourAccountViewModel,
                type = type,
                homeActivity = homeActivity
            )
            Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            yourAccountViewModel.isEditable.value = true
            yourAccountViewModel.isSurrogateDataLoading = false
            yourAccountViewModel.isParentDataLoading = false
        }
    }
}

fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}

private fun convertImageMultiPart(imagePath: String): MultipartBody.Part {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        "image1",
        file.name,
        file.asRequestBody("image/*".toMediaTypeOrNull())
    )
}


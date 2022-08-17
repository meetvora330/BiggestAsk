package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.YourAccountViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.util.Constants.PARENT
import com.biggestAsk.util.Constants.SURROGATE
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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
    LaunchedEffect(Unit) {
        yourAccountViewModel.isEditable.value = false
        when (type) {
            SURROGATE -> {
                yourAccountViewModel.isSurrogateApiCalled = true
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
            PARENT -> {
                yourAccountViewModel.isParentApiCalled = true
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
            else -> {
                Log.e("TAG", "YourAccountScreen: no surrogate no parent")
            }
        }
    }
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
                        if (yourAccountViewModel.bitmap.value == null) {
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
                                    }
                                    .placeholder(
                                        visible = yourAccountViewModel.isLoading,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(4.dp),
                                        highlight = PlaceholderHighlight.shimmer(
                                            highlightColor = Color.White,
                                        )
                                    ),
                                painter = if (yourAccountViewModel.surrogateImg != "") painter else painterResource(
                                    id = R.drawable.ic_placeholder_your_account
                                )
                            )
                        } else {
                            yourAccountViewModel.bitmap.value?.let {
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
                                .alpha(if (yourAccountViewModel.isEditable.value) 1f else 0f)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
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
                                },
                            painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                            contentDescription = ""
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
                        if (yourAccountViewModel.yourAccountPhoneNumberEmpty) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = stringResource(id = R.string.enter_phone_number),
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
                        if (yourAccountViewModel.yourAccountEmailEmpty) {
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
                        if (yourAccountViewModel.yourAccountEmailIsValid) {
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
                            value = yourAccountViewModel.surrogateHomeAddress,
                            onValueChange = {
                                yourAccountViewModel.surrogateHomeAddress = it
                                yourAccountViewModel.yourAccountHomeAddressEmpty = false
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
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
                                    painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                    "error",
                                )
                            }
                        )
                        if (yourAccountViewModel.yourAccountHomeAddressEmpty) {
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
                            value = yourAccountViewModel.surrogateDateOfBirth,
                            onValueChange = {
                                yourAccountViewModel.surrogateDateOfBirth = it
                                yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ), readOnly = !yourAccountViewModel.isEditable.value,
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
                            maxLines = 1,
                            textStyle = MaterialTheme.typography.body2,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_icon_et_calendar_your_account),
                                    "error",
                                )
                            }
                        )
                        if (yourAccountViewModel.yourAccountDateOfBirthEmpty) {
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
                        if (yourAccountViewModel.isEditable.value) {
                            Button(
                                onClick = {
                                    when {
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateFullName) && TextUtils.isEmpty(
                                            yourAccountViewModel.surrogatePhoneNumber
                                        ) && TextUtils.isEmpty(
                                            yourAccountViewModel.surrogateEmail
                                        ) && TextUtils.isEmpty(
                                            yourAccountViewModel.surrogateHomeAddress
                                        ) && TextUtils.isEmpty(
                                            yourAccountViewModel.surrogateDateOfBirth
                                        ) && TextUtils.isEmpty(
                                            yourAccountViewModel.surrogatePartnerName
                                        ) && TextUtils.isEmpty(
                                            yourAccountViewModel.yourAccountPassword
                                        ) -> {
                                            yourAccountViewModel.yourAccountFullNameEmpty = true
                                            yourAccountViewModel.yourAccountPhoneNumberEmpty = true
                                            yourAccountViewModel.yourAccountEmailEmpty = true
                                            yourAccountViewModel.yourAccountHomeAddressEmpty = true
                                            yourAccountViewModel.yourAccountDateOfBirthEmpty = true
                                            yourAccountViewModel.yourAccountPartnerNameEmpty = true
                                            yourAccountViewModel.yourAccountPasswordEmpty = true
                                            Log.i("TAG", "All Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateFullName) -> {
                                            yourAccountViewModel.yourAccountFullNameEmpty = true
                                            Log.i("TAG", "Full Name Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogatePhoneNumber) -> {
                                            yourAccountViewModel.yourAccountPhoneNumberEmpty = true
                                            Log.i("TAG", "Phone Number Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateEmail) -> {
                                            yourAccountViewModel.yourAccountEmailEmpty = true
                                            Log.i("TAG", "Email Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateHomeAddress) -> {
                                            yourAccountViewModel.yourAccountHomeAddressEmpty = true
                                            Log.i("TAG", "Home Address Empty")
                                        }
                                        TextUtils.isEmpty(yourAccountViewModel.surrogateDateOfBirth) -> {
                                            yourAccountViewModel.yourAccountDateOfBirthEmpty = true
                                            Log.i("TAG", "DOB Empty")
                                        }
                                        !Patterns.EMAIL_ADDRESS.matcher(yourAccountViewModel.surrogateEmail)
                                            .matches() -> {
                                            Log.i("TAG", "Invalid Email Empty")
                                            yourAccountViewModel.yourAccountEmailIsValid = true
                                        }
                                        else -> {
                                            yourAccountViewModel.isEditable.value = false
                                            val image = yourAccountViewModel.uriPath?.let {
                                                convertImageMultiPart(it)
                                            }
                                            yourAccountViewModel.updateUserProfile(
                                                userId,
                                                MultipartBody.Part.createFormData(
                                                    "name",
                                                    yourAccountViewModel.surrogateFullName
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "email",
                                                    yourAccountViewModel.surrogateEmail
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "number",
                                                    yourAccountViewModel.surrogatePhoneNumber
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "address",
                                                    yourAccountViewModel.surrogateHomeAddress
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "date_of_birth",
                                                    yourAccountViewModel.surrogateDateOfBirth
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "partner_name",
                                                    yourAccountViewModel.surrogatePartnerName
                                                ),
                                                image,
                                                null,
                                                MultipartBody.Part.createFormData(
                                                    "type",
                                                    type
                                                ),
                                            )
                                            yourAccountViewModel.updateUserProfileResponse.observe(
                                                homeActivity
                                            ) {
                                                if (it != null) {
                                                    handleUserUpdateData(
                                                        result = it,
                                                        yourAccountViewModel = yourAccountViewModel,
                                                        context = context
                                                    )
                                                }
                                            }
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
                            val logout = context.resources.getString(R.string.logout)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp, bottom = 32.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        openLogoutDialog.value = true
                                    },
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
                                        .wrapContentWidth()
                                        .placeholder(
                                            visible = yourAccountViewModel.isLoading,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(4.dp),
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = Color.White,
                                            )
                                        ),
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
                                            .padding(end = 2.dp)
                                            .placeholder(
                                                visible = yourAccountViewModel.isLoading,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(4.dp),
                                                highlight = PlaceholderHighlight.shimmer(
                                                    highlightColor = Color.White,
                                                )
                                            ),
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
                                            .wrapContentWidth()
                                            .placeholder(
                                                visible = yourAccountViewModel.isLoading,
                                                color = Color.LightGray,
                                                shape = RoundedCornerShape(4.dp),
                                                highlight = PlaceholderHighlight.shimmer(
                                                    highlightColor = Color.White,
                                                )
                                            ),
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
                                        .padding(top = 18.dp)
                                        .placeholder(
                                            visible = yourAccountViewModel.isLoading,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(4.dp),
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = Color.White,
                                            )
                                        ),
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
                                        .padding(top = 11.dp)
                                        .placeholder(
                                            visible = yourAccountViewModel.isLoading,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(4.dp),
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = Color.White,
                                            )
                                        ),
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
                                        .padding(top = 16.dp)
                                        .placeholder(
                                            visible = yourAccountViewModel.isLoading,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(4.dp),
                                            highlight = PlaceholderHighlight.shimmer(
                                                highlightColor = Color.White,
                                            )
                                        ),
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
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 23.dp, top = 34.dp)
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                text = "What is your favorite snack?",
                                color = Color.Black,
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 24.sp
                                ),
                            )
                            Row {
                                Text(
                                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                    text = "Martha Smith",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Custom_Blue,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        lineHeight = 22.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, end = 24.dp),
                                    text = "1 Day ago",
                                    color = Color(0xFF9F9D9B),
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    top = 4.dp,
                                    bottom = 22.dp
                                ),
                                text = "Chocolate all the way!!",
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 22.sp
                                ),
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 18.dp)
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                text = "What is your favorite snack?",
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 24.sp
                                ),
                            )
                            Row {
                                Text(
                                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                    text = "Samantha  Jones",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Custom_Blue,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        lineHeight = 22.sp
                                    ),
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, end = 24.dp),
                                    text = "1 Day ago",
                                    color = Color(0xFF9F9D9B),
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    top = 4.dp,
                                    bottom = 22.dp
                                ),
                                text = "Basketball and Miami Heat",
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
        PARENT -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 34.dp, bottom = 50.dp)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (img_father, img_edit_father, img_father_arrow, img_mother, img_edit_mother, img_mother_arrow) = createRefs()
                    val painter1 = rememberImagePainter(yourAccountViewModel.parentImg1,
                        builder = { placeholder(R.drawable.ic_placeholder_your_account) })
                    val painter2 = rememberImagePainter(
                        yourAccountViewModel.parentImg2,
                        builder = { placeholder(R.drawable.ic_placeholder_your_account) }
                    )
                    Image(
                        modifier = Modifier
                            .width(88.dp)
                            .height(88.dp)
                            .padding(end = 10.dp)
                            .constrainAs(img_father) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                yourAccountViewModel.isParentClicked = true
                                yourAccountViewModel.isMotherClicked = false
                            }
                            .placeholder(
                                visible = yourAccountViewModel.isLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        painter = if (yourAccountViewModel.parentImg1 != "") painter1 else painterResource(
                            id = R.drawable.ic_placeholder_your_account
                        ),
                        contentDescription = "",
                    )
                    if (yourAccountViewModel.isParentClicked) {
                        Image(
                            modifier = Modifier
                                .constrainAs(img_edit_father) {
                                    start.linkTo(img_father.start)
                                    end.linkTo(img_father.end)
                                    top.linkTo(img_father.top)
                                    bottom.linkTo(img_father.bottom)
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
                            contentDescription = ""
                        )
                    }
                    Image(
                        modifier = Modifier.padding(end = 10.dp).constrainAs(img_father_arrow) {
                            start.linkTo(img_father.start)
                            end.linkTo(img_father.end)
                            top.linkTo(img_father.bottom)
                        },
                        painter = painterResource(
                            id = R.drawable.ic_baseline_arrow_drop_up_24
                        ),
                        contentDescription = "",
                        alpha = if (!yourAccountViewModel.isParentClicked) 0f else 1f
                    )
                    Image(
                        modifier = Modifier
                            .width(88.dp)
                            .height(88.dp)
                            .padding(start = 15.dp)
                            .constrainAs(img_mother) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                yourAccountViewModel.isParentClicked = false
                                yourAccountViewModel.isMotherClicked = true
                            }
                            .placeholder(
                                visible = yourAccountViewModel.isLoading,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(4.dp),
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White,
                                )
                            ),
                        painter = if (yourAccountViewModel.parentImg2 != "") painter2 else painterResource(
                            id = R.drawable.ic_placeholder_your_account
                        ),
                        contentDescription = "",
                    )
                    if (yourAccountViewModel.isMotherClicked) {
                        Image(
                            modifier = Modifier
                                .constrainAs(img_edit_mother) {
                                    start.linkTo(img_mother.start)
                                    end.linkTo(img_mother.end)
                                    top.linkTo(img_mother.top)
                                    bottom.linkTo(img_mother.bottom)
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
                            contentDescription = ""
                        )
                    }
                    Image(
                        modifier = Modifier.padding(start = 10.dp).constrainAs(img_mother_arrow) {
                            start.linkTo(img_mother.start)
                            end.linkTo(img_mother.end)
                            top.linkTo(img_mother.bottom)
                        },
                        painter = painterResource(
                            id = R.drawable.ic_baseline_arrow_drop_up_24
                        ),
                        contentDescription = "",
                        alpha = if (!yourAccountViewModel.isMotherClicked) 0f else 1f
                    )
                    createHorizontalChain(
                        img_father,
                        img_mother,
                        chainStyle = ChainStyle.Packed
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
                            if (yourAccountViewModel.parentFullName != "") {
                                Text(
                                    modifier = Modifier.wrapContentWidth(),
                                    text =
                                    yourAccountViewModel.parentFullName,
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
                                        text =
                                        yourAccountViewModel.parentDateOfBirth,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color(0xFF7F7D7C),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                Text(
                                    modifier = Modifier.wrapContentWidth(),
                                    text = if (yourAccountViewModel.isParentClicked) "(37 Year)" else "(30 Year)",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        lineHeight = 22.sp
                                    )
                                )
                            }
                            Image(
                                modifier = Modifier.padding(top = 10.dp),
                                painter = painterResource(id = R.drawable.ic_img_intended_parents_liner),
                                contentDescription = ""
                            )
                            if (yourAccountViewModel.parentHomeAddress != "") {
                                Text(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(top = 18.dp),
                                    text =
                                    yourAccountViewModel.parentHomeAddress,
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        lineHeight = 22.sp
                                    )
                                )
                            }
                            if (yourAccountViewModel.parentPhoneNumber != "") {
                                Text(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(top = 11.dp),
                                    text =
                                    yourAccountViewModel.parentPhoneNumber,
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
                                ), readOnly = true,
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
                            if (yourAccountViewModel.yourAccountPhoneNumberEmpty) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp),
                                    text = stringResource(id = R.string.enter_phone_number),
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
                            if (yourAccountViewModel.yourAccountEmailEmpty) {
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
                            if (yourAccountViewModel.yourAccountEmailIsValid) {
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
                                value = yourAccountViewModel.parentHomeAddress,
                                onValueChange = {
                                    yourAccountViewModel.parentHomeAddress = it
                                    yourAccountViewModel.yourAccountHomeAddressEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
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
                                        painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountHomeAddressEmpty) {
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
                                value = yourAccountViewModel.parentDateOfBirth,
                                onValueChange = {
                                    yourAccountViewModel.parentDateOfBirth = it
                                    yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ), readOnly = !yourAccountViewModel.isEditable.value,
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
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_calendar_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountDateOfBirthEmpty) {
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
                                value = yourAccountViewModel.parentPartnerName,
                                onValueChange = {
                                    yourAccountViewModel.surrogatePartnerName = it
                                    yourAccountViewModel.yourAccountPartnerNameEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
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
                                        painter = painterResource(id = R.drawable.ic_icon_et_parent_name_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountPartnerNameEmpty) {
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
                            if (yourAccountViewModel.yourAccountPasswordEmpty) {
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
                            if (yourAccountViewModel.isEditable.value) {
                                Button(
                                    onClick = {
                                        when {
                                            TextUtils.isEmpty(yourAccountViewModel.parentFullName) && TextUtils.isEmpty(
                                                yourAccountViewModel.parentPhoneNumber
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.parentEmail
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.parentHomeAddress
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.parentDateOfBirth
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.parentPartnerName
                                            ) -> {
                                                yourAccountViewModel.yourAccountFullNameEmpty = true
                                                yourAccountViewModel.yourAccountPhoneNumberEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountEmailEmpty = true
                                                yourAccountViewModel.yourAccountHomeAddressEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountDateOfBirthEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountPartnerNameEmpty =
                                                    true
                                                Log.i("TAG", "All Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentFullName) -> {
                                                yourAccountViewModel.yourAccountFullNameEmpty = true
                                                Log.i("TAG", "Full Name Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentPhoneNumber) -> {
                                                yourAccountViewModel.yourAccountPhoneNumberEmpty =
                                                    true
                                                Log.i("TAG", "Phone Number Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentEmail) -> {
                                                yourAccountViewModel.yourAccountEmailEmpty = true
                                                Log.i("TAG", "Email Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentHomeAddress) -> {
                                                yourAccountViewModel.yourAccountHomeAddressEmpty =
                                                    true
                                                Log.i("TAG", "Home Address Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentDateOfBirth) -> {
                                                yourAccountViewModel.yourAccountDateOfBirthEmpty =
                                                    true
                                                Log.i("TAG", "DOB Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.parentPartnerName) -> {
                                                yourAccountViewModel.yourAccountPartnerNameEmpty =
                                                    true
                                                Log.i("TAG", "Partner Name Empty")
                                            }
                                            !Patterns.EMAIL_ADDRESS.matcher(yourAccountViewModel.parentEmail)
                                                .matches() -> {
                                                Log.i("TAG", "Invalid Email Empty")
                                                yourAccountViewModel.yourAccountEmailIsValid = true
                                            }
                                            yourAccountViewModel.uriPath == null -> {
                                                Toast.makeText(
                                                    context,
                                                    "Please select image",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            else -> {
                                                yourAccountViewModel.isEditable.value = false
                                                val image = yourAccountViewModel.uriPath?.let {
                                                    convertImageMultiPart(it)
                                                }
                                                yourAccountViewModel.updateUserProfile(
                                                    userId,
                                                    MultipartBody.Part.createFormData(
                                                        "name",
                                                        yourAccountViewModel.parentFullName
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "email",
                                                        yourAccountViewModel.parentEmail
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "number",
                                                        yourAccountViewModel.parentPhoneNumber
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "address",
                                                        yourAccountViewModel.parentHomeAddress
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "date_of_birth",
                                                        yourAccountViewModel.parentDateOfBirth
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "partner_name",
                                                        yourAccountViewModel.parentPartnerName
                                                    ),
                                                    image,
                                                    null,
                                                    MultipartBody.Part.createFormData(
                                                        "type",
                                                        type
                                                    ),
                                                )
                                                yourAccountViewModel.updateUserProfileResponse.observe(
                                                    homeActivity
                                                ) {
                                                    if (it != null) {
                                                        handleUserUpdateData(
                                                            result = it,
                                                            yourAccountViewModel = yourAccountViewModel,
                                                            context = context
                                                        )
                                                    }
                                                }
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
                                val logout = context.resources.getString(R.string.logout)
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
                                ), readOnly = true,
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
                            if (yourAccountViewModel.yourAccountPhoneNumberEmpty) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp),
                                    text = stringResource(id = R.string.enter_phone_number),
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
                            if (yourAccountViewModel.yourAccountEmailEmpty) {
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
                            if (yourAccountViewModel.yourAccountEmailIsValid) {
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
                                value = yourAccountViewModel.surrogateHomeAddress,
                                onValueChange = {
                                    yourAccountViewModel.surrogateHomeAddress = it
                                    yourAccountViewModel.yourAccountHomeAddressEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
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
                                        painter = painterResource(id = R.drawable.ic_icon_et_location_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountHomeAddressEmpty) {
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
                                value = yourAccountViewModel.surrogateDateOfBirth,
                                onValueChange = {
                                    yourAccountViewModel.surrogateDateOfBirth = it
                                    yourAccountViewModel.yourAccountDateOfBirthEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ), readOnly = !yourAccountViewModel.isEditable.value,
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
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.body2,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_icon_et_calendar_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountDateOfBirthEmpty) {
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
                                value = yourAccountViewModel.surrogatePartnerName,
                                onValueChange = {
                                    yourAccountViewModel.surrogatePartnerName = it
                                    yourAccountViewModel.yourAccountPartnerNameEmpty = false
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
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
                                        painter = painterResource(id = R.drawable.ic_icon_et_parent_name_your_account),
                                        "error",
                                    )
                                }
                            )
                            if (yourAccountViewModel.yourAccountPartnerNameEmpty) {
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
                            if (yourAccountViewModel.yourAccountPasswordEmpty) {
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
                            if (yourAccountViewModel.isEditable.value) {
                                Button(
                                    onClick = {
                                        when {
                                            TextUtils.isEmpty(yourAccountViewModel.surrogateFullName) && TextUtils.isEmpty(
                                                yourAccountViewModel.surrogatePhoneNumber
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.surrogateEmail
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.surrogateHomeAddress
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.surrogateDateOfBirth
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.surrogatePartnerName
                                            ) && TextUtils.isEmpty(
                                                yourAccountViewModel.yourAccountPassword
                                            ) -> {
                                                yourAccountViewModel.yourAccountFullNameEmpty = true
                                                yourAccountViewModel.yourAccountPhoneNumberEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountEmailEmpty = true
                                                yourAccountViewModel.yourAccountHomeAddressEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountDateOfBirthEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountPartnerNameEmpty =
                                                    true
                                                yourAccountViewModel.yourAccountPasswordEmpty = true
                                                Log.i("TAG", "All Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogateFullName) -> {
                                                yourAccountViewModel.yourAccountFullNameEmpty = true
                                                Log.i("TAG", "Full Name Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogatePhoneNumber) -> {
                                                yourAccountViewModel.yourAccountPhoneNumberEmpty =
                                                    true
                                                Log.i("TAG", "Phone Number Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogateEmail) -> {
                                                yourAccountViewModel.yourAccountEmailEmpty = true
                                                Log.i("TAG", "Email Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogateHomeAddress) -> {
                                                yourAccountViewModel.yourAccountHomeAddressEmpty =
                                                    true
                                                Log.i("TAG", "Home Address Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogateHomeAddress) -> {
                                                yourAccountViewModel.yourAccountDateOfBirthEmpty =
                                                    true
                                                Log.i("TAG", "DOB Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.surrogatePartnerName) -> {
                                                yourAccountViewModel.yourAccountPartnerNameEmpty =
                                                    true
                                                Log.i("TAG", "Partner Name Empty")
                                            }
                                            TextUtils.isEmpty(yourAccountViewModel.yourAccountPassword) -> {
                                                yourAccountViewModel.yourAccountPasswordEmpty = true
                                                Log.i("TAG", "Password Empty")
                                            }
                                            !Patterns.EMAIL_ADDRESS.matcher(yourAccountViewModel.surrogateEmail)
                                                .matches() -> {
                                                Log.i("TAG", "Invalid Email Empty")
                                                yourAccountViewModel.yourAccountEmailIsValid = true
                                            }
                                            yourAccountViewModel.uriPath == null -> {
                                                Toast.makeText(
                                                    context,
                                                    "Please select image",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            else -> {
                                                yourAccountViewModel.isEditable.value = false
                                                val image = yourAccountViewModel.uriPath?.let {
                                                    convertImageMultiPart(it)
                                                }
                                                yourAccountViewModel.updateUserProfile(
                                                    userId,
                                                    MultipartBody.Part.createFormData(
                                                        "name",
                                                        yourAccountViewModel.surrogateFullName
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "email",
                                                        yourAccountViewModel.surrogateEmail
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "number",
                                                        yourAccountViewModel.surrogatePhoneNumber
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "address",
                                                        yourAccountViewModel.surrogateHomeAddress
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "date_of_birth",
                                                        yourAccountViewModel.surrogateDateOfBirth
                                                    ),
                                                    MultipartBody.Part.createFormData(
                                                        "partner_name",
                                                        yourAccountViewModel.surrogatePartnerName
                                                    ),
                                                    image,
                                                    null,
                                                    MultipartBody.Part.createFormData(
                                                        "type",
                                                        type
                                                    ),
                                                )
                                                yourAccountViewModel.updateUserProfileResponse.observe(
                                                    homeActivity
                                                ) {
                                                    if (it != null) {
                                                        handleUserUpdateData(
                                                            result = it,
                                                            yourAccountViewModel = yourAccountViewModel,
                                                            context = context
                                                        )
                                                    }
                                                }
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
                                val logout = context.resources.getString(R.string.logout)
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
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 23.dp, top = 24.dp)
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                text = "What is your favorite snack?",
                                color = Color.Black,
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 24.sp
                                ),
                            )
                            Row {
                                Text(
                                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                    text = "Martha Smith",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Custom_Blue,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        lineHeight = 22.sp
                                    )
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, end = 24.dp),
                                    text = "1 Day ago",
                                    color = Color(0xFF9F9D9B),
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    top = 4.dp,
                                    bottom = 22.dp
                                ),
                                text = "Chocolate all the way!!",
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 22.sp
                                ),
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 18.dp)
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                text = "What is your favorite snack?",
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 24.sp
                                ),
                            )
                            Row {
                                Text(
                                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                    text = "Samantha  Jones",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Custom_Blue,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        lineHeight = 22.sp
                                    ),
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, end = 24.dp),
                                    text = "1 Day ago",
                                    color = Color(0xFF9F9D9B),
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    top = 4.dp,
                                    bottom = 22.dp
                                ),
                                text = "Basketball and Miami Heat",
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

private fun handleUserDataSurrogate(
    result: NetworkResult<GetUserDetailsSurrogateResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false
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
            if (result.data?.image1 != null) {
                yourAccountViewModel.surrogateImg = result.data.image1
            }
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
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
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false
            if (result.data?.parent_name != null) {
                yourAccountViewModel.parentFullName = result.data.parent_name
            }
            if (result.data?.parent_number != null) {
                yourAccountViewModel.parentPhoneNumber = result.data.parent_number
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
            if (result.data?.parent_partner_name != null) {
                yourAccountViewModel.parentPartnerName = result.data.parent_partner_name
            }
            if (result.data?.parent_image1 != null) {
                yourAccountViewModel.parentImg1 = result.data.parent_image1
            }
            if (result.data?.parent_image2 != null) {
                yourAccountViewModel.parentImg2 = result.data.parent_image2
            }
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun handleUserUpdateData(
    result: NetworkResult<UpdateUserProfileResponse>,
    yourAccountViewModel: YourAccountViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.data?.message, Toast.LENGTH_SHORT).show()

        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            yourAccountViewModel.isLoading = false
        }
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


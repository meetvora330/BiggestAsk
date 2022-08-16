package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.GetUserDetailsParentRequest
import com.biggestAsk.data.model.request.GetUserDetailsSurrogateRequest
import com.biggestAsk.data.model.response.GetUserDetailsParentResponse
import com.biggestAsk.data.model.response.GetUserDetailsSurrogateResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.YourAccountViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.util.PathUtil
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun YourAccountScreenTemp(
    navHostController: NavHostController,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
    context: Context
) {
    val focusManager = LocalFocusManager.current
    val isRationale = remember { mutableStateOf(false) }
    val provider = PreferenceProvider(context)
    var imageData by remember {
        mutableStateOf<Uri?>(null)
    }
    var uriPath: String? = null

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
        uriPath = uri?.let { it1 -> PathUtil.getPath(context, it1) }
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val type = PreferenceProvider(context).getValue("type", "")
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    if (ActivityCompat.checkSelfPermission(
                            homeActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        yourAccountViewModel.isPermissionAllowed = false
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(Unit) {
        when (type) {
            "surrogate" -> {
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
            "parent" -> {
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
    if (yourAccountViewModel.isLoading) {
        ProgressBarTransparentBackground("Loading...")
    } else {
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
                    val painter = rememberImagePainter(
                        yourAccountViewModel.surrogateImg,
                        builder = {
                            placeholder(R.drawable.ic_placeholder_your_account)
                        })
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .width(88.dp)
                            .height(88.dp)
                            .constrainAs(img_user) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            },
                        contentScale = ContentScale.Crop
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
                        .alpha(if (yourAccountViewModel.isEditable.value) 1f else 0f)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
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
                    ), readOnly = !yourAccountViewModel.isEditable.value,
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
                /*Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp),
                    text = "Password",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp,
                    color = Text_Accept_Terms
                )*/
                /*TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                    value = yourAccountViewModel.yourAccountPassword.value,
                    onValueChange = {
                        yourAccountViewModel.yourAccountPassword.value = it
                        yourAccountViewModel.yourAccountPasswordEmpty = false
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
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
                            painter = painterResource(id = R.drawable.ic_icon_et_password_your_account),
                            "error",
                        )
                    }, keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )*/
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
                                TextUtils.isEmpty(yourAccountViewModel.surrogateHomeAddress) -> {
                                    yourAccountViewModel.yourAccountDateOfBirthEmpty = true
                                    Log.i("TAG", "DOB Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.surrogatePartnerName) -> {
                                    yourAccountViewModel.yourAccountPartnerNameEmpty = true
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
                                else -> {
                                    yourAccountViewModel.isEditable.value = false
                                }
                            }
                            val image = uriPath?.let { convertImageMultiPart(it) }

                            yourAccountViewModel.updateUserProfile(
                                1,
                                MultipartBody.Part.createFormData(
                                    "name",
                                    yourAccountViewModel.surrogateFullName
                                ),
                                MultipartBody.Part.createFormData(
                                    "email",
                                    yourAccountViewModel.surrogateEmail
                                ),
                                MultipartBody.Part.createFormData(
                                    "password",
                                    yourAccountViewModel.yourAccountPassword
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
                                    "surrogate"
                                ),
                            )
                            yourAccountViewModel.updateUserProfileResponse.observe(homeActivity) {
                                if (it != null) {
                                    handleUserUpdateData(
                                        result = it,
                                        yourAccountViewModel = yourAccountViewModel,
                                        context = context
                                    )
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
    }
}



private fun handleUserData(
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
            yourAccountViewModel.surrogateFullName = result.data?.name.toString()
            yourAccountViewModel.surrogatePhoneNumber = result.data?.number.toString()
            yourAccountViewModel.surrogateEmail = result.data?.email.toString()
            yourAccountViewModel.surrogateHomeAddress = result.data?.address.toString()
            yourAccountViewModel.surrogateDateOfBirth = result.data?.date_of_birth.toString()
            yourAccountViewModel.surrogatePartnerName = result.data?.partner_name.toString()
            yourAccountViewModel.surrogateImg = result.data?.image1.toString()
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
            yourAccountViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            yourAccountViewModel.isLoading = false
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
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
            yourAccountViewModel.surrogateFullName = result.data?.name.toString()
            yourAccountViewModel.surrogatePhoneNumber = result.data?.number.toString()
            yourAccountViewModel.surrogateEmail = result.data?.email.toString()
            yourAccountViewModel.surrogateHomeAddress = result.data?.address.toString()
            yourAccountViewModel.surrogateDateOfBirth = result.data?.date_of_birth.toString()
            yourAccountViewModel.surrogatePartnerName = result.data?.partner_name.toString()
            yourAccountViewModel.surrogateImg = result.data?.image1.toString()
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
            yourAccountViewModel.parentFullName = result.data?.parent_name.toString()
            yourAccountViewModel.parentPhoneNumber = result.data?.parent_number.toString()
            yourAccountViewModel.parentEmail = result.data?.parent_email.toString()
            yourAccountViewModel.parentHomeAddress = result.data?.parent_address.toString()
            yourAccountViewModel.parentDateOfBirth = result.data?.parent_date_of_birth.toString()
            yourAccountViewModel.parentPartnerName = result.data?.parent_partner_name.toString()
            yourAccountViewModel.parentImg1 = result.data?.parent_image1.toString()
            yourAccountViewModel.parentImg2 = result.data?.parent_image2.toString()
        }
        is NetworkResult.Error -> {
            // show error message
            yourAccountViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

private fun convertImageMultiPart(imagePath: String): MultipartBody.Part? {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        "image1",
        file.name,
        file.asRequestBody("image/*".toMediaTypeOrNull())
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun YourAccountScreenPreview() {
    //    YourAccountScreen(YourAccountViewModel())
}

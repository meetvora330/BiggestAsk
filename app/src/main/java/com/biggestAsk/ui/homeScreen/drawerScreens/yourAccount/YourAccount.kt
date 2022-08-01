package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.biggestAsk.data.model.request.GetUserDetailsRequest
import com.biggestAsk.data.model.response.GetUserDetailsResponse
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
fun YourAccountScreen(
    navHostController: NavHostController,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
    context:Context
) {
    val focusManager = LocalFocusManager.current
    val isRationale = remember { mutableStateOf(false) }

    var imageData by remember {
        mutableStateOf<Uri?>(null)
    }
    val provider = PreferenceProvider(context)
    var uriPath: String? = null

    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        imageData = uri
    }

//    val permissionReqLauncher =
//        homeActivity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            when {
//                it -> {
//                    yourAccountViewModel.isPermissionAllowed.value = false
//                    launcher.launch("image/*")
//                    Log.d(
//                        "TAG",
//                        "Permission Granted"
//                    )
//                }
//                ActivityCompat.shouldShowRequestPermissionRationale(
//                    homeActivity,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) -> {
//                    isRationale.value = true
//                    Log.d(
//                        "TAG",
//                        "Permission Not Granted"
//                    )
//                }
//                else -> {
//                    Log.d(
//                        "TAG",
//                        "Permission Permanently Denied"
//                    )
//                    yourAccountViewModel.isPermissionAllowed.value = true
//                }
//            }
//        }
//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(
//        key1 = lifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver { _, event ->
//                if (event == Lifecycle.Event.ON_RESUME) {
//                    if (permissionState.status.isGranted) {
//                        yourAccountViewModel.isPermissionAllowed.value = false
//                    }
//                }
//            }
//            lifecycleOwner.lifecycle.addObserver(observer)
//            onDispose {
//                lifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )

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
        val userId = provider.getIntValue("user_id", 0)
        val type = provider.getValue("type", "")
        Log.d("TAG", "YourAccountScreen: User Id Is $userId")
        Log.d("TAG", "YourAccountScreen: Type is $type")
        yourAccountViewModel.getUserDetails(GetUserDetailsRequest(userId, type!!))
        yourAccountViewModel.getUserDetailResponse.observe(homeActivity) {
            if (it != null) {
                handleUserData(
                    result = it,
                    yourAccountViewModel = yourAccountViewModel,
                    context = context
                )
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
                        yourAccountViewModel.profileImg,
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
                            if (yourAccountViewModel.isEditable.value) {
                                if (ActivityCompat.checkSelfPermission(
                                        homeActivity,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    homeActivity.permissionReqLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                } else {
                                    launcher.launch("image/*")
                                    yourAccountViewModel.isPermissionAllowed = false
                                }
//                                when {
//                                    permissionState.status.isGranted -> {
//                                        launcher.launch("image/*")
//                                        yourAccountViewModel.isPermissionAllowed = false
//                                    }
//                                    permissionState.status.shouldShowRationale -> {
//                                        permissionState.launchPermissionRequest()
//                                        yourAccountViewModel.isPermissionAllowed = false
//                                        yourAccountViewModel.isRational = true
//                                    }
//                                    !permissionState.status.isGranted -> {
//                                        permissionState.launchPermissionRequest()
//                                        yourAccountViewModel.isPermissionAllowed =
//                                            yourAccountViewModel.isRational
//                                    }
//                                }
                            }
                        },
                    painter = painterResource(id = R.drawable.ic_icon_camera_edit_img_your_account),
                    contentDescription = ""
                )
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
                    value = yourAccountViewModel.yourAccountFullName,
                    onValueChange = {
                        yourAccountViewModel.yourAccountFullName = it
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
                    value = yourAccountViewModel.yourAccountPhoneNumber,
                    onValueChange = {
                        yourAccountViewModel.yourAccountPhoneNumber = it
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
                    value = yourAccountViewModel.yourAccountEmail,
                    onValueChange = {
                        yourAccountViewModel.yourAccountEmail = it
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
                    value = yourAccountViewModel.yourAccountHomeAddress,
                    onValueChange = {
                        yourAccountViewModel.yourAccountHomeAddress = it
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
                    value = yourAccountViewModel.yourAccountDateOfBirth,
                    onValueChange = {
                        yourAccountViewModel.yourAccountDateOfBirth = it
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
                    value = yourAccountViewModel.yourAccountPartnerName,
                    onValueChange = {
                        yourAccountViewModel.yourAccountPartnerName = it
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
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountFullName) && TextUtils.isEmpty(
                                    yourAccountViewModel.yourAccountPhoneNumber
                                ) && TextUtils.isEmpty(
                                    yourAccountViewModel.yourAccountEmail
                                ) && TextUtils.isEmpty(
                                    yourAccountViewModel.yourAccountHomeAddress
                                ) && TextUtils.isEmpty(
                                    yourAccountViewModel.yourAccountDateOfBirth
                                ) && TextUtils.isEmpty(
                                    yourAccountViewModel.yourAccountPartnerName
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
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountFullName) -> {
                                    yourAccountViewModel.yourAccountFullNameEmpty = true
                                    Log.i("TAG", "Full Name Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountPhoneNumber) -> {
                                    yourAccountViewModel.yourAccountPhoneNumberEmpty = true
                                    Log.i("TAG", "Phone Number Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountEmail) -> {
                                    yourAccountViewModel.yourAccountEmailEmpty = true
                                    Log.i("TAG", "Email Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountHomeAddress) -> {
                                    yourAccountViewModel.yourAccountHomeAddressEmpty = true
                                    Log.i("TAG", "Home Address Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountDateOfBirth) -> {
                                    yourAccountViewModel.yourAccountDateOfBirthEmpty = true
                                    Log.i("TAG", "DOB Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountPartnerName) -> {
                                    yourAccountViewModel.yourAccountPartnerNameEmpty = true
                                    Log.i("TAG", "Partner Name Empty")
                                }
                                TextUtils.isEmpty(yourAccountViewModel.yourAccountPassword) -> {
                                    yourAccountViewModel.yourAccountPasswordEmpty = true
                                    Log.i("TAG", "Password Empty")
                                }
                                !Patterns.EMAIL_ADDRESS.matcher(yourAccountViewModel.yourAccountEmail)
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
                                    yourAccountViewModel.yourAccountFullName
                                ),
                                MultipartBody.Part.createFormData(
                                    "email",
                                    yourAccountViewModel.yourAccountEmail
                                ),
                                MultipartBody.Part.createFormData(
                                    "password",
                                    yourAccountViewModel.yourAccountPassword
                                ),
                                MultipartBody.Part.createFormData(
                                    "number",
                                    yourAccountViewModel.yourAccountPhoneNumber
                                ),
                                MultipartBody.Part.createFormData(
                                    "address",
                                    yourAccountViewModel.yourAccountHomeAddress
                                ),
                                MultipartBody.Part.createFormData(
                                    "date_of_birth",
                                    yourAccountViewModel.yourAccountDateOfBirth
                                ),
                                MultipartBody.Part.createFormData(
                                    "partner_name",
                                    yourAccountViewModel.yourAccountPartnerName
                                ),
                                image,
                                null,
                                MultipartBody.Part.createFormData(
                                    "type",
                                    yourAccountViewModel.yourAccountType
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
    result: NetworkResult<GetUserDetailsResponse>,
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
            yourAccountViewModel.yourAccountFullName = result.data?.name.toString()
            yourAccountViewModel.yourAccountPhoneNumber = result.data?.number.toString()
            yourAccountViewModel.yourAccountEmail = result.data?.email.toString()
            yourAccountViewModel.yourAccountHomeAddress = result.data?.address.toString()
            yourAccountViewModel.yourAccountDateOfBirth = result.data?.date_of_birth.toString()
            yourAccountViewModel.yourAccountPartnerName = result.data?.partner_name.toString()
            yourAccountViewModel.profileImg = result.data?.image1.toString()
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

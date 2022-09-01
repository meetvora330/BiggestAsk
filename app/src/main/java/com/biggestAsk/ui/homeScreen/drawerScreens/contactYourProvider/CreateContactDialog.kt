package com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.biggestAsk.data.model.response.CreateContactResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.ContactYourProviderViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.regex.Pattern

@Composable
fun CreateContactDialog(
    homeActivity: HomeActivity,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    openDialogCustom: MutableState<Boolean>,
    tf_text_first: MutableState<String>,
    tf_text_second: MutableState<String>,
    tf_text_third: MutableState<String>,
    tf_text_fourth: MutableState<String>,
    tf_hint_tv1: String,
    tv_text_tittle: String,
    tf_hint_tv2: String,
    tv_text_second: String,
    tf_hint_tv3: String,
    tv_text_third: String,
    tf_hint_tv4: String,
    tv_text_fourth: String,
    btn_text_add: String,
) {
    val tfTextFirstEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextSecondEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextThirdEmpty = remember {
        mutableStateOf(false)
    }
    val tfTextFourthEmpty = remember {
        mutableStateOf(false)
    }
    val regex = Pattern.compile(Constants.REGEX_PHONE_PATTERN)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
                        contactYourProviderViewModel.isPermissionAllowed = false
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        contactYourProviderViewModel.imageData = uri
        contactYourProviderViewModel.getImage(context)
        contactYourProviderViewModel.isImagePresent.value = uri != null
    }


    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
        ) {
            val (dialog_community_tittle, icon_close_dialog) = createRefs()
            Text(
                modifier = Modifier.constrainAs(dialog_community_tittle) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                text = tv_text_tittle,
                style = MaterialTheme.typography.h1,
                fontSize = 22.sp,
                fontWeight = FontWeight.W900,
                color = Color.Black,
                lineHeight = 28.sp
            )
            Icon(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .constrainAs(icon_close_dialog) {
                        end.linkTo(parent.end, margin = 17.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                    }
                    .clickable {
                        openDialogCustom.value = false
                        tf_text_first.value = ""
                        tf_text_second.value = ""
                        tf_text_third.value = ""
                        tf_text_fourth.value = ""
                        contactYourProviderViewModel.bitmap.value = null
                        contactYourProviderViewModel.phoneErrorVisible = false
                    },
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp, start = 12.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.tittle),
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_first.value,
                onValueChange = {
                    tf_text_first.value = it
                    tfTextFirstEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv1,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextFirstEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.enter_tittle),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_second,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_second.value,
                onValueChange = {
                    tf_text_second.value = it
                    tfTextSecondEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv2,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextSecondEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_second",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_third,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_third.value.trim(),
                onValueChange = {
                    tf_text_third.value = it
                    tfTextThirdEmpty.value = false
                    contactYourProviderViewModel.isLoginEmailValid = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv3,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1
            )
            if (tfTextThirdEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_third",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            if (contactYourProviderViewModel.isLoginEmailValid) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.register_error_text_valid_email),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    fontSize = 12.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = tv_text_fourth,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900,
                letterSpacing = (-0.24).sp
            )
            TextField(
                shape = RoundedCornerShape(9.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = tf_text_fourth.value.trim(),
                onValueChange = {
                    val maxChar = 12
                    val minChar = 8
                    if (it.length <= maxChar) {
                        if ((it.startsWith("+") && !regex.matcher(it)
                                .find()) || it.isDigitsOnly()
                        ) {
                            tf_text_fourth.value = it
                            contactYourProviderViewModel.phoneErrorVisible = minChar > it.length
                        }
                    }
                    tfTextFourthEmpty.value = false
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF2F2F7),
                    cursorColor = Custom_Blue,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = tf_hint_tv4,
                        color = Text_Accept_Terms
                    )
                },
                maxLines = 1,
            )
            if (tfTextFourthEmpty.value) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Enter $tv_text_fourth",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            if (contactYourProviderViewModel.phoneErrorVisible) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.minimum_8_character_required),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_icon_attachment_community_add_logo),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .padding(start = 11.dp)
                        .clickable {
                            if (ActivityCompat.checkSelfPermission(
                                    homeActivity,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                homeActivity.callPermissionRequestLauncher(launcher)
                                contactYourProviderViewModel.isPermissionAllowed = false
                            } else {
                                launcher.launch(Constants.IMAGE_LAUNCHER)
                                contactYourProviderViewModel.isPermissionAllowed = false
                            }
                        },
                    text = stringResource(id = R.string.add_logo),
                    style = MaterialTheme.typography.body1,
                    color = Color(0xFF007AFF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                )
            }

            contactYourProviderViewModel.bitmap.value?.let {
                Card(
                    modifier = Modifier
                        .width(88.dp)
                        .padding(top = 15.dp)
                        .height(88.dp),
                    shape = RoundedCornerShape(10.dp)
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
            Button(
                onClick = {
                    when {
                        TextUtils.isEmpty(tf_text_first.value) &&
                                TextUtils.isEmpty(tf_text_second.value) &&
                                TextUtils.isEmpty(tf_text_third.value) &&
                                TextUtils.isEmpty(tf_text_fourth.value) -> {
                            tfTextFirstEmpty.value = true
                            tfTextSecondEmpty.value = true
                            tfTextThirdEmpty.value = true
                            tfTextFourthEmpty.value = true
                            if (!contactYourProviderViewModel.isImagePresent.value) {
                                Toast.makeText(context,
                                    Constants.PLEASE_ADD_LOGO,
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        TextUtils.isEmpty(tf_text_first.value) -> {
                            tfTextFirstEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_second.value) -> {
                            tfTextSecondEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_third.value) -> {
                            tfTextThirdEmpty.value = true
                        }
                        TextUtils.isEmpty(tf_text_fourth.value) -> {
                            tfTextFourthEmpty.value = true
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(tf_text_third.value.trim()).matches() -> {
                            contactYourProviderViewModel.isLoginEmailValid = true
                        }
                        !contactYourProviderViewModel.isImagePresent.value -> {
                            Toast.makeText(context, Constants.PLEASE_ADD_LOGO, Toast.LENGTH_SHORT)
                                .show()
                        }

                        !contactYourProviderViewModel.phoneErrorVisible && contactYourProviderViewModel.isImagePresent.value && !TextUtils.isEmpty(
                            tf_text_first.value) &&
                                !TextUtils.isEmpty(tf_text_second.value) &&
                                !TextUtils.isEmpty(tf_text_third.value) &&
                                !TextUtils.isEmpty(tf_text_fourth.value) -> {

                            val image =
                                contactYourProviderViewModel.uriPath?.let { convertImageMultiPart(it) }

                            contactYourProviderViewModel.createContact(
                                MultipartBody.Part.createFormData(Constants.TITLE,
                                    tf_text_first.value),
                                MultipartBody.Part.createFormData(Constants.AGENCY_NAME_CREATE_CONTACT,
                                    tf_text_second.value),
                                MultipartBody.Part.createFormData(Constants.AGENCY_EMAIL_CREATE_CONTACT,
                                    tf_text_third.value),
                                MultipartBody.Part.createFormData(Constants.AGENCY_NUMBER_CREATE_CONTACT,
                                    tf_text_fourth.value),
                                image,
                                MultipartBody.Part.createFormData(Constants.USER_ID,
                                    userId.toString()),
                                MultipartBody.Part.createFormData(Constants.TYPE, type!!)
                            )
                            contactYourProviderViewModel.createContactResponse.observe(homeActivity) {
                                if (it != null) {
                                    handleCreateContactData(
                                        result = it,
                                        contactYourProviderViewModel = contactYourProviderViewModel,
                                        context = context,
                                        openDialogCustom,
                                        tf_text_first,
                                        tf_text_second,
                                        tf_text_third,
                                        tf_text_fourth,
                                        user_id = userId,
                                        type = type,
                                        homeActivity = homeActivity
                                    )
                                }
                            }
                        }
                        else -> {
                            openDialogCustom.value = contactYourProviderViewModel.phoneErrorVisible
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 31.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .height(45.dp),
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
                    text = btn_text_add,
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
    if (contactYourProviderViewModel.isLoading) {
        ProgressBarTransparentBackground(stringResource(id = R.string.please_wait))
    }
    if (contactYourProviderViewModel.isPermissionAllowed) {
        AlertDialog(
            onDismissRequest = {
                contactYourProviderViewModel.isPermissionAllowed = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                })
                { Text(text = stringResource(id = R.string.app_settings), color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = {
                    contactYourProviderViewModel.isPermissionAllowed = false
                })
                { Text(text = stringResource(id = R.string.cancel_dialog), color = Color.Red) }
            },
            title = { Text(text = stringResource(id = R.string.permission_denied_dialog)) },
            text = { Text(text = stringResource(id = R.string.allow_permission_dialog)) }
        )
    }
}

private fun convertImageMultiPart(imagePath: String): MultipartBody.Part? {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        Constants.IMAGE,
        file.name,
        file.asRequestBody(Constants.IMAGE_LAUNCHER.toMediaTypeOrNull())
    )
}

private fun handleCreateContactData(
    result: NetworkResult<CreateContactResponse>,
    contactYourProviderViewModel: ContactYourProviderViewModel,
    context: Context,
    openDialogCustom: MutableState<Boolean>,
    tf_text_first: MutableState<String>,
    tf_text_second: MutableState<String>,
    tf_text_third: MutableState<String>,
    tf_text_fourth: MutableState<String>,
    type: String,
    user_id: Int,
    homeActivity: HomeActivity,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            contactYourProviderViewModel.isLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            contactYourProviderViewModel.isLoading = false
            openDialogCustom.value = false
            tf_text_first.value = ""
            tf_text_second.value = ""
            tf_text_third.value = ""
            tf_text_fourth.value = ""
            contactYourProviderViewModel.bitmap.value = null
            Toast.makeText(context, result.data!!.message, Toast.LENGTH_SHORT).show()
            getUpdatedContact(type = type,
                user_id = user_id,
                contactYourProviderViewModel = contactYourProviderViewModel,
                context,
                homeActivity)
        }
        is NetworkResult.Error -> {
            // show error message
            contactYourProviderViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}


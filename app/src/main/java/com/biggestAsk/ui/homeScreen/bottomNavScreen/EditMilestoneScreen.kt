package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.DeleteMilestoneImageRequest
import com.biggestAsk.data.model.request.EditMilestoneRequest
import com.biggestAsk.data.model.request.SaveNoteRequest
import com.biggestAsk.data.model.request.UpdateMilestoneAnsInfoRequest
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.main.viewmodel.EditMilestoneViewModel
import com.biggestAsk.ui.ui.theme.CheckBox_Check
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PathUtil
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EditMilestoneScreen(
    navHostController: NavHostController,
    milestoneId: Int,
    editMilestoneViewModel: EditMilestoneViewModel,
    homeActivity: HomeActivity
) {
    val context =
        LocalContext.current
    val focusManager = LocalFocusManager.current
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]

    val stroke = Stroke(
        width = 5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
    )
    val editMilestoneBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val latestIndex = remember { mutableStateOf(0) }
    var uriPath: String?
    val lifecycleOwner = LocalLifecycleOwner.current
    val provider = PreferenceProvider(context)
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
                        editMilestoneViewModel.isPermissionAllowed.value = false
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            Log.i("TAG", "The URI is $uri")
            uriPath = uri.let { it1 -> PathUtil.getPath(context, it1) }
            if (editMilestoneViewModel.imageListIndex.value != -1) {
                val image = uriPath?.let { convertImageMultiPartSingle(it) }
                editMilestoneViewModel.updateMilestoneImage(
                    MultipartBody.Part.createFormData(
                        Constants.IMAGE_ID,
                        editMilestoneViewModel.imageList[latestIndex.value].id.toString()
                    ),
                    image
                )
                editMilestoneViewModel.updateMilestoneImage.observe(
                    homeActivity
                ) {
                    if (it != null) {
                        handleUpdateImageData(
                            result = it,
                            editMilestoneViewModel = editMilestoneViewModel,
                            context = context,
                            latestIndex.value
                        )
                    }
                }
            } else {
                editMilestoneViewModel.imageList.add(
                    EditMilestoneImageResponse(
                        "", 0, "", 0, "", "", 0, uriPath, uri,
                        is_need_to_upload = true
                    )
                )
            }
            editMilestoneViewModel.imageListIndex.value = -1
            Log.i("TAG", editMilestoneViewModel.imageList.size.toString())
            Log.i("TAG", "The URI Path is ${uriPath.toString()}")
        }
    }
    val isPicAvailable = remember {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()
    val type = provider.getValue(Constants.TYPE, "")
    val partnerId = provider.getIntValue(Constants.PARTNER_ID, 0)
    val milestoneType = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        editMilestoneViewModel.editMilestoneLocationB.value = ""
        editMilestoneViewModel.editMilestoneTimeEmpty.value = false
        editMilestoneViewModel.editMilestoneDateEmpty.value = false
        editMilestoneViewModel.editMilestoneLocationBEmpty.value = false
        editMilestoneViewModel.milestoneType.value = ""
        Log.d("TAG", "EditMilestoneScreen: $milestoneId")
        getUpdatedMilestone(
            homeActivity = homeActivity,
            editMilestoneViewModel = editMilestoneViewModel,
            context = context, type = type,
            milestoneId = milestoneId,
            partner_id = partnerId,
        )
    }

    if (!editMilestoneViewModel.isEditMilestoneDataLoaded.value) {
        BottomSheetScaffold(
            scaffoldState = editMilestoneBottomSheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 55.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(top = 11.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.update_information), style = MaterialTheme.typography.h2.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W600,
                                lineHeight = 32.sp,
                                color = Color.Black
                            )
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .align(Alignment.CenterHorizontally),
                            text = "Posuere faucibus laoreet vitae fermentum. Porttitor\nquis egestas porta arcu scelerisque sed sed turpis.",
                            style = MaterialTheme.typography.body2.copy(
                                color = Color(0xFF7F7D7C),
                                fontWeight = FontWeight.W600,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 20.dp),
                            text = stringResource(id = R.string.tittle),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = Color(0xFF7F7D7C)
                            )
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(start = 24.dp, end = 24.dp, top = 12.dp),
                            value = editMilestoneViewModel.editMilestoneTittle.value,
                            onValueChange = {
                                editMilestoneViewModel.editMilestoneTittle.value = it
                                editMilestoneViewModel.editMilestoneTittleEmpty.value = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            textStyle = MaterialTheme.typography.body2.copy(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400
                            ), singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_tittle),
                                    style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                )
                            }, readOnly = editMilestoneViewModel.isMilestoneTittleEditable.value,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFFF4F4F4),
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        if (editMilestoneViewModel.editMilestoneTittleEmpty.value) {
                            if (!editMilestoneViewModel.isMilestoneTittleEditable.value) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp),
                                    text = stringResource(id = R.string.enter_tittle),
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.error,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = stringResource(id = R.string.date),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = Color(0xFF7F7D7C)
                            )
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
                                            editMilestoneViewModel.editMilestoneDate.value =
                                                "$year/" + "%02d".format(month + 1) + "/" + "%02d".format(
                                                    day
                                                )
                                        }, year, month, day
                                    )
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
                                    editMilestoneViewModel.editMilestoneDateEmpty.value = false
                                },
                            value = editMilestoneViewModel.editMilestoneDate.value,
                            onValueChange = {
                                editMilestoneViewModel.editMilestoneDateEmpty.value = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFFF4F4F4),
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ), readOnly = true, enabled = false, placeholder = {
                                Text(
                                    text = stringResource(id = R.string.select_date),
                                    style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                )
                            },
                            textStyle = MaterialTheme.typography.body2.copy(
                                color = Color.Black
                            )
                        )
                        if (editMilestoneViewModel.editMilestoneDateEmpty.value) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = stringResource(id = R.string.enter_date),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = stringResource(id = R.string.start_time),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = Color(0xFF7F7D7C)
                            )
                        )
                        TextField(
                            modifier = Modifier
                                .width(145.dp)
                                .wrapContentHeight()
                                .padding(start = 24.dp, top = 12.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    val timePickerDialog = TimePickerDialog(
                                        context,
                                        R.style.CalenderViewCustom,
                                        { _, hourOfDay, minute ->
                                            val amPm: String = if (hourOfDay < 12) {
                                                "AM"
                                            } else {
                                                "PM"
                                            }
                                            editMilestoneViewModel.editMilestoneTime.value =
                                                "%02d".format(hourOfDay) + ":" + "%02d".format(
                                                    minute
                                                ) + " " + amPm
                                        }, mHour, mMinute, false
                                    )
                                    timePickerDialog.show()
                                    timePickerDialog
                                        .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                        .setTextColor(
                                            ContextCompat.getColor(context, R.color.custom_blue)
                                        )
                                    timePickerDialog
                                        .getButton(DatePickerDialog.BUTTON_POSITIVE)
                                        .setTextColor(
                                            ContextCompat.getColor(context, R.color.custom_blue)
                                        )
                                    timePickerDialog
                                        .getButton(DatePickerDialog.BUTTON_NEGATIVE)
                                        .setOnClickListener {
                                            timePickerDialog.dismiss()
                                        }
                                    focusManager.clearFocus()
                                    editMilestoneViewModel.editMilestoneTimeEmpty.value = false
                                }, readOnly = true, enabled = false,
                            value = editMilestoneViewModel.editMilestoneTime.value,
                            onValueChange = {
                                editMilestoneViewModel.editMilestoneTime.value
                                editMilestoneViewModel.editMilestoneTimeEmpty.value = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFFF4F4F4),
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.select_time),
                                    style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                )
                            },
                            textStyle = MaterialTheme.typography.body2.copy(
                                color = Color.Black
                            )
                        )
                        if (editMilestoneViewModel.editMilestoneTimeEmpty.value) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = stringResource(id = R.string.enter_time),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = stringResource(id = R.string.location),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = Color(0xFF7F7D7C)
                            )
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 12.dp),
                            value = editMilestoneViewModel.editMilestoneLocationB.value,
                            onValueChange = {
                                editMilestoneViewModel.editMilestoneLocationB.value = it
                                editMilestoneViewModel.editMilestoneLocationBEmpty.value = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFFF4F4F4),
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ), enabled = true, placeholder = {
                                Text(
                                    text = stringResource(id = R.string.enter_location),
                                    style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                )
                            }, singleLine = true,
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            maxLines = 1,
                            textStyle = MaterialTheme.typography.body2.copy(
                                color = Color.Black
                            )
                        )
                        if (editMilestoneViewModel.editMilestoneLocationBEmpty.value) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = stringResource(id = R.string.enter_location),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 5.dp),
                            onClick = {
                                when {
                                    TextUtils.isEmpty(editMilestoneViewModel.editMilestoneTittle.value) && TextUtils.isEmpty(
                                        editMilestoneViewModel.editMilestoneDate.value
                                    ) || TextUtils.isEmpty(editMilestoneViewModel.editMilestoneTime.value) && TextUtils.isEmpty(
                                        editMilestoneViewModel.editMilestoneLocationB.value
                                    ) -> {
                                        editMilestoneViewModel.editMilestoneTittleEmpty.value = true
                                        editMilestoneViewModel.editMilestoneDateEmpty.value = true
                                        editMilestoneViewModel.editMilestoneTimeEmpty.value = true
                                        editMilestoneViewModel.editMilestoneLocationBEmpty.value =
                                            true
                                    }
                                    TextUtils.isEmpty(editMilestoneViewModel.editMilestoneTittle.value) -> {
                                        editMilestoneViewModel.editMilestoneTittleEmpty.value = true
                                    }
                                    TextUtils.isEmpty(editMilestoneViewModel.editMilestoneDate.value) -> {
                                        editMilestoneViewModel.editMilestoneDateEmpty.value = true
                                    }
                                    TextUtils.isEmpty(editMilestoneViewModel.editMilestoneTime.value) -> {
                                        editMilestoneViewModel.editMilestoneTimeEmpty.value = true
                                    }
                                    TextUtils.isEmpty(editMilestoneViewModel.editMilestoneLocationB.value) -> {
                                        editMilestoneViewModel.editMilestoneLocationBEmpty.value =
                                            true
                                    }
                                    else -> {
                                        val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
                                        val userId =
                                            PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
                                        val updateMilestoneAnsInfoRequest =
                                            UpdateMilestoneAnsInfoRequest(
                                                title = editMilestoneViewModel.editMilestoneTittle.value,
                                                time = editMilestoneViewModel.editMilestoneTime.value,
                                                date = editMilestoneViewModel.editMilestoneDate.value,
                                                location = editMilestoneViewModel.editMilestoneLocationB.value,
                                                longitude = Constants.LONGITUDE,
                                                latitude = Constants.LATITUDE,
                                                user_id = userId,
                                                type = type!!,
                                                milestone_id = editMilestoneViewModel.milestoneId.value
                                            )
                                        editMilestoneViewModel.updateMilestoneAnsInfo(
                                            updateMilestoneAnsInfoRequest = updateMilestoneAnsInfoRequest
                                        )
                                        editMilestoneViewModel.updateMilestoneAnsInfoResponse.observe(
                                            homeActivity
                                        ) {
                                            if (it != null) {
                                                handleUpdateMilestoneData(
                                                    coroutineScope = coroutineScope,
                                                    editMilestoneBottomSheetScaffoldState = editMilestoneBottomSheetState,
                                                    result = it,
                                                    editMilestoneViewModel = editMilestoneViewModel,
                                                    context = context,
                                                    homeActivity
                                                )
                                            }
                                        }
                                    }
                                }
                            },
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
                                text = stringResource(id = R.string.confirm),
                                style = MaterialTheme.typography.body2.copy(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight.W900
                                )
                            )
                        }
                    }
                }
            }, sheetPeekHeight = 40.dp,
            content = {
                BackHandler(editMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                    coroutineScope.launch {
                        if(editMilestoneViewModel.isBottomSheetOpen.value){
                            editMilestoneBottomSheetState.bottomSheetState.expand()
                        }else{
                            editMilestoneBottomSheetState.bottomSheetState.collapse()
                        }
                        editMilestoneViewModel.isBottomSheetOpen.value = false
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, bottom = 50.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                if (editMilestoneViewModel.editMilestoneTitleImage.value.isEmpty()) {
                                    Image(
                                        modifier = Modifier
                                            .width(75.dp)
                                            .height(75.dp),
                                        painter = painterResource(id = R.drawable.img_user_add_new_milestone),
                                        contentDescription = ""
                                    )
                                } else {
                                    val painter = rememberImagePainter(
                                        editMilestoneViewModel.editMilestoneTitleImage.value,
                                        builder = {
                                            placeholder(R.drawable.ic_baseline_place_holder_image_24)
                                        })
                                    Image(
                                        modifier = Modifier
                                            .width(75.dp)
                                            .height(75.dp),
                                        painter = painter,
                                        contentDescription = "",
                                        contentScale = ContentScale.Inside
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            editMilestoneViewModel.editMilestoneTimeEmpty.value =
                                                false
                                            editMilestoneViewModel.editMilestoneDateEmpty.value =
                                                false
                                            editMilestoneViewModel.editMilestoneLocationBEmpty.value =
                                                false
                                            editMilestoneViewModel.isBottomSheetOpen.value = true
                                            coroutineScope.launch {
                                                if (editMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                                    editMilestoneBottomSheetState.bottomSheetState.collapse()
                                                    editMilestoneViewModel.isBottomSheetOpen.value = true
                                                } else {
                                                    editMilestoneBottomSheetState.bottomSheetState.expand()
                                                    editMilestoneViewModel.isBottomSheetOpen.value = false
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = MutableInteractionSource()
                                            ) {
                                                editMilestoneViewModel.editMilestoneTimeEmpty.value =
                                                    false
                                                editMilestoneViewModel.editMilestoneDateEmpty.value =
                                                    false
                                                editMilestoneViewModel.editMilestoneLocationBEmpty.value =
                                                    false
                                                editMilestoneViewModel.isBottomSheetOpen.value = true
                                                coroutineScope.launch {
                                                    if (editMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                                        editMilestoneBottomSheetState.bottomSheetState.collapse()
                                                        editMilestoneViewModel.isBottomSheetOpen.value = true
                                                    } else {
                                                        editMilestoneBottomSheetState.bottomSheetState.expand()
                                                        editMilestoneViewModel.isBottomSheetOpen.value = false
                                                    }
                                                }
                                            },
                                        text = editMilestoneViewModel.editMilestoneTittle.value,
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W400,
                                            lineHeight = 22.sp,
                                        )
                                    )
                                    Row(modifier = Modifier.padding(top = 32.dp, start = 16.dp)) {
                                        val dateTime =
                                            if (editMilestoneViewModel.editMilestoneDate.value == "" && editMilestoneViewModel.editMilestoneTime.value == "") "N/A" else "${editMilestoneViewModel.editMilestoneDate.value} at ${editMilestoneViewModel.editMilestoneTime.value}"
                                        Image(
                                            painter = painterResource(id = R.drawable.img_medical_calender_icon),
                                            contentDescription = ""
                                        )
                                        Text(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .padding(start = 8.dp, top = 2.dp),
                                            text = dateTime,
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFF9F9D9B),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    items(editMilestoneViewModel.imageList.size) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 26.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .zIndex(1f)
                                    .padding(top = 10.dp, end = 10.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Top
                            ) {
                                if (type == Constants.PARENT) {
                                    if (editMilestoneViewModel.milestoneType.value == Constants.PARENT) {
                                        Icon(
                                            modifier = Modifier
                                                .width(25.dp)
                                                .height(25.dp)
                                                .background(
                                                    Color(0xFFF34646),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clickable {
                                                    if (editMilestoneViewModel.imageList[index].is_need_to_upload) {
                                                        editMilestoneViewModel.imageList.removeAt(
                                                            index
                                                        )
                                                        val tempData =
                                                            editMilestoneViewModel.imageList.toList()
                                                        editMilestoneViewModel.imageList.clear()
                                                        editMilestoneViewModel.imageList.addAll(
                                                            tempData
                                                        )
                                                    } else {
                                                        editMilestoneViewModel.deleteMileStoneImage(
                                                            DeleteMilestoneImageRequest(
                                                                editMilestoneViewModel.imageList[index].id
                                                            )
                                                        )
                                                        editMilestoneViewModel.deleteMilestoneImageResponse.observe(
                                                            homeActivity
                                                        ) {
                                                            if (it != null) {
                                                                handleDeleteImageData(
                                                                    result = it,
                                                                    editMilestoneViewModel = editMilestoneViewModel,
                                                                    homeActivity = homeActivity,
                                                                    context = context,
                                                                    type = type,
                                                                    milestoneId = milestoneId,
                                                                    partner_id = partnerId,
                                                                    milestone_type = milestoneType.value
                                                                )
                                                            }
                                                        }
                                                    }
                                                },
                                            imageVector = Icons.Default.Close,
                                            tint = Color.White,
                                            contentDescription = ""
                                        )
                                    }
                                } else {
                                    Icon(
                                        modifier = Modifier
                                            .width(25.dp)
                                            .height(25.dp)
                                            .background(
                                                Color(0xFFF34646),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                if (editMilestoneViewModel.imageList[index].is_need_to_upload) {
                                                    Log.d(
                                                        "TAG",
                                                        "EditMilestoneScreen: New Image Remove"
                                                    )
                                                    editMilestoneViewModel.imageList.removeAt(
                                                        index
                                                    )
                                                    val tempData =
                                                        editMilestoneViewModel.imageList.toList()
                                                    editMilestoneViewModel.imageList.clear()
                                                    editMilestoneViewModel.imageList.addAll(
                                                        tempData
                                                    )
                                                } else {
                                                    editMilestoneViewModel.deleteMileStoneImage(
                                                        DeleteMilestoneImageRequest(
                                                            editMilestoneViewModel.imageList[index].id
                                                        )
                                                    )
                                                    editMilestoneViewModel.deleteMilestoneImageResponse.observe(
                                                        homeActivity
                                                    ) {
                                                        if (it != null) {
                                                            handleDeleteImageData(
                                                                result = it,
                                                                editMilestoneViewModel = editMilestoneViewModel,
                                                                homeActivity = homeActivity,
                                                                context = context,
                                                                type = type,
                                                                milestoneId = milestoneId,
                                                                partner_id = partnerId,
                                                                milestone_type = milestoneType.value
                                                            )
                                                        }
                                                    }
                                                }
                                            },
                                        imageVector = Icons.Default.Close,
                                        tint = Color.White,
                                        contentDescription = ""
                                    )
                                }
                            }
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                            ) {
                                drawRoundRect(
                                    color = Color(0xFFC6C4C2),
                                    style = stroke,
                                    cornerRadius = CornerRadius(18f, 18f)
                                )
                            }
                            Card(
                                modifier = Modifier.height(190.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                editMilestoneViewModel.imageList[index].is_need_to_upload.let {
                                    if (it) {
                                        if (Build.VERSION.SDK_INT < 28) {
                                            Image(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                bitmap = MediaStore.Images.Media.getBitmap(
                                                    context.contentResolver,
                                                    editMilestoneViewModel.imageList[index].imageUri
                                                ).asImageBitmap(),
                                                contentDescription = "",
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            editMilestoneViewModel.imageList[index].imageUri?.let { imageUri ->
                                                val source = ImageDecoder.createSource(
                                                    context.contentResolver,
                                                    imageUri
                                                )
                                                Image(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    bitmap = ImageDecoder.decodeBitmap(source)
                                                        .asImageBitmap(),
                                                    contentDescription = "",
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    } else {
                                        val painter = rememberImagePainter(
                                            editMilestoneViewModel.imageList[index].image,
                                            builder = {
                                                placeholder(R.drawable.ic_baseline_place_holder_image_24)
                                            })
                                        Image(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            painter = painter,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                            if (type == Constants.PARENT) {
                                if (editMilestoneViewModel.milestoneType.value == Constants.PARENT) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(175.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Button(
                                            modifier = Modifier
                                                .width(210.dp)
                                                .alpha(if (isPicAvailable.value) 1f else 0f),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Custom_Blue
                                            ),
                                            elevation = ButtonDefaults.elevation(
                                                defaultElevation = 0.dp,
                                                pressedElevation = 0.dp,
                                                disabledElevation = 0.dp,
                                                hoveredElevation = 0.dp,
                                                focusedElevation = 0.dp
                                            ),
                                            shape = RoundedCornerShape(13.dp),
                                            enabled = true,
                                            onClick = {
                                                if (ActivityCompat.checkSelfPermission(
                                                        homeActivity,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    homeActivity.callPermissionRequestLauncher(
                                                        launcher
                                                    )
                                                    editMilestoneViewModel.isPermissionAllowed.value =
                                                        false
                                                    editMilestoneViewModel.imageListIndex.value =
                                                        index
                                                    latestIndex.value = index
                                                } else {
                                                    launcher.launch(Constants.IMAGE_LAUNCHER)
                                                    editMilestoneViewModel.isPermissionAllowed.value =
                                                        false
                                                    editMilestoneViewModel.imageListIndex.value =
                                                        index
                                                    latestIndex.value = index
                                                }
                                            }) {
                                            Text(
                                                modifier = Modifier.wrapContentWidth(),
                                                text = stringResource(id = R.string.change_photo),
                                                style = MaterialTheme.typography.h1.copy(
                                                    fontWeight = FontWeight.W900,
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    lineHeight = 24.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                            Icon(
                                                modifier = Modifier.padding(start = 13.dp),
                                                painter = painterResource(id = R.drawable.ic_icon_btn_upload_picture),
                                                contentDescription = "",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(175.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Button(
                                        modifier = Modifier
                                            .width(210.dp)
                                            .alpha(if (isPicAvailable.value) 1f else 0f),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Custom_Blue
                                        ),
                                        elevation = ButtonDefaults.elevation(
                                            defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp
                                        ),
                                        shape = RoundedCornerShape(13.dp),
                                        enabled = true,
                                        onClick = {
                                            if (ActivityCompat.checkSelfPermission(
                                                    homeActivity,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                                ) != PackageManager.PERMISSION_GRANTED
                                            ) {
                                                homeActivity.callPermissionRequestLauncher(launcher)
                                                editMilestoneViewModel.isPermissionAllowed.value =
                                                    false
                                                editMilestoneViewModel.imageListIndex.value =
                                                    index
                                                latestIndex.value = index
                                            } else {
                                                launcher.launch(Constants.IMAGE_LAUNCHER)
                                                editMilestoneViewModel.isPermissionAllowed.value =
                                                    false
                                                editMilestoneViewModel.imageListIndex.value =
                                                    index
                                                latestIndex.value = index
                                            }
                                        }) {
                                        Text(
                                            modifier = Modifier.wrapContentWidth(),
                                            text = stringResource(id = R.string.change_photo),
                                            style = MaterialTheme.typography.h1.copy(
                                                fontWeight = FontWeight.W900,
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                        Icon(
                                            modifier = Modifier.padding(start = 13.dp),
                                            painter = painterResource(id = R.drawable.ic_icon_btn_upload_picture),
                                            contentDescription = "",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        if (type == Constants.PARENT) {
                            if (editMilestoneViewModel.milestoneType.value == Constants.PARENT) {
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 27.dp)
                                ) {
                                    val (border_image_bg, tv_no_img_tittle, tv_no_img_desc, btn_upload_pic) = createRefs()
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(190.dp)
                                            .constrainAs(border_image_bg) {
                                                top.linkTo(parent.top)
                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                                bottom.linkTo(parent.bottom)
                                            }
                                    ) {
                                        drawRoundRect(
                                            color = Color(0xFFC6C4C2),
                                            style = stroke,
                                            cornerRadius = CornerRadius(18f, 18f)
                                        )
                                    }
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp)
                                            .alpha(if (isPicAvailable.value) 1f else 0f)
                                            .constrainAs(tv_no_img_tittle) {
                                                start.linkTo(border_image_bg.start)
                                                end.linkTo(border_image_bg.end)
                                                top.linkTo(border_image_bg.top)
                                            },
                                        text = stringResource(id = R.string.no_image_available),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color(0xFF7F7D7C),
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 22.dp, top = 8.dp, end = 22.dp)
                                            .alpha(if (isPicAvailable.value) 1f else 0f)
                                            .constrainAs(tv_no_img_desc) {
                                                start.linkTo(border_image_bg.start)
                                                end.linkTo(border_image_bg.end)
                                                top.linkTo(tv_no_img_tittle.bottom)
                                            },
                                        text = stringResource(id = R.string.attach_photo_and_comment),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color(0xFFC6C4C2),
                                            fontSize = 12.sp,
                                            lineHeight = 24.sp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                    Button(
                                        modifier = Modifier
                                            .width(210.dp)
                                            .wrapContentHeight()
                                            .padding(top = 30.dp)
                                            .alpha(if (isPicAvailable.value) 1f else 0f)
                                            .constrainAs(btn_upload_pic) {
                                                start.linkTo(border_image_bg.start)
                                                end.linkTo(border_image_bg.end)
                                                top.linkTo(tv_no_img_desc.bottom)
                                            },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Custom_Blue
                                        ),
                                        elevation = ButtonDefaults.elevation(
                                            defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp
                                        ),
                                        shape = RoundedCornerShape(13.dp),
                                        enabled = true,
                                        onClick = {
                                            if (editMilestoneViewModel.imageList.size > 4) {
                                                Toast.makeText(
                                                    context,
                                                    Constants.MAX_5_IMAGE,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                if (ActivityCompat.checkSelfPermission(
                                                        homeActivity,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    homeActivity.callPermissionRequestLauncher(
                                                        launcher
                                                    )
                                                    editMilestoneViewModel.isPermissionAllowed.value =
                                                        false
                                                    editMilestoneViewModel.imageListIndex.value =
                                                        -1
                                                } else {
                                                    launcher.launch(Constants.IMAGE_LAUNCHER)
                                                    editMilestoneViewModel.isPermissionAllowed.value =
                                                        false
                                                    editMilestoneViewModel.imageListIndex.value =
                                                        -1
                                                }
                                            }
                                        }) {
                                        Text(
                                            modifier = Modifier.wrapContentWidth(),
                                            text = stringResource(id = R.string.upload_picture),
                                            style = MaterialTheme.typography.h1.copy(
                                                fontWeight = FontWeight.W900,
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                        Icon(
                                            modifier = Modifier.padding(start = 13.dp),
                                            painter = painterResource(id = R.drawable.ic_icon_btn_upload_picture),
                                            contentDescription = "",
                                            tint = Color.White
                                        )
                                    }
                                }
                            } else if (editMilestoneViewModel.milestoneType.value == Constants.COMMON) {
                                if (editMilestoneViewModel.imageList.isEmpty()) {
                                    ConstraintLayout(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 27.dp)
                                    ) {
                                        val (border_image_bg, tv_no_img_tittle, tv_no_img_desc) = createRefs()
                                        Canvas(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(112.dp)
                                                .constrainAs(border_image_bg) {
                                                    top.linkTo(parent.top)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                    bottom.linkTo(parent.bottom)
                                                }
                                        ) {
                                            drawRoundRect(
                                                color = Color(0xFFC6C4C2),
                                                style = stroke,
                                                cornerRadius = CornerRadius(18f, 18f)
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp)
                                                .alpha(if (isPicAvailable.value) 1f else 0f)
                                                .constrainAs(tv_no_img_tittle) {
                                                    start.linkTo(border_image_bg.start)
                                                    end.linkTo(border_image_bg.end)
                                                    top.linkTo(border_image_bg.top)
                                                },
                                            text = stringResource(id = R.string.no_image_available),
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFF7F7D7C),
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 22.dp, top = 8.dp, end = 22.dp)
                                                .alpha(if (isPicAvailable.value) 1f else 0f)
                                                .constrainAs(tv_no_img_desc) {
                                                    start.linkTo(border_image_bg.start)
                                                    end.linkTo(border_image_bg.end)
                                                    top.linkTo(tv_no_img_tittle.bottom)
                                                },
                                            text = stringResource(id = R.string.attach_photo_and_comment),
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color(0xFFC6C4C2),
                                                fontSize = 12.sp,
                                                lineHeight = 24.sp
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 27.dp)
                            ) {
                                val (border_image_bg, tv_no_img_tittle, tv_no_img_desc, btn_upload_pic) = createRefs()
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                        .constrainAs(border_image_bg) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
                                        }
                                ) {
                                    drawRoundRect(
                                        color = Color(0xFFC6C4C2),
                                        style = stroke,
                                        cornerRadius = CornerRadius(18f, 18f)
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp)
                                        .alpha(if (isPicAvailable.value) 1f else 0f)
                                        .constrainAs(tv_no_img_tittle) {
                                            start.linkTo(border_image_bg.start)
                                            end.linkTo(border_image_bg.end)
                                            top.linkTo(border_image_bg.top)
                                        },
                                    text = stringResource(id = R.string.no_image_available),
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color(0xFF7F7D7C),
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 22.dp, top = 8.dp, end = 22.dp)
                                        .alpha(if (isPicAvailable.value) 1f else 0f)
                                        .constrainAs(tv_no_img_desc) {
                                            start.linkTo(border_image_bg.start)
                                            end.linkTo(border_image_bg.end)
                                            top.linkTo(tv_no_img_tittle.bottom)
                                        },
                                    text = stringResource(id = R.string.attach_photo_and_comment),
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color(0xFFC6C4C2),
                                        fontSize = 12.sp,
                                        lineHeight = 24.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    modifier = Modifier
                                        .width(210.dp)
                                        .wrapContentHeight()
                                        .padding(top = 30.dp)
                                        .alpha(if (isPicAvailable.value) 1f else 0f)
                                        .constrainAs(btn_upload_pic) {
                                            start.linkTo(border_image_bg.start)
                                            end.linkTo(border_image_bg.end)
                                            top.linkTo(tv_no_img_desc.bottom)
                                        },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Custom_Blue
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp
                                    ),
                                    shape = RoundedCornerShape(13.dp),
                                    enabled = true,
                                    onClick = {
                                        if (editMilestoneViewModel.imageList.size > 4) {
                                            Toast.makeText(
                                                context,
                                                Constants.MAX_5_IMAGE,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            if (ActivityCompat.checkSelfPermission(
                                                    homeActivity,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                                ) != PackageManager.PERMISSION_GRANTED
                                            ) {
                                                homeActivity.callPermissionRequestLauncher(launcher)
                                                editMilestoneViewModel.isPermissionAllowed.value =
                                                    false
                                                editMilestoneViewModel.imageListIndex.value =
                                                    -1
                                            } else {
                                                launcher.launch(Constants.IMAGE_LAUNCHER)
                                                editMilestoneViewModel.isPermissionAllowed.value =
                                                    false
                                                editMilestoneViewModel.imageListIndex.value =
                                                    -1
                                            }
                                        }
                                    }) {
                                    Text(
                                        modifier = Modifier.wrapContentWidth(),
                                        text = stringResource(id = R.string.upload_picture),
                                        style = MaterialTheme.typography.h1.copy(
                                            fontWeight = FontWeight.W900,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                    Icon(
                                        modifier = Modifier.padding(start = 13.dp),
                                        painter = painterResource(id = R.drawable.ic_icon_btn_upload_picture),
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        if (editMilestoneViewModel.isPermissionAllowed.value) {
                            AlertDialog(
                                properties = DialogProperties(
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = false,
                                    usePlatformDefaultWidth = true,
                                ),
                                onDismissRequest = {
                                    editMilestoneViewModel.isPermissionAllowed.value = false
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        val uri =
                                            Uri.fromParts("package", context.packageName, null)
                                        intent.data = uri
                                        context.startActivity(intent)
                                    })
                                    { Text(text = stringResource(id = R.string.app_settings), color = Color.Red) }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        editMilestoneViewModel.isPermissionAllowed.value = false
                                    })
                                    { Text(text = stringResource(id = R.string.cancel_dialog), color = Color.Red) }
                                },
                                title = { Text(text = stringResource(id = R.string.permission_denied_dialog)) },
                                text = { Text(text = stringResource(id = R.string.allow_permission_dialog)) }
                            )
                        }
                        if (type == Constants.PARENT && editMilestoneViewModel.surrogateNote.value != "") {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 26.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.surrogate_note),
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.W800,
                                        fontSize = 12.sp
                                    )
                                )
                                TextField(
                                    value = editMilestoneViewModel.surrogateNote.value,
                                    onValueChange = {
                                        editMilestoneViewModel.surrogateNote.value = it
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        focusManager.clearFocus()
                                    }),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(top = 12.dp),
                                    textStyle = MaterialTheme.typography.body2,
                                    placeholder = {
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = ET_Bg,
                                        cursorColor = Custom_Blue,
                                        focusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ), readOnly = true
                                )
                            }
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 26.dp),
                            text = stringResource(id = R.string.notes), style = MaterialTheme.typography.body2.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.W800,
                                fontSize = 12.sp
                            )
                        )
                        TextField(
                            value = editMilestoneViewModel.addNewMilestoneNotes.value,
                            onValueChange = {
                                editMilestoneViewModel.addNewMilestoneNotes.value = it
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(142.dp)
                                .padding(top = 12.dp),
                            textStyle = MaterialTheme.typography.body2,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.write_notes_here),
                                    style = MaterialTheme.typography.body1.copy(
                                        fontSize = 16.sp,
                                        color = Text_Color,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.W400
                                    )
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = ET_Bg,
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                        )
                        if (type == Constants.PARENT) {
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                    Checkbox(modifier = Modifier.padding(
                                        top = 18.dp, bottom = 10.dp, end = 5.dp
                                    ),
                                        checked = editMilestoneViewModel.checkBoxShareWithBiggestAsk,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = CheckBox_Check,
                                            uncheckedColor = Color.DarkGray
                                        ),
                                        onCheckedChange = {
                                            editMilestoneViewModel.checkBoxShareWithBiggestAsk = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp, start = 10.dp),
                                        text = stringResource(id = R.string.share_with_the_biggest_ask),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.W600,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                        } else {
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                    Checkbox(modifier = Modifier.padding(
                                        top = 18.dp, bottom = 10.dp, end = 5.dp
                                    ),
                                        checked = editMilestoneViewModel.checkBoxShareWithParents,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = CheckBox_Check,
                                            uncheckedColor = Color.DarkGray
                                        ),
                                        onCheckedChange = {
                                            editMilestoneViewModel.checkBoxShareWithParents = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp, start = 10.dp),
                                        text = stringResource(id = R.string.share_with_intended_parents),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.W600,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                            Row(modifier = Modifier) {
                                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                    Checkbox(modifier = Modifier.padding(
                                        top = 18.dp, bottom = 10.dp, end = 5.dp
                                    ),
                                        checked = editMilestoneViewModel.checkBoxShareWithBiggestAsk,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = CheckBox_Check,
                                            uncheckedColor = Color.DarkGray
                                        ),
                                        onCheckedChange = {
                                            editMilestoneViewModel.checkBoxShareWithBiggestAsk = it
                                        })
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp, start = 10.dp),
                                        text = stringResource(id = R.string.share_with_the_biggest_ask),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.W600,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(
                                        top = 30.dp,
                                        bottom = if (type == Constants.PARENT && editMilestoneViewModel.milestoneType.value == Constants.PARENT) 0.dp else 20.dp
                                    ),
                                onClick = {
                                    if (!TextUtils.isEmpty(editMilestoneViewModel.addNewMilestoneNotes.value)) {
                                        editMilestoneViewModel.saveNote(
                                            SaveNoteRequest(
                                                milestone_id = milestoneId,
                                                note = editMilestoneViewModel.addNewMilestoneNotes.value,
                                                type = type!!,
                                                share_note_with_partner = editMilestoneViewModel.checkBoxShareWithParents,
                                                share_note_with_biggestask = editMilestoneViewModel.checkBoxShareWithBiggestAsk
                                            )
                                        )
                                        editMilestoneViewModel.saveNoteResponse.observe(
                                            homeActivity
                                        ) {
                                            if (it != null) {
                                                handleSaveNoteData(
                                                    result = it,
                                                    editMilestoneViewModel = editMilestoneViewModel,
                                                    context = context
                                                )
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            Constants.ENTER_NOTE,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Custom_Blue
                                ), elevation = ButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp,
                                    disabledElevation = 0.dp,
                                    hoveredElevation = 0.dp,
                                    focusedElevation = 0.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.save_notes),
                                    style = MaterialTheme.typography.body1.copy(
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.W600
                                    )
                                )
                            }
                        }
                        if (type == Constants.PARENT) {
                            if (editMilestoneViewModel.milestoneType.value == Constants.PARENT) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp, bottom = 30.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        modifier = Modifier.width(218.dp),
                                        onClick = {
                                            val userId =
                                                PreferenceProvider(context).getIntValue(
                                                    Constants.USER_ID,
                                                    0
                                                )
                                            val imageList = ArrayList<MultipartBody.Part?>()
                                            editMilestoneViewModel.imageList.forEach {
                                                if (it.is_need_to_upload) {
                                                    imageList.add(it.uriPath?.let { uriPath ->
                                                        convertImageMultiPart(
                                                            uriPath
                                                        )
                                                    })
                                                }
                                            }
                                            if (imageList.isEmpty()) {
                                                val toast =
                                                    Toast.makeText(
                                                        context,
                                                        Constants.SELECT_IMAGE_FIRST,
                                                        Toast.LENGTH_LONG
                                                    )
                                                toast.setGravity(Gravity.CENTER, 0, 0)
                                                toast.show()
                                            } else {
                                                editMilestoneViewModel.storeMilestoneAns(
                                                    images = imageList,
                                                    user_id = MultipartBody.Part.createFormData(
                                                        Constants.USER_ID,
                                                        userId.toString()
                                                    ),
                                                    type = MultipartBody.Part.createFormData(
                                                        Constants.TYPE,
                                                        type
                                                    ),
                                                    milestone_id = MultipartBody.Part.createFormData(
                                                        Constants.MILESTONE_ID,
                                                        milestoneId.toString()
                                                    )
                                                )
                                                editMilestoneViewModel.updateMilestoneResponse.observe(
                                                    homeActivity
                                                ) {
                                                    if (it != null) {
                                                        handleStoreMilestoneData(
                                                            result = it,
                                                            editMilestoneViewModel = editMilestoneViewModel,
                                                            navHostController = navHostController,
                                                            milestoneId
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Custom_Blue
                                        ), shape = RoundedCornerShape(12.dp),
                                        elevation = ButtonDefaults.elevation(
                                            defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.update_milestone),
                                            style = MaterialTheme.typography.body2.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.W900,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Icon(
                                            modifier = Modifier.padding(start = 8.dp),
                                            painter = painterResource(id = R.drawable.ic_img_btn_add_milestone),
                                            contentDescription = "",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp, bottom = 30.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    modifier = Modifier.width(218.dp),
                                    onClick = {
                                        val userId =
                                            PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
                                        val imageList = ArrayList<MultipartBody.Part?>()
                                        editMilestoneViewModel.imageList.forEach {
                                            if (it.is_need_to_upload) {
                                                imageList.add(it.uriPath?.let { uriPath ->
                                                    convertImageMultiPart(
                                                        uriPath
                                                    )
                                                })
                                            }
                                        }
                                        if (imageList.isEmpty()) {
                                            val toast =
                                                Toast.makeText(
                                                    context,
                                                    Constants.SELECT_IMAGE_FIRST,
                                                    Toast.LENGTH_LONG
                                                )
                                            toast.setGravity(Gravity.CENTER, 0, 0)
                                            toast.show()
                                        } else {
                                            editMilestoneViewModel.storeMilestoneAns(
                                                images = imageList,
                                                user_id = MultipartBody.Part.createFormData(
                                                    Constants.USER_ID,
                                                    userId.toString()
                                                ),
                                                type = MultipartBody.Part.createFormData(
                                                    Constants.TYPE,
                                                    type!!
                                                ),
                                                milestone_id = MultipartBody.Part.createFormData(
                                                    Constants.MILESTONE_ID,
                                                    milestoneId.toString()
                                                )
                                            )
                                            editMilestoneViewModel.updateMilestoneResponse.observe(
                                                homeActivity
                                            ) {
                                                if (it != null) {
                                                    handleStoreMilestoneData(
                                                        result = it,
                                                        editMilestoneViewModel = editMilestoneViewModel,
                                                        navHostController = navHostController,
                                                        milestoneId
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Custom_Blue
                                    ), shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp
                                    )
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.update_milestone),
                                        style = MaterialTheme.typography.body2.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.W900,
                                            fontSize = 16.sp
                                        )
                                    )
                                    Icon(
                                        modifier = Modifier.padding(start = 8.dp),
                                        painter = painterResource(id = R.drawable.ic_img_btn_add_milestone),
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        )
    }
    if (editMilestoneViewModel.isEditMilestoneDataLoaded.value) {
        ProgressBarTransparentBackground(loadingText = stringResource(id = R.string.loading))
    }
    if (editMilestoneViewModel.isMilestoneDataUpdated.value || editMilestoneViewModel.isNoteSaved.value) {
        ProgressBarTransparentBackground(loadingText = if (editMilestoneViewModel.isMilestoneDataUpdated.value) stringResource(id = R.string.updating) else stringResource(id = R.string.saving))
    }
    if (editMilestoneViewModel.isMilestoneAnsUpdated.value || editMilestoneViewModel.isMilestoneImageUpdated.value) {
        ProgressBarTransparentBackground(loadingText = stringResource(id = R.string.updating))
    }
    if (editMilestoneViewModel.isImageDeleted.value) {
        ProgressBarTransparentBackground(loadingText = stringResource(id = R.string.removing))
    }
}

private fun getUpdatedMilestone(
    homeActivity: HomeActivity,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context,
    type: String?,
    milestoneId: Int,
    partner_id: Int,
) {
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    editMilestoneViewModel.getMilestoneDetails(
        EditMilestoneRequest(
            type = type!!,
            user_id = userId,
            milestone_id = milestoneId,
            partner_id = partner_id
        )
    )
    editMilestoneViewModel.editMilestoneResponse.observe(homeActivity) {
        if (it != null) {
            handleEditMilestoneData(
                result = it,
                context = context,
                editMilestoneViewModel = editMilestoneViewModel
            )
        }
    }
}

private fun handleDeleteImageData(
    result: NetworkResult<CommonResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    homeActivity: HomeActivity,
    context: Context,
    type: String?,
    milestoneId: Int,
    partner_id: Int,
    milestone_type: String
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            editMilestoneViewModel.isImageDeleted.value = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            editMilestoneViewModel.isImageDeleted.value = false
            getUpdatedMilestone(
                homeActivity = homeActivity,
                editMilestoneViewModel = editMilestoneViewModel,
                context = context,
                type = type,
                milestoneId = milestoneId,
                partner_id = partner_id,
            )
        }
        is NetworkResult.Error -> {
            // show error message
            editMilestoneViewModel.isImageDeleted.value = false
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

private fun handleUpdateImageData(
    result: NetworkResult<UpdateImageResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context,
    selectedImageIndex: Int
) {
    when (result) {
        is NetworkResult.Loading -> {
            editMilestoneViewModel.isMilestoneImageUpdated.value = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            editMilestoneViewModel.isMilestoneImageUpdated.value = false
            if (!result.data?.image.isNullOrEmpty()) {
                editMilestoneViewModel.imageList[selectedImageIndex].image =
                    result.data?.image.toString()
            }
            val tempData =
                editMilestoneViewModel.imageList.toList()
            editMilestoneViewModel.imageList.clear()
            editMilestoneViewModel.imageList.addAll(tempData)
        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            editMilestoneViewModel.isMilestoneImageUpdated.value = false
        }
    }
}


private fun handleStoreMilestoneData(
    result: NetworkResult<UpdateUserProfileResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    navHostController: NavHostController,
    milestoneId: Int
) {
    when (result) {
        is NetworkResult.Loading -> {
            editMilestoneViewModel.isMilestoneAnsUpdated.value = true
        }
        is NetworkResult.Success -> {
            Log.i("TAG", result.message.toString())
            editMilestoneViewModel.isMilestoneAnsUpdated.value = false
            navHostController.popBackStack(
                BottomNavScreen.AddNewMileStones.route,
                true
            )
            navHostController.navigate(
                BottomNavScreen.AddNewMileStones.editMilestone(
                    milestoneId
                )
            )
        }
        is NetworkResult.Error -> {
            // show error message
            editMilestoneViewModel.isMilestoneAnsUpdated.value = false
        }
    }
}

private fun handleEditMilestoneData(
    result: NetworkResult<EditMilestoneResponse>,
    context: Context,
    editMilestoneViewModel: EditMilestoneViewModel
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            editMilestoneViewModel.isEditMilestoneDataLoaded.value = true
        }
        is NetworkResult.Success -> {
            val type = PreferenceProvider(context).getValue("type", "")
            editMilestoneViewModel.editMilestoneTittle.value =
                result.data?.milestone?.get(0)?.title!!
            if (result.data.milestone[0].date == null) {
                editMilestoneViewModel.editMilestoneDate.value = ""
            } else {
                editMilestoneViewModel.editMilestoneDate.value = result.data.milestone[0].date
            }
            if (result.data.milestone[0].time == null) {
                editMilestoneViewModel.editMilestoneTime.value = ""
            } else {
                editMilestoneViewModel.editMilestoneTime.value = result.data.milestone[0].time
            }
            if (!result.data.milestone[0].milestone_image.isNullOrEmpty()) {
                editMilestoneViewModel.editMilestoneTitleImage.value =
                    result.data.milestone[0].milestone_image
            } else {
                editMilestoneViewModel.editMilestoneTitleImage.value = ""
            }
            if (type == Constants.PARENT) {
                if (result.data.milestone[0].share_note_with_partner_status != 0) {
                    editMilestoneViewModel.surrogateNote.value =
                        result.data.milestone[0].surrogate_note
                } else {
                    editMilestoneViewModel.surrogateNote.value = ""
                }
                editMilestoneViewModel.checkBoxShareWithBiggestAsk =
                    result.data.milestone[0].share_note_with_biggestask_status != 0
                if (result.data.milestone[0].parent_note == null) {
                    editMilestoneViewModel.addNewMilestoneNotes.value = ""
                } else {
                    editMilestoneViewModel.addNewMilestoneNotes.value =
                        result.data.milestone[0].parent_note
                }
            } else if (type == Constants.SURROGATE) {
                editMilestoneViewModel.checkBoxShareWithParents =
                    result.data.milestone[0].share_note_with_partner_status != 0
                if (result.data.milestone[0].surrogate_note == null) {
                    editMilestoneViewModel.addNewMilestoneNotes.value = ""
                } else {
                    editMilestoneViewModel.addNewMilestoneNotes.value =
                        result.data.milestone[0].surrogate_note
                }
            }
            if (result.data.milestone[0].location == null) {
                editMilestoneViewModel.editMilestoneLocationB.value = ""
            } else {
                editMilestoneViewModel.editMilestoneLocationB.value =
                    result.data.milestone[0].location
            }
            editMilestoneViewModel.isMilestoneTittleEditable.value =
                result.data.milestone[0].type == "common"
            editMilestoneViewModel.milestoneType.value = result.data.milestone[0].type
            editMilestoneViewModel.milestoneId.value = result.data.milestone[0].milestone_id
            editMilestoneViewModel.imageList.clear()
            editMilestoneViewModel.imageList.addAll(result.data.milestone_image)
            editMilestoneViewModel.isEditMilestoneDataLoaded.value = false
        }
        is NetworkResult.Error -> {
            // show error message
            editMilestoneViewModel.isEditMilestoneDataLoaded.value = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun handleUpdateMilestoneData(
    coroutineScope: CoroutineScope,
    editMilestoneBottomSheetScaffoldState: BottomSheetScaffoldState,
    result: NetworkResult<CommonResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context,
    homeActivity: HomeActivity
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            editMilestoneViewModel.isMilestoneDataUpdated.value = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            editMilestoneViewModel.isMilestoneDataUpdated.value = false
            Log.e("TAG", "handleUserData() --> Success  $result")
            Toast.makeText(context, result.data?.message, Toast.LENGTH_SHORT).show()
            coroutineScope.launch {
                editMilestoneBottomSheetScaffoldState.bottomSheetState.collapse()
            }
            val provider = PreferenceProvider(context)
            val type = provider.getValue("type", "")
            val userId = provider.getIntValue("user_id", 0)
            if (userId != 0 && !type.isNullOrEmpty()) {
                editMilestoneViewModel.getUpdatedStatus(userId, type)
                editMilestoneViewModel.getUpdatedStatusResponse.observe(homeActivity) {
                    if (it != null) {
                        handleUpdatedStatusData(it, context)
                    }
                }
            }
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            editMilestoneViewModel.isMilestoneDataUpdated.value = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun convertImageMultiPart(imagePath: String): MultipartBody.Part {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        Constants.IMAGE_ARRAY,
        file.name,
        file.asRequestBody(Constants.IMAGE_LAUNCHER.toMediaTypeOrNull())
    )
}

fun convertImageMultiPartSingle(imagePath: String): MultipartBody.Part {
    val file = File(imagePath)
    return MultipartBody.Part.createFormData(
        Constants.IMAGE,
        file.name,
        file.asRequestBody(Constants.IMAGE_LAUNCHER.toMediaTypeOrNull())
    )
}

private fun handleSaveNoteData(
    result: NetworkResult<CommonResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            editMilestoneViewModel.isNoteSaved.value = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            editMilestoneViewModel.isNoteSaved.value = false
            Log.e("TAG", "handleUserData() --> Success  $result")
            Toast.makeText(context, result.data?.message, Toast.LENGTH_SHORT).show()

        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            editMilestoneViewModel.isNoteSaved.value = false
        }
    }
}

private fun handleUpdatedStatusData(
    result: NetworkResult<UpdatedStatusResponse>,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {

        }
        is NetworkResult.Success -> {
            // bind data to the view
            val provider = PreferenceProvider(context)
            result.data?.let {
                provider.setValue(
                    Constants.LOGIN_STATUS,
                    it.status
                )
            }
        }
        is NetworkResult.Error -> {

        }
    }
}


@file:Suppress("NAME_SHADOWING", "DEPRECATION")

package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.DeleteMilestoneImageRequest
import com.biggestAsk.data.model.request.EditMilestoneRequest
import com.biggestAsk.data.model.request.SaveNoteRequest
import com.biggestAsk.data.model.request.UpdateMilestoneAnsInfoRequest
import com.biggestAsk.data.model.response.EditMilestoneImageResponse
import com.biggestAsk.data.model.response.EditMilestoneResponse
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.UpdateUserProfileResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavItems
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.main.viewmodel.EditMilestoneViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.CheckBox_Check
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditMilestoneScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel,
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
    var uriPath: String?
    var latestUpdatedUriPath: String? = null
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            Log.i("TAG", "The URI is $uri")
            uriPath = uri.let { it1 -> PathUtil.getPath(context, it1) }
            if (editMilestoneViewModel.imageListIndex.value != -1) {
                editMilestoneViewModel.imageList[editMilestoneViewModel.imageListIndex.value].uriPath =
                    uriPath
                editMilestoneViewModel.imageList[editMilestoneViewModel.imageListIndex.value].imageUri =
                    uri
                editMilestoneViewModel.imageList[editMilestoneViewModel.imageListIndex.value].is_need_to_upload =
                    true
            } else {
                editMilestoneViewModel.imageList.add(
                    EditMilestoneImageResponse("", 0, "", 0, "", "", 0, uriPath, uri, true)
                )
            }
            editMilestoneViewModel.imageListIndex.value = -1
            latestUpdatedUriPath = uriPath
            Log.i("TAG", editMilestoneViewModel.imageList.size.toString())
            Log.i("TAG", "The URI Path is ${uriPath.toString()}")
        }
    }
    val isPicAvailable = remember {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()
    val type = PreferenceProvider(context).getValue("type", "")
    LaunchedEffect(Unit) {
        editMilestoneViewModel.editMilestoneLocationB.value = ""
        Log.d("TAG", "EditMilestoneScreen: $milestoneId")
        getUpdatedMilestone(
            homeActivity = homeActivity,
            editMilestoneViewModel = editMilestoneViewModel,
            context = context, type = type,
            milestoneId = milestoneId,
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
                            text = "Update information", style = MaterialTheme.typography.h2.copy(
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
                            text = "Title",
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
                                    text = "Enter Tittle",
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
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = "Enter tittle",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = "Date",
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
                                                "$year/$month/$day"
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
                                editMilestoneViewModel.editMilestoneDate.value
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
                                    text = "Select date",
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
                                text = "Enter date",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = "Start Time",
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
                                                "$hourOfDay:$minute $amPm"
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
                                    text = "Select time",
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
                                text = "Enter time",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 24.dp, top = 15.dp),
                            text = "Location",
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
                                    text = "Enter Location",
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
                                text = "Enter location",
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
                                        val type = PreferenceProvider(context).getValue("type", "")
                                        val userId =
                                            PreferenceProvider(context).getIntValue("user_id", 0)
                                        val updateMilestoneAnsInfoRequest =
                                            UpdateMilestoneAnsInfoRequest(
                                                title = editMilestoneViewModel.editMilestoneTittle.value,
                                                time = editMilestoneViewModel.editMilestoneTime.value,
                                                date = editMilestoneViewModel.editMilestoneDate.value,
                                                location = editMilestoneViewModel.editMilestoneLocationB.value,
                                                longitude = "00000000002",
                                                latitude = "00005000505",
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
                                                    context = context
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
                                text = "Confirm",
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
                                Image(
                                    modifier = Modifier
                                        .width(75.dp)
                                        .height(75.dp),
                                    painter = painterResource(id = R.drawable.img_user_add_new_milestone),
                                    contentDescription = ""
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            indication = null,
                                            interactionSource = MutableInteractionSource()
                                        ) {
                                            coroutineScope.launch {
                                                if (editMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                                    editMilestoneBottomSheetState.bottomSheetState.collapse()
                                                } else {
                                                    editMilestoneBottomSheetState.bottomSheetState.expand()
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
                                                coroutineScope.launch {
                                                    if (editMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                                        editMilestoneBottomSheetState.bottomSheetState.collapse()
                                                    } else {
                                                        editMilestoneBottomSheetState.bottomSheetState.expand()
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
                        Log.i(
                            "TAG",
                            "List Size From Inner ${editMilestoneViewModel.imageList.size}"
                        )

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
                                Icon(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp)
                                        .background(Color(0xFFF34646), RoundedCornerShape(12.dp))
                                        .clickable {
                                            editMilestoneViewModel.deleteMileStoneImage(
                                                DeleteMilestoneImageRequest(editMilestoneViewModel.imageList[index].id)
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
                                                        navHostController = navHostController
                                                    )
                                                }
                                            }
                                        },
                                    imageVector = Icons.Default.Close,
                                    tint = Color.White,
                                    contentDescription = ""
                                )
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
                                        com.skydoves.landscapist.glide.GlideImage(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentDescription = "",
                                            imageModel = editMilestoneViewModel.imageList[index].image,
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
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
                                        editMilestoneViewModel.imageListIndex.value = index
                                        Log.i("TAG", "Index is $index")
                                        Log.i(
                                            "TAG",
                                            "Image Id is ${editMilestoneViewModel.imageList[index].id}"
                                        )
                                        launcher.launch("image/*")
                                        if (latestUpdatedUriPath != null) {
                                            editMilestoneViewModel.updateMilestoneImage(
                                                MultipartBody.Part.createFormData(
                                                    "image_id",
                                                    editMilestoneViewModel.imageList[index].id.toString()
                                                ),
                                                MultipartBody.Part.createFormData(
                                                    "image",
                                                    editMilestoneViewModel.imageList[index].uriPath.toString()
                                                )
                                            )
                                            editMilestoneViewModel.updateMilestoneImage.observe(
                                                homeActivity
                                            ) {
                                                if (it != null) {
                                                    handleUpdateImageData(
                                                        result = it,
                                                        editMilestoneViewModel = editMilestoneViewModel,
                                                        context = context
                                                    )
                                                }
                                            }
                                        }

                                    }) {
                                    Text(
                                        modifier = Modifier.wrapContentWidth(),
                                        text = "Change photo",
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
                    item {
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
                                text = "No ultrasound image available",
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
                                text = "For confirmation, you must attach a picture of the ultrasound and leave a comment.",
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
                                            "You can select maximum 5 images.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        editMilestoneViewModel.imageListIndex.value = -1
                                        launcher.launch("image/*")
                                    }
                                }) {
                                Text(
                                    modifier = Modifier.wrapContentWidth(),
                                    text = "Upload picture",
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
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 26.dp),
                            text = "Notes", style = MaterialTheme.typography.body2.copy(
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
                                    text = "Write notes here",
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(top = 30.dp),
                                onClick = {
                                    if (!TextUtils.isEmpty(editMilestoneViewModel.addNewMilestoneNotes.value)) {
                                        editMilestoneViewModel.saveNote(
                                            SaveNoteRequest(
                                                milestone_id = milestoneId,
                                                note = editMilestoneViewModel.addNewMilestoneNotes.value,
                                                type = type!!
                                            )
                                        )
                                        editMilestoneViewModel.saveNoteResponse.observe(homeActivity) {
                                            if (it != null) {
                                                handleSaveNoteData(
                                                    result = it,
                                                    editMilestoneViewModel = editMilestoneViewModel,
                                                    context = context
                                                )
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Enter note", Toast.LENGTH_SHORT)
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
                                    text = "Save notes",
                                    style = MaterialTheme.typography.body1.copy(
                                        fontSize = 16.sp,
                                        color = Color.White,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.W600
                                    )
                                )
                            }
                        }
                        Row {
                            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                Checkbox(modifier = Modifier.padding(
                                    top = 18.dp, bottom = 10.dp, end = 5.dp
                                ),
                                    checked = viewModel.checkBoxShareWithParents,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CheckBox_Check,
                                        uncheckedColor = Color.DarkGray
                                    ),
                                    onCheckedChange = {
                                        viewModel.checkBoxShareWithParents = it
                                    })
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, start = 10.dp),
                                    text = if (type == "parent") "Share with parents" else "Share with Surrogate Mother",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.W600,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                        Row {
                            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                Checkbox(modifier = Modifier.padding(
                                    top = 18.dp, bottom = 10.dp, end = 5.dp
                                ),
                                    checked = viewModel.checkBoxShareWithBiggestAsk,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CheckBox_Check,
                                        uncheckedColor = Color.DarkGray
                                    ),
                                    onCheckedChange = {
                                        viewModel.checkBoxShareWithBiggestAsk = it
                                    })
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, start = 10.dp),
                                    text = "Share with the biggest ask",
                                    style = MaterialTheme.typography.body2.copy(
                                        color = Color.Black,
                                        fontWeight = FontWeight.W600,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
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
                                        PreferenceProvider(context).getIntValue("user_id", 0)
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
                                    editMilestoneViewModel.storeMilestoneAns(
                                        note = MultipartBody.Part.createFormData(
                                            "note",
                                            editMilestoneViewModel.addNewMilestoneNotes.value
                                        ),
                                        images = imageList,
                                        user_id = MultipartBody.Part.createFormData(
                                            "user_id",
                                            userId.toString()
                                        ),
                                        type = MultipartBody.Part.createFormData("type", type!!),
                                        milestone_id = MultipartBody.Part.createFormData(
                                            "milestone_id",
                                            milestoneId.toString()
                                        ),
                                        note_status = MultipartBody.Part.createFormData(
                                            "share_note_with_partner",
                                            true.toString()
                                        ),
                                        note_biggest = MultipartBody.Part.createFormData(
                                            "share_note_with_biggestask",
                                            true.toString()
                                        )
                                    )
                                    editMilestoneViewModel.updateMilestoneResponse.observe(
                                        homeActivity
                                    ) {
                                        if (it != null) {
                                            handleStoreMilestoneData(
                                                result = it,
                                                editMilestoneViewModel = editMilestoneViewModel,
                                                navHostController = navHostController
                                            )
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
                                    text = "Update milestone",
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
            },
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        )
    }
    if (editMilestoneViewModel.isEditMilestoneDataLoaded.value) {
        ProgressBarTransparentBackground(loadingText = "Loading...")
    }
    if (editMilestoneViewModel.isMilestoneDataUpdated.value || editMilestoneViewModel.isNoteSaved.value) {
        ProgressBarTransparentBackground(loadingText = if (editMilestoneViewModel.isMilestoneDataUpdated.value) "Updating..." else "Saving")
    }
    if (editMilestoneViewModel.isMilestoneAnsUpdated.value) {
        ProgressBarTransparentBackground(loadingText = "Updating...")
    }
    if (editMilestoneViewModel.isImageDeleted.value) {
        ProgressBarTransparentBackground(loadingText = "Removing...")
    }
}

private fun getUpdatedMilestone(
    homeActivity: HomeActivity,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context,
    type: String?,
    milestoneId: Int,
) {
    val userId = PreferenceProvider(context).getIntValue("user_id", 0)
    editMilestoneViewModel.getMilestoneDetails(
        EditMilestoneRequest(
            type = type!!,
            user_id = userId,
            milestone_id = milestoneId
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
    navHostController: NavHostController
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
    result: NetworkResult<CommonResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            editMilestoneViewModel.isMilestoneImageUpdated.value = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            editMilestoneViewModel.isMilestoneImageUpdated.value = false
        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            editMilestoneViewModel.isMilestoneImageUpdated.value = false
        }
    }
}


private fun handleStoreMilestoneData(
    result: NetworkResult<UpdateUserProfileResponse>,
    editMilestoneViewModel: EditMilestoneViewModel,
    navHostController: NavHostController
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            editMilestoneViewModel.isMilestoneAnsUpdated.value = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            editMilestoneViewModel.isMilestoneAnsUpdated.value = false
            navHostController.popBackStack(
                BottomNavScreen.AddNewMileStones.route,
                true
            )
            navHostController.navigate(BottomNavItems.Milestones.navRoute)
            navHostController.popBackStack(
                BottomNavScreen.MileStones.route,
                true
            )
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
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
            Log.e("TAG", "handleUserData() --> Loading  $result")
            editMilestoneViewModel.isEditMilestoneDataLoaded.value = true
        }
        is NetworkResult.Success -> {
            val type = PreferenceProvider(context).getValue("type", "")
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            editMilestoneViewModel.editMilestoneTittle.value =
                result.data?.milestone?.get(0)?.title!!
            if (result.data.milestone[0].date == null) {
                editMilestoneViewModel.editMilestoneDate.value = ""
            } else {
                editMilestoneViewModel.editMilestoneDate.value = result.data.milestone[0].date!!
            }
            if (result.data.milestone[0].time == null) {
                editMilestoneViewModel.editMilestoneTime.value = ""
            } else {
                editMilestoneViewModel.editMilestoneTime.value = result.data.milestone[0].time!!
            }
            if (type == "parent") {
                if (result.data.milestone[0].parent_note == null) {
                    editMilestoneViewModel.addNewMilestoneNotes.value = ""
                } else {
                    editMilestoneViewModel.addNewMilestoneNotes.value =
                        result.data.milestone[0].surrogate_note.toString()
                }
            } else if (type == "surrogate") {
                if (result.data.milestone[0].surrogate_note == null) {
                    editMilestoneViewModel.addNewMilestoneNotes.value = ""
                } else {
                    editMilestoneViewModel.addNewMilestoneNotes.value =
                        result.data.milestone[0].parent_note.toString()
                }
            }
            if (result.data.milestone[0].location == null) {
                editMilestoneViewModel.editMilestoneLocationB.value = ""
            } else {
                editMilestoneViewModel.editMilestoneLocationB.value =
                    result.data.milestone[0].location!!
            }
            editMilestoneViewModel.isMilestoneTittleEditable.value =
                result.data.milestone[0].type == "common"
            editMilestoneViewModel.milestoneId.value = result.data.milestone[0].milestone_id!!
            editMilestoneViewModel.imageList = result.data.milestone_image.toMutableList()
            editMilestoneViewModel.isEditMilestoneDataLoaded.value = false
            Log.d("TAG", "handleEditMilestoneData: ${result.data.milestone_image.size}")
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
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
    context: Context
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
        "image[]",
        file.name,
        file.asRequestBody("image/*".toMediaTypeOrNull())
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


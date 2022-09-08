package com.biggestAsk.ui.homeScreen.bottomNavScreen

//import com.biggestAsk.ui.homeScreen.ClearRippleTheme
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.TextUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.CreateMilestoneRequest
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetMilestoneResponse
import com.biggestAsk.data.model.response.ResetMilestoneResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.ClearRippleTheme
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.MilestoneScreenShimmerAnimation
import com.biggestAsk.ui.main.viewmodel.BottomMilestoneViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MilestonesScreen(
    navHostController: NavHostController,
    milestoneViewModel: BottomMilestoneViewModel,
    homeActivity: HomeActivity,
    scaffoldState: ScaffoldState,
) {
    val addNewMilestoneBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val openDialogReset = remember { mutableStateOf(false) }
    val back = remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        milestoneViewModel.isSelected = false
        getMilestones(
            milestoneViewModel = milestoneViewModel,
            context = context,
            homeActivity = homeActivity
        )
    }
    if (scaffoldState.drawerState.isOpen) {
        BackHandler(scaffoldState.drawerState.isOpen) {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        }
    } else {
        BackHandler(back.value) {
            if (milestoneViewModel.isSelected) {
                milestoneViewModel.milestoneList.forEachIndexed { index, _ ->
                    milestoneViewModel.milestoneList[index].show = false
                }
                val milestoneListNew =
                    milestoneViewModel.milestoneList.toList()
                milestoneViewModel.milestoneList.clear()
                milestoneViewModel.milestoneList.addAll(milestoneListNew)
                milestoneViewModel.isSelected = false
            } else {
                navHostController.popBackStack(BottomNavScreen.MileStones.route, true)
            }
            back.value = false
        }
    }

    if (milestoneViewModel.isAnyErrorOccurred) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp),
                text = stringResource(id = R.string.no_data_found),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
        }
    } else {
        BottomSheetScaffold(
            scaffoldState = addNewMilestoneBottomSheetState,
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
                                .align(CenterHorizontally),
                            painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                            contentDescription = stringResource(id = R.string.content_description),
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(top = 11.dp)
                                .align(CenterHorizontally),
                            text = stringResource(id = R.string.add_milestone),
                            style = MaterialTheme.typography.h2.copy(
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
                                .align(CenterHorizontally),
                            text = stringResource(id = R.string.add_new_milestone_bottom_sheet_tittle),
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
                            text = stringResource(id = R.string.title),
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
                            value = milestoneViewModel.addNewMilestoneTittle.value,
                            onValueChange = {
                                milestoneViewModel.addNewMilestoneTittle.value = it
                                milestoneViewModel.addNewMilestoneTittleEmpty.value = false
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
                                    text = stringResource(id = R.string.enter_title),
                                    style = MaterialTheme.typography.body2.copy(Color(0xFF7F7D7C))
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFFF4F4F4),
                                cursorColor = Custom_Blue,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        if (milestoneViewModel.addNewMilestoneTittleEmpty.value) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp),
                                text = stringResource(id = R.string.enter_title),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error,
                                fontSize = 12.sp
                            )
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
                                            milestoneViewModel.addNewMilestoneDate.value =
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
                                    milestoneViewModel.addNewMilestoneDateEmpty.value = false
                                },
                            value = milestoneViewModel.addNewMilestoneDate.value,
                            onValueChange = {
                                milestoneViewModel.addNewMilestoneDate.value
                                milestoneViewModel.addNewMilestoneDateEmpty.value = false
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
                        if (milestoneViewModel.addNewMilestoneDateEmpty.value) {
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
                                            milestoneViewModel.addNewMilestoneTime.value =
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
                                    milestoneViewModel.addNewMilestoneTimeEmpty.value = false
                                }, readOnly = true, enabled = false,
                            value = milestoneViewModel.addNewMilestoneTime.value,
                            onValueChange = {
                                milestoneViewModel.addNewMilestoneTime.value
                                milestoneViewModel.addNewMilestoneTimeEmpty.value = false
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
                        if (milestoneViewModel.addNewMilestoneTimeEmpty.value) {
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
                            value = milestoneViewModel.addNewMilestoneLocationB.value,
                            onValueChange = {
                                milestoneViewModel.addNewMilestoneLocationB.value = it
                                milestoneViewModel.addNewMilestoneLocationBEmpty.value = false
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
                        if (milestoneViewModel.addNewMilestoneLocationBEmpty.value) {
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
                                    TextUtils.isEmpty(milestoneViewModel.addNewMilestoneTittle.value) && TextUtils.isEmpty(
                                        milestoneViewModel.addNewMilestoneDate.value
                                    ) || TextUtils.isEmpty(milestoneViewModel.addNewMilestoneTime.value) && TextUtils.isEmpty(
                                        milestoneViewModel.addNewMilestoneLocationB.value
                                    ) -> {
                                        milestoneViewModel.addNewMilestoneTittleEmpty.value = true
                                        milestoneViewModel.addNewMilestoneDateEmpty.value = true
                                        milestoneViewModel.addNewMilestoneTimeEmpty.value = true
                                        milestoneViewModel.addNewMilestoneLocationBEmpty.value =
                                            true
                                    }
                                    TextUtils.isEmpty(milestoneViewModel.addNewMilestoneTittle.value) -> {
                                        milestoneViewModel.addNewMilestoneTittleEmpty.value = true
                                    }
                                    TextUtils.isEmpty(milestoneViewModel.addNewMilestoneDate.value) -> {
                                        milestoneViewModel.addNewMilestoneDateEmpty.value = true
                                    }
                                    TextUtils.isEmpty(milestoneViewModel.addNewMilestoneTime.value) -> {
                                        milestoneViewModel.addNewMilestoneTimeEmpty.value = true
                                    }
                                    TextUtils.isEmpty(milestoneViewModel.addNewMilestoneLocationB.value) -> {
                                        milestoneViewModel.addNewMilestoneLocationBEmpty.value =
                                            true
                                    }
                                    else -> {
                                        coroutineScope.launch {
                                            if (milestoneViewModel.isBottomSheetOpen.value) {
                                                addNewMilestoneBottomSheetState.bottomSheetState.expand()
                                            } else {
                                                addNewMilestoneBottomSheetState.bottomSheetState.collapse()
                                            }
                                            milestoneViewModel.isBottomSheetOpen.value = false
                                        }
                                        val type =
                                            PreferenceProvider(context).getValue(Constants.TYPE, "")
                                        val userId =
                                            PreferenceProvider(context).getIntValue(
                                                Constants.USER_ID,
                                                0
                                            )
                                        val createMilestoneRequest = CreateMilestoneRequest(
                                            milestone = milestoneViewModel.addNewMilestoneTittle.value,
                                            user_type = type!!,
                                            user_id = userId,
                                            date = milestoneViewModel.addNewMilestoneDate.value,
                                            time = milestoneViewModel.addNewMilestoneTime.value,
                                            location = milestoneViewModel.addNewMilestoneLocationB.value,
                                            longitude = "",
                                            latitude = ""
                                        )
                                        milestoneViewModel.createMilestone(createMilestoneRequest)
                                        milestoneViewModel.createMilestoneResponse.observe(
                                            homeActivity
                                        ) {
                                            if (it != null) {
                                                handleCreatedMilestoneData(
                                                    homeActivity = homeActivity,
                                                    navHostController,
                                                    result = it,
                                                    context = context,
                                                    milestoneViewModel = milestoneViewModel,
                                                    coroutineScope = coroutineScope,
                                                    addNewMilestoneBottomSheetState = addNewMilestoneBottomSheetState
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
                                text = stringResource(id = R.string.add),
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
                BackHandler(addNewMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                    coroutineScope.launch {
                        if (milestoneViewModel.isBottomSheetOpen.value) {
                            addNewMilestoneBottomSheetState.bottomSheetState.expand()
                        } else {
                            addNewMilestoneBottomSheetState.bottomSheetState.collapse()
                        }
                        milestoneViewModel.isBottomSheetOpen.value = false
                    }
                }
                if (milestoneViewModel.isNewMilestoneAdded.value) {
                    ProgressBarTransparentBackground(loadingText = "Adding...")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.White
                            ), contentPadding = PaddingValues(bottom = 70.dp)
                    ) {
                        if (!milestoneViewModel.isSelected) item {
                            Column(
                                modifier = Modifier.background(
                                    Color(0xFFF4F4F4)
                                )
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 25.dp, top = 24.dp),
                                    text = stringResource(id = R.string.tittle_bottom_milestone),
                                    style = MaterialTheme.typography.body2,
                                    color = Color(0xFF7F7D7C),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 22.sp
                                )
                                Button(
                                    onClick = {
                                        milestoneViewModel.isBottomSheetOpen.value = true
                                        coroutineScope.launch {
                                            milestoneViewModel.addNewMilestoneTittleEmpty.value =
                                                false
                                            milestoneViewModel.addNewMilestoneDateEmpty.value =
                                                false
                                            milestoneViewModel.addNewMilestoneTimeEmpty.value =
                                                false
                                            milestoneViewModel.addNewMilestoneLocationBEmpty.value =
                                                false
                                            milestoneViewModel.addNewMilestoneTittle.value = ""
                                            milestoneViewModel.addNewMilestoneDate.value = ""
                                            milestoneViewModel.addNewMilestoneTime.value = ""
                                            milestoneViewModel.addNewMilestoneLocationB.value = ""
                                            if (addNewMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                                addNewMilestoneBottomSheetState.bottomSheetState.collapse()
                                                milestoneViewModel.isBottomSheetOpen.value = true
                                            } else {
                                                addNewMilestoneBottomSheetState.bottomSheetState.expand()
                                                milestoneViewModel.isBottomSheetOpen.value = false
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(top = 16.dp)
                                        .align(CenterHorizontally)
                                        .height(48.dp),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp
                                    ),
                                    shape = RoundedCornerShape(30),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFF3870C9),
                                    )
                                ) {
                                    Row {
                                        Text(
                                            text = (stringResource(id = R.string.add_new_milestone)),
                                            color = Color.White,
                                            style = MaterialTheme.typography.body2,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Image(
                                            modifier = Modifier.padding(start = 10.dp),
                                            painter = painterResource(id = R.drawable.icon_add_milestone),
                                            contentDescription = "",
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(top = 25.dp)
                                        .background(
                                            Color.White,
                                            shape = RoundedCornerShape(
                                                topStart = 18.dp,
                                                topEnd = 18.dp
                                            )
                                        )
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                        if (milestoneViewModel.isSelected) item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 23.dp)
                                        .combinedClickable(
                                            onClick = {
                                                milestoneViewModel.milestoneList.forEachIndexed { index, _ ->
                                                    milestoneViewModel.milestoneList[index].show =
                                                        true
                                                }
                                                val milestoneListNew =
                                                    milestoneViewModel.milestoneList.toList()
                                                milestoneViewModel.milestoneList.clear()
                                                milestoneViewModel.milestoneList.addAll(
                                                    milestoneListNew
                                                )
                                            },
                                        ),
                                    text = stringResource(id = R.string.select_all),
                                    color = Custom_Blue,
                                    style = MaterialTheme.typography.body2,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if (milestoneViewModel.isAllMilestoneLoaded) item {
                            MilestoneScreenShimmerAnimation()
                        } else {
                            items(milestoneViewModel.milestoneList.size) { index ->
                                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                                    val (card_main, img_select) = createRefs()
                                    CompositionLocalProvider(
                                        LocalRippleTheme provides ClearRippleTheme
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .zIndex(1f)
                                                .padding(top = 10.dp, bottom = 10.dp)
                                                .alpha(if (milestoneViewModel.milestoneList[index].show) 1f else 0f)
                                                .constrainAs(img_select) {
                                                    top.linkTo(parent.top)
                                                    end.linkTo(card_main.end, margin = 24.dp)
                                                },
                                            painter = painterResource(id = R.drawable.ic_icon_milestone_screen_item_select),
                                            contentDescription = stringResource(id = R.string.content_description),
                                        )
                                        Column(
                                            modifier = Modifier
                                                .padding(top = 20.dp)
                                                .constrainAs(card_main) {
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                    top.linkTo(parent.top)
                                                }
                                                .background(
                                                    Color.White,
                                                    RoundedCornerShape(
                                                        topStart = 20.dp,
                                                        topEnd = 20.dp
                                                    )
                                                )
                                        ) {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 24.dp, end = 24.dp)
                                                    .combinedClickable(
                                                        onClick = {
                                                            if (milestoneViewModel.isSelected) {
                                                                coroutineScope.launch {
                                                                    milestoneViewModel.milestoneList[index] =
                                                                        milestoneViewModel.milestoneList[index].copy(
                                                                            show = !milestoneViewModel.milestoneList[index].show
                                                                        )
                                                                    milestoneViewModel.milestoneList.forEach {
                                                                        if (it.show) {
                                                                            milestoneViewModel.isSelected =
                                                                                true
                                                                            return@launch
                                                                        }
                                                                    }
                                                                    milestoneViewModel.isSelected =
                                                                        false
                                                                }
                                                            } else {
                                                                navHostController.popBackStack(
                                                                    BottomNavScreen.AddNewMileStones.route,
                                                                    true
                                                                )
                                                                milestoneViewModel.milestoneList[index].id
                                                                    ?.let { it1 ->
                                                                        BottomNavScreen.AddNewMileStones.editMilestone(
                                                                            id = it1
                                                                        )
                                                                    }
                                                                    ?.let { it2 ->
                                                                        navHostController.navigate(
                                                                            route = it2
                                                                        )
                                                                    }
                                                            }
                                                        },
                                                        onLongClick = {
                                                            coroutineScope.launch {
                                                                if (!milestoneViewModel.isSelected) {
                                                                    milestoneViewModel.milestoneList[index].show =
                                                                        true
                                                                    milestoneViewModel.isSelected =
                                                                        true
                                                                }
                                                                back.value = true
                                                            }
                                                        }
                                                    ),
                                                shape = RoundedCornerShape(15.dp),
                                                border = BorderStroke(
                                                    width = if (milestoneViewModel.milestoneList[index].show) 1.5.dp else (-1).dp,
                                                    color = Color(0xFF3870C9)
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.background(Color.White)
                                                ) {
                                                    milestoneViewModel.milestoneList[index].title?.let { it1 ->
                                                        Text(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(
                                                                    start = 24.dp,
                                                                    top = 16.dp
                                                                ),
                                                            text = it1,
                                                            style = MaterialTheme.typography.body2,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.Black,
                                                            fontSize = 20.sp
                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 18.dp)
                                                    ) {
                                                        Image(
                                                            modifier = Modifier.padding(
                                                                top = 33.dp,
                                                                start = 24.dp
                                                            ),
                                                            painter = painterResource(id = R.drawable.img_medical_calender_icon),
                                                            contentDescription = stringResource(id = R.string.content_description),
                                                        )
                                                        Text(
                                                            modifier = Modifier.padding(
                                                                start = 8.dp,
                                                                top = 35.dp
                                                            ),
                                                            text = if (milestoneViewModel.milestoneList[index].date != null && milestoneViewModel.milestoneList[index].time != null && milestoneViewModel.milestoneList[index].date != "" && milestoneViewModel.milestoneList[index].time != "") "${milestoneViewModel.milestoneList[index].date} at ${milestoneViewModel.milestoneList[index].time}" else "N/A",
                                                            style = MaterialTheme.typography.body2,
                                                            color = Color(0xFF9F9D9B),
                                                            fontSize = 13.sp,
                                                        )
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            verticalAlignment = Alignment.Top,
                                                            horizontalArrangement = Arrangement.End
                                                        ) {
                                                            Image(
                                                                modifier = Modifier
                                                                    .padding(
                                                                        top = 32.dp,
                                                                        end = 28.dp
                                                                    ),
                                                                painter = painterResource(R.drawable.img_milestone_location),
                                                                contentDescription = stringResource(
                                                                    id = R.string.content_description),
                                                            )
                                                        }
                                                        Card(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(
                                                                    start = 16.dp,
                                                                    end = 24.dp,
                                                                    top = 16.dp,
                                                                    bottom = 12.dp
                                                                )
                                                                .alpha(0f),
                                                            shape = RoundedCornerShape(10.dp),
                                                            border = BorderStroke(
                                                                2.dp,
                                                                color = Color(0xFFF4F4F4)
                                                            )
                                                        ) {
                                                            Text(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(36.dp)
                                                                    .padding(
                                                                        top = 6.dp,
                                                                        bottom = 6.dp
                                                                    ),
                                                                text = stringResource(id = R.string.ask_surrogate),
                                                                color = Color(0xFF3870C9),
                                                                style = MaterialTheme.typography.body2,
                                                                fontWeight = FontWeight.Normal,
                                                                fontSize = 16.sp,
                                                                textAlign = TextAlign.Center
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (milestoneViewModel.isSelected) item {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                            ) {
                                val (btn_reset_milestone) = createRefs()
                                Button(
                                    onClick = {
                                        openDialogReset.value = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .constrainAs(btn_reset_milestone) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(parent.bottom)
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
                                        text = stringResource(id = R.string.reset_milestone),
                                        style = MaterialTheme.typography.body2,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                }
                                if (openDialogReset.value) {
                                    Dialog(
                                        onDismissRequest = { openDialogReset.value = false },
                                        properties = DialogProperties(
                                            dismissOnBackPress = true,
                                            dismissOnClickOutside = false,
                                            usePlatformDefaultWidth = true,
                                        )
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(170.dp),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            ResetMilestoneMilestone(
                                                openDialogResetMilestone = openDialogReset,
                                                tittle = R.string.are_you_sure_reset_milestone,
                                                milestoneViewModel = milestoneViewModel,
                                                homeActivity = homeActivity,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
        )
    }
}

fun getMilestones(
    milestoneViewModel: BottomMilestoneViewModel,
    context: Context,
    homeActivity: HomeActivity,
) {
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    milestoneViewModel.getMilestones(
        GetPregnancyMilestoneRequest(
            user_id = userId,
            type = type!!
        )
    )
    milestoneViewModel.getMilestoneResponse.observe(homeActivity) {
        if (it != null) {
            handleGetMilestoneData(
                result = it,
                milestoneViewModel = milestoneViewModel
            )
        }
    }
}

@Composable
fun ResetMilestoneMilestone(
    tittle: Int,
    openDialogResetMilestone: MutableState<Boolean>,
    milestoneViewModel: BottomMilestoneViewModel,
    homeActivity: HomeActivity,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .width(54.dp)
                    .height(59.dp)
                    .padding(top = 16.dp),
                painter = painterResource(id = R.drawable.logo_setting_delete_dialog),
                contentDescription = stringResource(id = R.string.content_description),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = stringResource(id = tittle),
                style = MaterialTheme.typography.body1.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.08).sp
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                color = Color(0xFFC7C7CC),
                thickness = 0.5.dp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp), Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 13.dp, start = 30.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        openDialogResetMilestone.value = false
                    },
                text = stringResource(id = R.string.negative_btn_text),
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF8E8E93),
                    fontWeight = FontWeight.W600,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            )
            Divider(
                color = Color(0xFFC7C7CC),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 55.dp)
                    .width(1.dp)
                    .height(55.dp),
                thickness = 0.5.dp
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 13.dp, end = 10.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        openDialogResetMilestone.value = false
                        val selectedMilestoneId = arrayListOf<Int>()
                        milestoneViewModel.milestoneList.forEach {
                            if (it.show)
                                it.id?.let { it1 -> selectedMilestoneId.add(it1) }
                        }
                        resetMilestoneApiCall(
                            selectedMilestoneId, context, milestoneViewModel,
                            homeActivity
                        )
                    },
                text = stringResource(id = R.string.reset_milestone),
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF007AFF),
                    fontWeight = FontWeight.W600,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

fun resetMilestoneApiCall(
    selectedMilestoneId: ArrayList<Int>,
    context: Context,
    milestoneViewModel: BottomMilestoneViewModel,
    homeActivity: HomeActivity,
) {
    milestoneViewModel.milestoneList.forEachIndexed { index, _ ->
        milestoneViewModel.milestoneList[index].show = false
    }
    val milestoneListNew =
        milestoneViewModel.milestoneList.toList()
    milestoneViewModel.milestoneList.clear()
    milestoneViewModel.milestoneList.addAll(milestoneListNew)
    milestoneViewModel.isSelected = false
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")

    milestoneViewModel.resetMilestone(
        ResetMilestoneRequest(
            selectedMilestoneId,
            type.toString(),
            userId
        )
    )
    milestoneViewModel.resetMilestoneResponse.observe(homeActivity) {
        if (it != null) {
            handleResetMilestoneData(
                result = it,
                context = context,
                milestoneViewModel = milestoneViewModel,
                homeActivity
            )
        }
    }
}

private fun handleResetMilestoneData(
    result: NetworkResult<ResetMilestoneResponse>,
    context: Context,
    milestoneViewModel: BottomMilestoneViewModel,
    homeActivity: HomeActivity,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            milestoneViewModel.isAllMilestoneLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            milestoneViewModel.isAllMilestoneLoaded = true
            getMilestones(milestoneViewModel, context, homeActivity)
        }
        is NetworkResult.Error -> {
            // show error message
            milestoneViewModel.isAllMilestoneLoaded = true
        }
    }
}

private fun handleGetMilestoneData(
    result: NetworkResult<GetMilestoneResponse>,
    milestoneViewModel: BottomMilestoneViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            milestoneViewModel.isAllMilestoneLoaded = true
            milestoneViewModel.isAnyErrorOccurred = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            milestoneViewModel.isAllMilestoneLoaded = false
            if (result.data?.milestone?.isEmpty()!!) {
                milestoneViewModel.isAnyErrorOccurred = true
            }
            milestoneViewModel.milestoneList.clear()
            milestoneViewModel.milestoneList.addAll(result.data.milestone)
        }
        is NetworkResult.Error -> {
            // show error message
            milestoneViewModel.isAllMilestoneLoaded = false
            milestoneViewModel.isAnyErrorOccurred = true
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun handleCreatedMilestoneData(
    homeActivity: HomeActivity,
    navHostController: NavHostController,
    result: NetworkResult<CommonResponse>,
    context: Context,
    milestoneViewModel: BottomMilestoneViewModel,
    coroutineScope: CoroutineScope,
    addNewMilestoneBottomSheetState: BottomSheetScaffoldState,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            milestoneViewModel.isNewMilestoneAdded.value = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            coroutineScope.launch {
                addNewMilestoneBottomSheetState.bottomSheetState.collapse()
            }
            milestoneViewModel.isNewMilestoneAdded.value = false
            Toast.makeText(context, result.data?.message, Toast.LENGTH_SHORT).show()
            milestoneViewModel.milestoneList.clear()
            milestoneViewModel.milestoneList.addAll(milestoneViewModel.emptyList)
            getMilestones(
                milestoneViewModel = milestoneViewModel,
                context = context,
                homeActivity = homeActivity
            )
        }
        is NetworkResult.Error -> {
            // show error message
            milestoneViewModel.isNewMilestoneAdded.value = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

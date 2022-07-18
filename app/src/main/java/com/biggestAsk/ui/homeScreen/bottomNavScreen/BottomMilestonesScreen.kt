package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.homeScreen.ClearRippleTheme
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MilestonesScreen(navHostController: NavHostController, viewModel: MainViewModel) {
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
    val focusManager = LocalFocusManager.current
    viewModel.list = viewModel.listData
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
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 11.dp)
                            .align(CenterHorizontally),
                        text = "Add milestone", style = MaterialTheme.typography.h2.copy(
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
                        value = viewModel.addNewMilestoneTittle.value,
                        onValueChange = {
                            viewModel.addNewMilestoneTittle.value = it
                            viewModel.addNewMilestoneTittleEmpty.value = false
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
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFF4F4F4),
                            cursorColor = Custom_Blue,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    if (viewModel.addNewMilestoneTittleEmpty.value) {
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
                                    { _: DatePicker, day: Int, month: Int, year: Int ->
                                        viewModel.addNewMilestoneDate.value = "$month/$year/$day"
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
                            },
                        value = viewModel.addNewMilestoneDate.value,
                        onValueChange = {
                            viewModel.addNewMilestoneDate.value
                            viewModel.addNewMilestoneDateEmpty.value = false
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
                    if (viewModel.addNewMilestoneDateEmpty.value) {
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
                                        viewModel.addNewMilestoneTime.value =
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
                            }, readOnly = true, enabled = false,
                        value = viewModel.addNewMilestoneTime.value,
                        onValueChange = {
                            viewModel.addNewMilestoneTime.value
                            viewModel.addNewMilestoneTimeEmpty.value = false
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
                    if (viewModel.addNewMilestoneTimeEmpty.value) {
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
                        value = viewModel.addNewMilestoneLocationB.value,
                        onValueChange = {
                            viewModel.addNewMilestoneLocationB.value = it
                            viewModel.addNewMilestoneLocationBEmpty.value = false
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
                    if (viewModel.addNewMilestoneLocationBEmpty.value) {
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
//                            listMilestoneDetails.add(
//                                MilestoneDetails(
//                                    tittle = viewModel.addNewMilestoneTittle.value,
//                                    calendarIcon = R.drawable.img_medical_calender_icon,
//                                    dateTime = "09/22/2021 at 9:30AM",
//                                    locationIcon = R.drawable.img_milestone_location,
//                                    show = false
//                                )
//                            )
                            coroutineScope.launch {
                                addNewMilestoneBottomSheetState.bottomSheetState.collapse()
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
                            text = "Add",
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
                    .background(
                        Color.White
                    ), contentPadding = PaddingValues(bottom = 70.dp)
            ) {
                if (!viewModel.isSelected) item {
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
                                coroutineScope.launch {
                                    if (addNewMilestoneBottomSheetState.bottomSheetState.isExpanded) {
                                        addNewMilestoneBottomSheetState.bottomSheetState.collapse()
                                    } else {
                                        addNewMilestoneBottomSheetState.bottomSheetState.expand()
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
                if (viewModel.isSelected) item {
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
                                        viewModel.list.forEachIndexed { index, _ ->
                                            viewModel.list[index].show = true
                                        }
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
                items(viewModel.list.size) { index ->
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (card_main, img_select) = createRefs()
                        CompositionLocalProvider(
                            LocalRippleTheme provides ClearRippleTheme
                        ) {
                            Image(
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(top = 10.dp, bottom = 10.dp)
                                    .alpha(if (viewModel.list[index].show) 1f else 0f)
                                    .constrainAs(img_select) {
                                        top.linkTo(parent.top)
                                        end.linkTo(card_main.end, margin = 24.dp)
                                    },
                                painter = painterResource(id = R.drawable.ic_icon_milestone_screen_item_select),
                                contentDescription = ""
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
                                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                    )
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 24.dp)
                                        .combinedClickable(
                                            onClick = {
                                                if (viewModel.isSelected) {
                                                    coroutineScope.launch {
                                                        viewModel.list[index].show =
                                                            !viewModel.list[index].show
                                                        viewModel.list.forEach {
                                                            if (it.show) {
                                                                viewModel.isSelected = true
                                                                return@launch
                                                            }
                                                        }
                                                        viewModel.isSelected = false
                                                    }
                                                } else {
                                                    navHostController.popBackStack(
                                                        BottomNavScreen.AddNewMileStones.route,
                                                        true
                                                    )
                                                    navHostController.navigate(route = "add_new_milestone/${viewModel.list[index].tittle}")
                                                }
                                            },
                                            onLongClick = {
//                                                        coroutineScope.launch {
                                                if (!viewModel.isSelected) {
                                                    viewModel.list[index].show = true
                                                    viewModel.isSelected = true
                                                }
                                                // viewModel.list = viewModel.listMilestoneDetails
//                                                        }
                                            }
                                        ),
                                    shape = RoundedCornerShape(15.dp),
                                    border = BorderStroke(
                                        width = if (viewModel.list[index].show) 1.5.dp else (-1).dp,
                                        color = Color(0xFF3870C9)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.background(Color.White)
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 24.dp, top = 16.dp),
                                            text = viewModel.list[index].tittle,
                                            style = MaterialTheme.typography.body2,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            fontSize = 20.sp
                                        )
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
                                                painter = painterResource(id = viewModel.list[index].calendarIcon),
                                                contentDescription = ""
                                            )
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 8.dp,
                                                    top = 35.dp
                                                ),
                                                text = viewModel.list[index].dateTime,
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
                                                        .padding(top = 32.dp, end = 28.dp),
                                                    painter = painterResource(id = viewModel.list[index].locationIcon),
                                                    contentDescription = ""
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
                                                        .padding(top = 6.dp, bottom = 6.dp),
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
                if (viewModel.isSelected) item{
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
                                        coroutineScope = coroutineScope,
                                        viewModel = viewModel,
                                        openDialogResetMilestone = openDialogReset
                                    )
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

@Composable
fun ResetMilestoneMilestone(
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel,
    openDialogResetMilestone: MutableState<Boolean>
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
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.are_you_sure_reset_milestone),
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
                        Toast
                            .makeText(
                                context,
                                R.string.reset_milestone_success_message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
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

@Preview
@Composable
fun ResetMilestoneMilestonePreview() {
    ResetMilestoneMilestone(
        coroutineScope = rememberCoroutineScope(),
        openDialogResetMilestone = remember { mutableStateOf(true) },
        viewModel = MainViewModel()
    )
}
//object ClearRippleTheme : RippleTheme {
//    @Composable
//    override fun defaultColor(): Color = Color.Transparent
//
//    @Composable
//    override fun rippleAlpha() = RippleAlpha(
//        draggedAlpha = 0.0f,
//        focusedAlpha = 0.0f,
//        hoveredAlpha = 0.0f,
//        pressedAlpha = 0.0f,
//    )
//}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MilestoneScreenPreview() {
    MilestonesScreen(rememberNavController(), MainViewModel())
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun MilestoneSmallScreenPreview() {
    MilestonesScreen(rememberNavController(), MainViewModel())
}
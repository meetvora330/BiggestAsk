@file:Suppress("NAME_SHADOWING", "DEPRECATION")

package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavItems
import com.biggestAsk.ui.homeScreen.bottomDrawerNavGraph.BottomNavScreen
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.CheckBox_Check
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.example.biggestAsk.R
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNewMilestoneScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel,
    milestoneName: String
) {
    val mContext =
        LocalContext.current
    val focusManager = LocalFocusManager.current
    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()
    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()
    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        R.style.CalenderViewCustom,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val stroke = Stroke(
        width = 5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
    )

    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }
    val isChosen = remember { mutableStateOf(false) }
    // Creating a TimePicker dialog
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        R.style.TimeViewCustom,
        { _, mHour: Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            Log.i("TAG", uri.toString())
            if (Build.VERSION.SDK_INT < 28) {
                if (viewModel.imageListIndex.value != -1) {
                    viewModel.imageList[viewModel.imageListIndex.value] =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    viewModel.imageList.add(
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    )
                }
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                if (viewModel.imageListIndex.value != -1) {
                    viewModel.imageList[viewModel.imageListIndex.value] =
                        ImageDecoder.decodeBitmap(source)
                } else {
                    viewModel.imageList.add(
                        ImageDecoder.decodeBitmap(source)
                    )
                }
            }
            viewModel.imageListIndex.value = -1
            Log.i("TAG", viewModel.imageList.size.toString())
        }
    }
    val isPicAvailable = remember {
        mutableStateOf(true)
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
                Row(modifier = Modifier.fillMaxWidth()) {
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
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            value = milestoneName,
                            onValueChange = { milestoneName },
                            textStyle = MaterialTheme.typography.body2.copy(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400,
                                lineHeight = 22.sp,
                            ), singleLine = true,
                            maxLines = 1
                        )
                        Row(modifier = Modifier.padding(top = 32.dp, start = 16.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_medical_calender_icon),
                                contentDescription = ""
                            )
                            Text(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 8.dp, top = 2.dp),
                                text = "09/22/2021 at 9:30AM",
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
        items(viewModel.imageList.size) { index ->
            Log.i("TAG", "List Size From Inner ${viewModel.imageList.size}")
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
                                viewModel.imageList.removeAt(index)
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
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(),
                        bitmap = viewModel.imageList[index].asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
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
                            viewModel.imageListIndex.value = index
                            Log.i("TAG", index.toString())
                            launcher.launch("image/*")
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
                        if (viewModel.imageList.size > 4) {
                            Toast.makeText(
                                context,
                                "You can select maximum 5 images.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.imageListIndex.value = -1
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
                value = viewModel.addNewMilestoneNotes.value,
                onValueChange = {
                    viewModel.addNewMilestoneNotes.value = it
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
                    onClick = { },
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
                        text = "Save notes", style = MaterialTheme.typography.body1.copy(
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
                            checkedColor = CheckBox_Check, uncheckedColor = Color.DarkGray
                        ),
                        onCheckedChange = {
                            viewModel.checkBoxShareWithParents = it
                        })
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 10.dp),
                        text = "Share with parents",
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
                        navHostController.popBackStack(BottomNavScreen.AddNewMileStones.route, true)
                        navHostController.navigate(BottomNavItems.Milestones.navRoute)
                        navHostController.popBackStack(BottomNavScreen.MileStones.route, true)
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
                        text = "Update milestone", style = MaterialTheme.typography.body2.copy(
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddNewMilestoneScreenPreview() {
    AddNewMilestoneScreen(rememberNavController(), MainViewModel(), "Milestone clearance exam")
}


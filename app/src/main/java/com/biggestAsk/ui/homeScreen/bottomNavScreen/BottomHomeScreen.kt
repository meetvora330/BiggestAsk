package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.example.biggestAsk.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomHomeScreen(navHostController: NavHostController) {
    val homeBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val viewModel = MainViewModel()
    val focusManager = LocalFocusManager.current
    BottomSheetScaffold(scaffoldState = homeBottomSheetScaffoldState, sheetContent = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(bottom = 10.dp),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp),
                text = stringResource(id = R.string.new_question),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 8.dp),
                text = stringResource(id = R.string.bottom_ques_bottom_sheet_new_question_desc),
                style = MaterialTheme.typography.body2,
                color = Color(0xFF7F7D7C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 12.dp, top = 19.dp),
                text = stringResource(id = R.string.text_home_bottom_sheet_answer),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W800,
                fontSize = 12.sp,
                color = Color.Black
            )
            TextField(
                value = viewModel.bottomQuesHome,
                onValueChange = {
                    viewModel.bottomQuesHome = it
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
                    .padding(top = 12.dp, start = 13.dp, end = 13.dp),
                textStyle = MaterialTheme.typography.body2,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.text_home_bottom_sheet_answer),
                        style = MaterialTheme.typography.body1.copy(
                            color = Text_Color,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400,
                            lineHeight = 24.sp
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
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 12.dp, top = 16.dp),
                text = stringResource(id = R.string.text_home_bottom_sheet_parent),
                style = MaterialTheme.typography.body2,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800
            )
            DropDownMenu()
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (homeBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                homeBottomSheetScaffoldState.bottomSheetState.collapse()
                            } else {
                                homeBottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }, modifier = Modifier
                        .padding(
                            start = 24.dp, end = 24.dp, bottom = 50.dp, top = 45.dp
                        )
                        .fillMaxWidth()
                        .height(56.dp), elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    ), shape = RoundedCornerShape(30), colors = ButtonDefaults.buttonColors(
                        backgroundColor = Custom_Blue,
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.add_answer),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 40.dp),
                text = stringResource(id = R.string.bottom_home_screen_pregnancy_milestone),
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 24.sp
            )
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 19.dp),
                        text = "Week seventeen",
                        color = Color.Black,
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W800,
                        lineHeight = 24.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 6.dp),
                        text = "Your baby is the size of avocado!",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    )
                    Image(
                        modifier = Modifier.padding(top = 3.dp, start = 24.dp, bottom = 11.dp),
                        painter = painterResource(id = R.drawable.img_test),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 24.dp),
                text = stringResource(id = R.string.bottom_home_screen_next_milestone),
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 24.sp
            )
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.padding(
                                top = 14.dp, start = 24.dp, bottom = 24.dp, end = 24.dp
                            ),
                            painter = painterResource(id = R.drawable.img_medical_clearence),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp),
                        text = "Medical Clearance Exam",
                        style = MaterialTheme.typography.body2,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 32.sp
                    )
                    Row {
                        Image(
                            modifier = Modifier.padding(top = 15.dp, start = 24.dp, bottom = 26.dp),
                            painter = painterResource(id = R.drawable.img_medical_calender_icon),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp, top = 17.dp),
                            text = "09/22/2021 at 9:30AM",
                            color = Color(0xFF9F9D9B),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W400,
                            lineHeight = 16.sp,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(start = 24.dp, top = 40.dp),
                text = stringResource(id = R.string.bottom_home_screen_your_last_question),
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 24.sp
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4479CC),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 37.dp, top = 24.dp, end = 36.dp),
                        text = stringResource(id = R.string.bottom_home_screen_latest_question_tittle),
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W900,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 6.dp),
                        text = stringResource(id = R.string.bottom_home_screen_latest_question_sub_tittle),
                        color = Color.White,
                        style = MaterialTheme.typography.body2,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (homeBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                    homeBottomSheetScaffoldState.bottomSheetState.collapse()
                                } else {
                                    homeBottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        }, modifier = Modifier
                            .padding(
                                start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp
                            )
                            .fillMaxWidth()
                            .height(48.dp), elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        ), shape = RoundedCornerShape(30), colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                        )
                    ) {
                        Text(
                            text = (stringResource(id = R.string.answer_the_question)),
                            color = Color(0xFF3870C9),
                            style = MaterialTheme.typography.body2,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(start = 24.dp, top = 44.dp),
                text = stringResource(id = R.string.bottom_home_screen_your_surrogate_mother),
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 24.sp
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 70.dp)
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                        text = "What is your favorite snack?",
                        color = Color.Black,
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                    )
                    Row {
                        Text(
                            modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                            text = "Martha Smith",
                            color = Custom_Blue,
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, end = 24.dp),
                            text = "1 Day ago",
                            color = Color(0xFF9F9D9B),
                            style = MaterialTheme.typography.body2,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.End
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 24.dp, top = 4.dp, bottom = 22.dp),
                        text = "Chocolate all the way!!",
                        color = Color.Black,
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }, sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenBottom() {
    BottomHomeScreen(rememberNavController())
}


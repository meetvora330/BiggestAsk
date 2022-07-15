package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.IntendedParentQuestionAnsRequest
import com.biggestAsk.data.model.response.GetHomeScreenQuestionResponse
import com.biggestAsk.data.model.response.GetNearestMilestoneResponse
import com.biggestAsk.data.model.response.GetPregnancyMilestoneResponse
import com.biggestAsk.data.model.response.IntendedParentQuestionResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.BottomHomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomHomeScreen(
    navHostController: NavHostController,
    context: Context,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel
) {
    val homeBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val viewModel = MainViewModel()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        val provider = PreferenceProvider(context)
        val userId = provider.getIntValue("user_id", 0)
        val type = provider.getValue("type", "")
        val partnerId = provider.getIntValue("partner_id", 0)
        Log.d("TAG", "BottomHomeScreen: $userId")
        Log.d("TAG", "BottomHomeScreen: $type")
        Log.d("TAG", "BottomHomeScreen: $partnerId")
        bottomHomeViewModel.getPregnancyMilestone(
            GetPregnancyMilestoneRequest(
                user_id = userId,
                type = type!!
            )
        )
        bottomHomeViewModel.getHomeScreenQuestion(
            GetPregnancyMilestoneRequest(
                user_id = 2,
                type = type
            )
        )
        bottomHomeViewModel.getIntendedParentQuestionAns(
            IntendedParentQuestionAnsRequest(
                user_id = userId,
                partner_id = partnerId,
                type = type
            )
        )
        bottomHomeViewModel.getNearestMilestone(
            GetPregnancyMilestoneRequest(
                user_id = 2,
                type = type
            )
        )
        bottomHomeViewModel.getPregnancyMilestoneResponse.observe(homeActivity) {
            if (it != null) {
                handlePregnancyMilestoneData(
                    navHostController = navHostController,
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
        bottomHomeViewModel.getHomeScreenQuestionResponse.observe(homeActivity) {
            if (it != null) {
                handleHomeQuestionData(
                    navHostController = navHostController,
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
        bottomHomeViewModel.intendedPartnerQuestionAnsResponse.observe(homeActivity) {
            if (it != null) {
                handleIntendedParentQuestionAnsData(
                    navHostController = navHostController,
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
        bottomHomeViewModel.getNearestMilestoneResponse.observe(homeActivity) {
            if (it != null) {
                handleNearestMilestoneData(
                    navHostController = navHostController,
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                    viewModel = viewModel,
                    context = context
                )
            }
        }
    }
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
                text = bottomHomeViewModel.homeScreenLatestQuestion,
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
            if (!bottomHomeViewModel.isPregnancyDataLoaded) {
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
                            text = bottomHomeViewModel.pregnancyTittle,
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
                            text = bottomHomeViewModel.pregnancyDescription,
                            style = MaterialTheme.typography.body2,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            lineHeight = 22.sp
                        )
                        GlideImage(
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .padding(top = 3.dp, start = 24.dp, bottom = 11.dp),
                            imageModel = bottomHomeViewModel.pregnancyImageUrl,
                            contentScale = ContentScale.Crop
                        )
                    }

                }
            }
            if (!bottomHomeViewModel.isNearestMilestoneDataLoaded){
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
                            text = bottomHomeViewModel.nearestMilestoneTittle,
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
                                text = "${bottomHomeViewModel.nearestMilestoneDate} at ${bottomHomeViewModel.nearestMilestoneTime}",
                                color = Color(0xFF9F9D9B),
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.W400,
                                lineHeight = 16.sp,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
            if (!bottomHomeViewModel.isHomeScreenQuestionDataLoaded) {
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
                            text = bottomHomeViewModel.homeScreenLatestQuestion,
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
            }
            if (!bottomHomeViewModel.isIntendedParentQuestionDataLoaded) {
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
                            text = bottomHomeViewModel.intendedParentQuestion,
                            color = Color.Black,
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W600,
                            lineHeight = 24.sp,
                            fontSize = 16.sp,
                        )
                        Row {
                            Text(
                                modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                                text = bottomHomeViewModel.intendedParentUserName,
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
                            text = bottomHomeViewModel.intendedParentAnswer,
                            color = Color.Black,
                            style = MaterialTheme.typography.body2,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }, sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
}

private fun handlePregnancyMilestoneData(
    navHostController: NavHostController,
    result: NetworkResult<GetPregnancyMilestoneResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    viewModel: MainViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isPregnancyDataLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.pregnancyTittle = result.data!!.title
            bottomHomeViewModel.pregnancyDescription = result.data.description
            bottomHomeViewModel.pregnancyImageUrl = result.data.image
            bottomHomeViewModel.isPregnancyDataLoaded = false

        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isPregnancyDataLoaded = false
        }
    }
}

private fun handleHomeQuestionData(
    navHostController: NavHostController,
    result: NetworkResult<GetHomeScreenQuestionResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    viewModel: MainViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.homeScreenLatestQuestion = result.data?.data?.question.toString()
            Log.d("TAG", "handleHomeQuestionData: ${result.data?.data?.question}")
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = false

        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = false
        }
    }
}

private fun handleIntendedParentQuestionAnsData(
    navHostController: NavHostController,
    result: NetworkResult<IntendedParentQuestionResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    viewModel: MainViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.intendedParentQuestion = result.data?.data?.question!!
            bottomHomeViewModel.intendedParentUserName = result.data.user_name
            bottomHomeViewModel.intendedParentAnswer = result.data.data.answer
            bottomHomeViewModel.intendedParentDays = result.data.day
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = false

        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = false
        }
    }
}

private fun handleNearestMilestoneData(
    navHostController: NavHostController,
    result: NetworkResult<GetNearestMilestoneResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    viewModel: MainViewModel,
    context: Context
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isNearestMilestoneDataLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.nearestMilestoneTittle = result.data?.data?.title!!
            bottomHomeViewModel.nearestMilestoneDate = result.data.data.date
            bottomHomeViewModel.nearestMilestoneTime = result.data.data.time
            bottomHomeViewModel.isNearestMilestoneDataLoaded = false
        }
        is NetworkResult.Error -> {
            // show error message
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isNearestMilestoneDataLoaded = false
        }
    }
}



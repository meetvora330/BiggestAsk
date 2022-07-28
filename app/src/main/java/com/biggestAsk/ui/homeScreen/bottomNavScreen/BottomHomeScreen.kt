package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
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
import com.biggestAsk.data.model.request.Answer
import com.biggestAsk.data.model.request.GetPregnancyMilestoneRequest
import com.biggestAsk.data.model.request.IntendedParentQuestionAnsRequest
import com.biggestAsk.data.model.request.StoreBaseScreenQuestionAnsRequest
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.BottomHomeViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomHomeScreen(
    context: Context,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel
) {
    val homeBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val provider = PreferenceProvider(context)
    val userId = provider.getIntValue("user_id", 0)
    val type = provider.getValue("type", "")
    val partnerId = provider.getIntValue("partner_id", 0)
    LaunchedEffect(Unit) {
        Log.d("TAG", "BottomHomeScreen: User Id $userId")
        Log.d("TAG", "BottomHomeScreen: Type $type")
        Log.d("TAG", "BottomHomeScreen: Partner Id $partnerId")
        bottomHomeViewModel.getPregnancyMilestone(
            GetPregnancyMilestoneRequest(
                user_id = userId,
                type = type!!
            )
        )
        bottomHomeViewModel.getNearestMilestone(
            GetPregnancyMilestoneRequest(
                user_id = userId,
                type = type
            )
        )
        getHomeScreenQuestion(
            user_id = userId,
            type = type,
            bottomHomeViewModel = bottomHomeViewModel,
            homeActivity = homeActivity
        )
        bottomHomeViewModel.getIntendedParentQuestionAns(
            IntendedParentQuestionAnsRequest(
                user_id = userId,
                partner_id = partnerId,
                type = type
            )
        )
        bottomHomeViewModel.getPregnancyMilestoneResponse.observe(homeActivity) {
            if (it != null) {
                handlePregnancyMilestoneData(
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                )
            }
        }
        bottomHomeViewModel.getNearestMilestoneResponse.observe(homeActivity) {
            if (it != null) {
                handleNearestMilestoneData(
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
                )
            }
        }
        bottomHomeViewModel.intendedPartnerQuestionAnsResponse.observe(homeActivity) {
            if (it != null) {
                handleIntendedParentQuestionAnsData(
                    result = it,
                    bottomHomeViewModel = bottomHomeViewModel,
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
                value = bottomHomeViewModel.homeScreenQuestionAns,
                onValueChange = {
                    bottomHomeViewModel.homeScreenQuestionAns = it
                    bottomHomeViewModel.isHomeScreenQuestionAnsEmpty = false
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
            if (bottomHomeViewModel.isHomeScreenQuestionAnsEmpty) {
                Text(
                    text = stringResource(id = R.string.bottom_home_screen_question_ans_empty_text),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 8.dp),
                    fontSize = 12.sp
                )
            }
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
                        if (TextUtils.isEmpty(bottomHomeViewModel.homeScreenQuestionAns)) {
                            bottomHomeViewModel.isHomeScreenQuestionAnsEmpty = true
                        } else {
                            bottomHomeViewModel.answerList.add(
                                Answer(
                                    answer = bottomHomeViewModel.homeScreenQuestionAns,
                                    question_id = bottomHomeViewModel.homeScreenQuestionId
                                )
                            )
                            bottomHomeViewModel.storeBaseScreenQuestionAns(
                                StoreBaseScreenQuestionAnsRequest(
                                    answer = bottomHomeViewModel.answerList,
                                    category_id = bottomHomeViewModel.homeScreenQuestionCategeryId,
                                    partner_id = partnerId.toString(),
                                    type = type!!,
                                    user_id = userId
                                )
                            )
                            bottomHomeViewModel.storeBaseScreenQuestionAnsResponse.observe(
                                homeActivity
                            ) {
                                if (it != null) {
                                    handleStoreQuestionAnsData(
                                        result = it,
                                        bottomHomeViewModel = bottomHomeViewModel,
                                        coroutineScope = coroutineScope,
                                        bottomSheetScaffoldState = homeBottomSheetScaffoldState,
                                        user_id = userId,
                                        type = type,
                                        homeActivity = homeActivity
                                    )
                                }
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
        if (bottomHomeViewModel.isHomeScreenQuestionDataLoaded && bottomHomeViewModel.isPregnancyDataLoaded && bottomHomeViewModel.isNearestMilestoneDataLoaded && bottomHomeViewModel.isIntendedParentQuestionDataLoaded) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 10.dp),
                    text = if (bottomHomeViewModel.isHomeScreenQuestionDataLoaded && bottomHomeViewModel.isPregnancyDataLoaded && bottomHomeViewModel.isNearestMilestoneDataLoaded && bottomHomeViewModel.isIntendedParentQuestionDataLoaded) stringResource(
                        id = R.string.no_data_found
                    ) else "",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W600
                )
            }
        } else {
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
                if (!bottomHomeViewModel.isNearestMilestoneDataLoaded) {
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
                                    modifier = Modifier.padding(
                                        top = 15.dp,
                                        start = 24.dp,
                                        bottom = 26.dp
                                    ),
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
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 16.dp,
                                bottom = if (bottomHomeViewModel.isIntendedParentQuestionDataLoaded) 70.dp else 0.dp
                            )
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
                                },
                                modifier = Modifier
                                    .padding(
                                        start = 24.dp,
                                        end = 24.dp,
                                        top = 16.dp,
                                        bottom = 24.dp
                                    )
                                    .fillMaxWidth()
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
                                    text = "${bottomHomeViewModel.intendedParentDays} Day ago",
                                    color = Color(0xFF9F9D9B),
                                    style = MaterialTheme.typography.body2,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End
                                )
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    top = 4.dp,
                                    bottom = 22.dp
                                ),
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
        }

    }, sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
    if (bottomHomeViewModel.isAllDataLoaded || bottomHomeViewModel.isHomeScreenQuestionAnswered) {
        ProgressBarTransparentBackground(if (bottomHomeViewModel.isAllDataLoaded) "Loading...." else "Adding...", id = R.color.white)
    }
}

fun getHomeScreenQuestion(
    user_id: Int,
    type: String,
    bottomHomeViewModel: BottomHomeViewModel,
    homeActivity: HomeActivity
) {
    bottomHomeViewModel.getHomeScreenQuestion(
        GetPregnancyMilestoneRequest(
            user_id = user_id,
            type = type
        )
    )
    bottomHomeViewModel.getHomeScreenQuestionResponse.observe(homeActivity) {
        if (it != null) {
            handleHomeQuestionData(
                result = it,
                bottomHomeViewModel = bottomHomeViewModel,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun handleStoreQuestionAnsData(
    result: NetworkResult<CommonResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    user_id: Int,
    type: String,
    homeActivity: HomeActivity
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isHomeScreenQuestionAnswered = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            getHomeScreenQuestion(user_id, type, bottomHomeViewModel, homeActivity)
            coroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
            bottomHomeViewModel.answerList.clear()
//            bottomHomeViewModel.homeScreenQuestionCategeryId = 0
//            bottomHomeViewModel.homeScreenQuestionId = 0
        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

private fun handlePregnancyMilestoneData(
    result: NetworkResult<GetPregnancyMilestoneResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isPregnancyDataLoaded = true
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.pregnancyTittle = result.data!!.title
            bottomHomeViewModel.pregnancyDescription = result.data.description
            bottomHomeViewModel.pregnancyImageUrl = result.data.image
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isPregnancyDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false

        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isPregnancyDataLoaded = true
        }
    }
}

private fun handleHomeQuestionData(
    result: NetworkResult<GetHomeScreenQuestionResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.homeScreenLatestQuestion = result.data?.data?.question.toString()
            Log.d("TAG", "handleHomeQuestionData: ${result.data?.data?.question}")
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = result.data?.data?.question == null
            if (result.data?.data?.category_id == null || result.data.data.id == null) {
                bottomHomeViewModel.homeScreenQuestionCategeryId = 0
                bottomHomeViewModel.homeScreenQuestionId = 0
            } else {
                bottomHomeViewModel.homeScreenQuestionCategeryId = result.data.data.category_id
                bottomHomeViewModel.homeScreenQuestionId = result.data.data.id
                bottomHomeViewModel.homeScreenQuestionAns = ""
            }
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = true
        }
    }
}


private fun handleNearestMilestoneData(
    result: NetworkResult<GetNearestMilestoneResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.nearestMilestoneTittle = result.data?.title!!
            bottomHomeViewModel.nearestMilestoneDate = result.data.date
            bottomHomeViewModel.nearestMilestoneTime = result.data.time
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = false
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = true
        }
    }
}

private fun handleIntendedParentQuestionAnsData(
    result: NetworkResult<IntendedParentQuestionResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
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
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = false

        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = true
        }
    }
}


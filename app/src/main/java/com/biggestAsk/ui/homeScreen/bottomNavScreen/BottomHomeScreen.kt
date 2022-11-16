package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberImagePainter
import com.biggestAsk.data.model.request.*
import com.biggestAsk.data.model.request.Answer
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.HomeScreenQuestionShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.IntendedParentsShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.NearestMilestoneShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.PregnancyMilestoneShimmerAnimation
import com.biggestAsk.ui.main.viewmodel.BottomHomeViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.biggestAsk.util.*
import com.example.biggestAsk.R
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomHomeScreen(
    context: Context,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    val homeBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val provider = PreferenceProvider(context)
    val userId = provider.getIntValue(Constants.USER_ID, 0)
    val type = provider.getValue(Constants.TYPE, "")
    val partnerId = provider.getIntValue(Constants.PARTNER_ID, 0)
    val selectedText = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        updateHomeScreenData(
            bottomHomeViewModel = bottomHomeViewModel,
            userId = userId,
            type = type!!,
            homeActivity = homeActivity,
            partnerId = partnerId,
            context = context
        )
    }
    BottomSheetScaffold(
        scaffoldState = homeBottomSheetScaffoldState,
        sheetGesturesEnabled = true,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 10.dp),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
                    contentDescription = stringResource(id = R.string.content_description),
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
                    text = if (bottomHomeViewModel.upperQuestion) bottomHomeViewModel.homeScreenImportantQuestion else bottomHomeViewModel.homeScreenLatestQuestion,
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
                if (type != stringResource(id = R.string.surrogate)) {
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
                    if (bottomHomeViewModel.parentList.isNotEmpty()) selectedText.value =
                        SimpleDropDown(
                            suggestions = bottomHomeViewModel.parentList,
                            hint = stringResource(id = R.string.select_user_name_hint),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                        )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = {
                            if (TextUtils.isEmpty(bottomHomeViewModel.homeScreenQuestionAns)) {
                                bottomHomeViewModel.isHomeScreenQuestionAnsEmpty = true
                            } else {
                                if (bottomHomeViewModel.upperQuestion) {
                                    val userName = provider.getValue(Constants.USER_NAME, "")
                                    bottomHomeViewModel.storeAnsImportantQuestion(
                                        StoreAnsImportantQuestionRequest(
                                            question_id = bottomHomeViewModel.homeScreenImportantQuestionId,
                                            answer = bottomHomeViewModel.homeScreenQuestionAns,
                                            user_name = (if (type == Constants.PARENT) selectedText.value else userName).toString()
                                        )
                                    )
                                    coroutineScope.launch {
                                        homeBottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                    bottomHomeViewModel.storeAnsImportantQuestionResponse.observe(
                                        homeActivity
                                    ) {
                                        if (it != null) {
                                            handleStoreAnsImportantQuestion(
                                                result = it,
                                                bottomHomeViewModel = bottomHomeViewModel,
                                                coroutineScope = coroutineScope,
                                                bottomSheetScaffoldState = homeBottomSheetScaffoldState,
                                                userId = userId,
                                                type = type!!,
                                            )
                                        }
                                    }
                                } else {
                                    bottomHomeViewModel.answerList.add(
                                        Answer(
                                            answer = bottomHomeViewModel.homeScreenQuestionAns,
                                            question_id = bottomHomeViewModel.homeScreenQuestionId,
                                        )
                                    )
                                    bottomHomeViewModel.storeBaseScreenQuestionAns(
                                        StoreBaseScreenQuestionAnsRequest(
                                            answer = bottomHomeViewModel.answerList,
                                            category_id = bottomHomeViewModel.homeScreenQuestionCategeryId,
                                            partner_id = partnerId.toString(),
                                            type = type!!,
                                            user_id = userId,
                                            user_name = provider.getValue(Constants.USER_NAME, "")
                                                .toString()
                                        )
                                    )
                                    coroutineScope.launch {
                                        homeBottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
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
        },
        content = {
            if (homeBottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                HideKeyboard(homeActivity)
            }
            BackHandler(homeBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                coroutineScope.launch {
                    if (bottomHomeViewModel.isBottomSheetOpen) {
                        homeBottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        homeBottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                    bottomHomeViewModel.isBottomSheetOpen = false
                }
            }
            if (bottomHomeViewModel.isErrorOccurredPregnancyMilestone && bottomHomeViewModel.isErrorOccurredNearestMilestone && bottomHomeViewModel.isErrorOccurredHomeScreenQuestion && bottomHomeViewModel.isErrorOccurredIntendedParentQuestion) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (!bottomHomeViewModel.isErrorOccurredPregnancyMilestone) {
                        if (bottomHomeViewModel.isPregnancyDataLoaded) {
                            if (bottomHomeViewModel.isQuestionDataEmpty) {
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
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(start = 18.dp)) {
                                            Text(
                                                modifier = Modifier
                                                    .padding(top = 25.dp),
                                                text = bottomHomeViewModel.pregnancyTittle,
                                                color = Color.Black,
                                                style = MaterialTheme.typography.body2,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W800,
                                                lineHeight = 24.sp
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .width(225.dp)
                                                    .padding(top = 10.dp, bottom = 10.dp),
                                                text = bottomHomeViewModel.pregnancyDescription,
                                                style = MaterialTheme.typography.body2,
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 22.sp
                                            )
                                        }
                                        Column(modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.End) {
                                            GlideImage(
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(110.dp)
                                                    .padding(top = 10.dp, start = 24.dp),
                                                imageModel = bottomHomeViewModel.pregnancyImageUrl,
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                            } else if (!bottomHomeViewModel.isQuestionDataEmpty) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.White,
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 24.dp, top = 30.dp)
                                ) {
                                    Column {
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 24.dp,
                                                top = 19.dp,
                                                end = 24.dp
                                            ),
                                            text = bottomHomeViewModel.homeScreenImportantQuestion,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.body2,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W800,
                                            lineHeight = 24.sp
                                        )
                                        Button(
                                            onClick = {
                                                bottomHomeViewModel.homeScreenQuestionAns = ""
                                                bottomHomeViewModel.upperQuestion = true
                                                bottomHomeViewModel.isBottomSheetOpen = true
                                                coroutineScope.launch {
                                                    bottomHomeViewModel.isHomeScreenQuestionAnsEmpty =
                                                        false
                                                    if (homeBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                                        homeBottomSheetScaffoldState.bottomSheetState.collapse()
                                                        bottomHomeViewModel.isBottomSheetOpen = true
                                                    } else {
                                                        homeBottomSheetScaffoldState.bottomSheetState.expand()
                                                        bottomHomeViewModel.isBottomSheetOpen =
                                                            false
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
                                                backgroundColor = Custom_Blue,
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.answer_the_question),
                                                color = Color.White,
                                                style = MaterialTheme.typography.body2,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W600,
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            PregnancyMilestoneShimmerAnimation()
                        }
                    }
                    if (!bottomHomeViewModel.isErrorOccurredNearestMilestone) {
                        if (bottomHomeViewModel.isNearestMilestoneDataLoaded) {
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
                                    .padding(
                                        start = 24.dp,
                                        end = 24.dp,
                                        top = 16.dp,
                                    )
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (bottomHomeViewModel.nearestMilestoneImage.isEmpty()) {
                                            Image(
                                                modifier = Modifier
                                                    .width(180.dp)
                                                    .height(180.dp)
                                                    .padding(
                                                        top = 14.dp,
                                                        start = 24.dp,
                                                        bottom = 24.dp,
                                                        end = 24.dp
                                                    ),
                                                painter = painterResource(id = R.drawable.img_user_add_new_milestone),
                                                contentDescription = stringResource(id = R.string.content_description),
                                                contentScale = ContentScale.FillBounds
                                            )
                                        } else {
                                            val painter = rememberImagePainter(
                                                bottomHomeViewModel.nearestMilestoneImage,
                                                builder = {
                                                    placeholder(R.drawable.ic_baseline_place_holder_image_24)
                                                })
                                            Image(
                                                modifier = Modifier
                                                    .width(180.dp)
                                                    .height(180.dp),
                                                painter = painter,
                                                contentDescription = stringResource(id = R.string.content_description),
                                            )
                                        }

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
                                            contentDescription = stringResource(id = R.string.content_description),
                                        )
                                        val getTimeZoneLong: DateFormat =
                                            SimpleDateFormat("zzzz", Locale.US)
                                        val timeZoneLong =
                                            getTimeZoneLong.format(Calendar.getInstance().time)
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 8.dp,
                                                top = 17.dp
                                            ),
                                            text = if (bottomHomeViewModel.nearestMilestoneDate.isNotEmpty()) bottomHomeViewModel.nearestMilestoneDate + " " + stringResource(
                                                id = R.string.date_time_concat) + " " + bottomHomeViewModel.nearestMilestoneTime + " " + timeZoneLong else "N/A",
                                            color = Color(0xFF9F9D9B),
                                            style = MaterialTheme.typography.body2,
                                            fontWeight = FontWeight.W400,
                                            lineHeight = 16.sp,
                                            fontSize = 13.sp
                                        )
                                    }
                                    if (bottomHomeViewModel.nearestMilestoneDate.isEmpty() && type == Constants.PARENT) {
//                                        if (!bottomHomeViewModel.isSurrogateAsked) {
                                        Button(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, end = 24.dp, bottom = 10.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, Color(0xFFF4F4F4)),
                                            onClick = {
                                                if (bottomHomeViewModel.isSurrogateAsked) {
                                                    bottomHomeViewModel.askSurrogate(
                                                        AskSurrogateRequest(
                                                            user_id = userId,
                                                            title = bottomHomeViewModel.nearestMilestoneTittle,
                                                            milestone_id = bottomHomeViewModel.nearestMilestoneId
                                                        )
                                                    )
                                                    bottomHomeViewModel.askSurrogate.observe(
                                                        homeActivity) {
                                                        if (it != null) {
                                                            handleAskSurrogateData(
                                                                result = it,
                                                                user_id = userId,
                                                                type = type,
                                                                partnerId = partnerId,
                                                                homeActivity = homeActivity,
                                                                bottomHomeViewModel = bottomHomeViewModel
                                                            )
                                                        }
                                                    }
                                                }
                                            },
                                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                                pressedElevation = 0.dp,
                                                hoveredElevation = 0.dp,
                                                disabledElevation = 0.dp,
                                                focusedElevation = 0.dp),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                                            Text(text = stringResource(id = R.string.ask_surrogate),
                                                style = MaterialTheme.typography.body1,
                                                fontWeight = FontWeight.W600,
                                                fontSize = 16.sp,
                                                color = Custom_Blue,
                                                textAlign = TextAlign.Center)
//                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            NearestMilestoneShimmerAnimation()
                        }
                    }
                    if (!bottomHomeViewModel.isErrorOccurredHomeScreenQuestion) {
                        if (bottomHomeViewModel.isHomeScreenQuestionDataLoaded) {
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
                                        bottom = if (bottomHomeViewModel.isIntendedParentQuestionDataLoaded) 0.dp else 70.dp
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
                                            bottomHomeViewModel.homeScreenQuestionAns = ""
                                            bottomHomeViewModel.upperQuestion = false
                                            bottomHomeViewModel.isBottomSheetOpen = true
                                            coroutineScope.launch {
                                                bottomHomeViewModel.isHomeScreenQuestionAnsEmpty =
                                                    false
                                                if (homeBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                                    homeBottomSheetScaffoldState.bottomSheetState.collapse()
                                                    bottomHomeViewModel.isBottomSheetOpen = true
                                                } else {
                                                    homeBottomSheetScaffoldState.bottomSheetState.expand()
                                                    bottomHomeViewModel.isBottomSheetOpen =
                                                        false
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
                        } else {
                            HomeScreenQuestionShimmerAnimation(isTittleAvailable = true)
                        }
                    }
                    if (!bottomHomeViewModel.isErrorOccurredIntendedParentQuestion) {
                        if (bottomHomeViewModel.isIntendedParentQuestionDataLoaded) {
                            Text(
                                modifier = Modifier.padding(start = 24.dp, top = 44.dp),
                                text = stringResource(id = if (type == Constants.PARENT) R.string.bottom_home_screen_your_surrogate_mother else R.string.bottom_home_screen_your_intended_parents),
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
                                    .padding(
                                        start = 25.dp,
                                        end = 23.dp,
                                        top = 16.dp,
                                        bottom = 70.dp
                                    )
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
                                            modifier = Modifier.padding(
                                                start = 24.dp,
                                                top = 10.dp
                                            ),
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
                                            text = "${bottomHomeViewModel.intendedParentDays} days ago",
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
                        } else {
                            IntendedParentsShimmerAnimation()
                        }
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    )
}

fun handleAskSurrogateData(
    result: NetworkResult<CommonResponse>,
    user_id: Int,
    type: String,
    partnerId: Int,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.i("TAG", result.message.toString())
            Toast.makeText(homeActivity, "Notification sent successfully", Toast.LENGTH_SHORT)
                .show()
            updateNearestMilestone(
                userId = user_id,
                type = type,
                partnerId = partnerId,
                homeActivity = homeActivity,
                bottomHomeViewModel = bottomHomeViewModel
            )
        }
        is NetworkResult.Error -> {
            // show error message
        }
    }
}

@Composable
fun HideKeyboard(activity: Activity) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_PAUSE) {
                    val imm: InputMethodManager =
                        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    var view = activity.currentFocus
                    if (view == null) {
                        view = View(activity)
                    }
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}

fun updateHomeScreenData(
    bottomHomeViewModel: BottomHomeViewModel,
    userId: Int,
    type: String,
    homeActivity: HomeActivity,
    partnerId: Int,
    context: Context,
) {
    bottomHomeViewModel.getPregnancyMilestone(
        GetPregnancyMilestoneRequest(
            user_id = userId,
            type = type
        )
    )
    updateNearestMilestone(
        userId = userId,
        type = type,
        partnerId = partnerId,
        homeActivity = homeActivity,
        bottomHomeViewModel = bottomHomeViewModel
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
            handleGetImportantQuestionData(
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

fun updateNearestMilestone(
    userId: Int,
    type: String,
    partnerId: Int,
    homeActivity: HomeActivity,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    bottomHomeViewModel.getNearestMilestone(
        GetNearestMilestoneRequest(
            user_id = userId,
            type = type,
            partner_id = partnerId
        )
    )
    bottomHomeViewModel.getNearestMilestoneResponse.observe(homeActivity) {
        if (it != null) {
            handleNearestMilestoneData(
                result = it,
                bottomHomeViewModel = bottomHomeViewModel,
            )
        }
    }
}

fun getHomeScreenQuestion(
    user_id: Int,
    type: String,
    bottomHomeViewModel: BottomHomeViewModel,
    homeActivity: HomeActivity,
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
    homeActivity: HomeActivity,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            bottomHomeViewModel.isHomeScreenQuestionAnswered = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.i("TAG", result.message.toString())
            getHomeScreenQuestion(user_id, type, bottomHomeViewModel, homeActivity)
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
            bottomHomeViewModel.answerList.clear()
        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun handleStoreAnsImportantQuestion(
    result: NetworkResult<CommonResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    userId: Int,
    type: String,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            bottomHomeViewModel.isHomeScreenQuestionAnswered = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            bottomHomeViewModel.getPregnancyMilestone(
                GetPregnancyMilestoneRequest(
                    user_id = userId,
                    type = type
                )
            )
            bottomHomeViewModel.homeScreenQuestionAns = ""
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isHomeScreenQuestionAnswered = false
            coroutineScope.launch {
                if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
        }
    }
}

private fun handleGetImportantQuestionData(
    result: NetworkResult<GetImportantQuestionResponse>,
    bottomHomeViewModel: BottomHomeViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            bottomHomeViewModel.isPregnancyDataLoaded = false
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isErrorOccurredPregnancyMilestone = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.i("TAG", result.message.toString())
            if (result.data?.question == null) {
                bottomHomeViewModel.isQuestionDataEmpty = true
                bottomHomeViewModel.pregnancyTittle =
                    result.data?.pregnancy_milestone?.title.toString()
                bottomHomeViewModel.pregnancyDescription =
                    result.data?.pregnancy_milestone?.description.toString()
                bottomHomeViewModel.pregnancyImageUrl =
                    result.data?.pregnancy_milestone?.image.toString()
            } else {
                bottomHomeViewModel.homeScreenImportantQuestionId = result.data.question.id
                bottomHomeViewModel.homeScreenImportantQuestion = result.data.question.question
                bottomHomeViewModel.isQuestionDataEmpty = false
                bottomHomeViewModel.parentList.clear()
                result.data.user_name.forEach {
                    if (it != null)
                        bottomHomeViewModel.parentList.add(it)
                }
            }
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isPregnancyDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false

        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isPregnancyDataLoaded = false
            bottomHomeViewModel.isErrorOccurredPregnancyMilestone = true
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
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isErrorOccurredHomeScreenQuestion = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false

            if (result.data?.data?.category_id == null || result.data.data.id == null || result.data.data.question == "") {
                bottomHomeViewModel.homeScreenQuestionCategeryId = 0
                bottomHomeViewModel.homeScreenQuestionId = 0
                bottomHomeViewModel.isHomeScreenQuestionDataLoaded = false
                bottomHomeViewModel.isErrorOccurredHomeScreenQuestion = true
            } else {
                bottomHomeViewModel.isHomeScreenQuestionDataLoaded = true
                bottomHomeViewModel.homeScreenQuestionCategeryId = result.data.data.category_id
                bottomHomeViewModel.homeScreenQuestionId = result.data.data.id
                bottomHomeViewModel.homeScreenLatestQuestion = result.data.data.question
                bottomHomeViewModel.homeScreenQuestionAns = ""
                bottomHomeViewModel.parentList.clear()
                result.data.user_name?.forEach {
                    if (it != null)
                        bottomHomeViewModel.parentList.add(it)
                }
            }
        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isHomeScreenQuestionDataLoaded = false
            bottomHomeViewModel.isErrorOccurredHomeScreenQuestion = true
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
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = false
            bottomHomeViewModel.isErrorOccurredNearestMilestone = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.i("TAG", result.message.toString())
            bottomHomeViewModel.nearestMilestoneTittle = result.data?.nearest_milestone?.title!!
            Log.d("TAG", "d: ${result.data.nearest_milestone.date}")
            if (result.data.nearest_milestone.date?.isNotEmpty() == true) {
                val dateTime = changeLocalFormat(result.data.nearest_milestone.date,
                    Constants.DATE_FORMAT_UTC,
                    Constants.DATE_FORMAT_LOCAL)
                Log.d("TAG", "handleNearestMilestoneData: $dateTime")
                val localDate = dateTime?.let { changeLocalDateFormat(it.trim()) }
                val localTime = dateTime?.let { changeLocalTimeFormat(it.trim()) }
                bottomHomeViewModel.nearestMilestoneDate = ""
                if (localDate != null) {
                    bottomHomeViewModel.nearestMilestoneDate = localDate
                    Log.d("TAG",
                        "handleNearestMilestoneData: ${TimeZone.getTimeZone(result.data.nearest_milestone.date)}")
                } else {
                    bottomHomeViewModel.nearestMilestoneDate = ""
                }
                if (localTime != null) {
                    bottomHomeViewModel.nearestMilestoneTime = localTime
                } else {
                    bottomHomeViewModel.nearestMilestoneTime = ""
                }
            }
            if (result.data.nearest_milestone.milestone_image == null)
                bottomHomeViewModel.nearestMilestoneImage = ""
            else
                bottomHomeViewModel.nearestMilestoneImage =
                    result.data.nearest_milestone.milestone_image
            bottomHomeViewModel.isSurrogateAsked = result.data.state
            if (result.data.nearest_milestone.milestone_id != null) {
                bottomHomeViewModel.nearestMilestoneId = result.data.nearest_milestone.milestone_id
            }
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = true
        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isNearestMilestoneDataLoaded = false
            bottomHomeViewModel.isErrorOccurredNearestMilestone = true
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
            bottomHomeViewModel.isAllDataLoaded = true
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = false
            bottomHomeViewModel.isErrorOccurredIntendedParentQuestion = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            bottomHomeViewModel.intendedParentQuestion = result.data?.data?.question!!
            bottomHomeViewModel.intendedParentUserName = result.data.user_name
            bottomHomeViewModel.intendedParentAnswer = result.data.data.answer
            bottomHomeViewModel.intendedParentDays = result.data.day
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = false
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = true

        }
        is NetworkResult.Error -> {
            // show error message
            bottomHomeViewModel.isAllDataLoaded = false
            bottomHomeViewModel.isErrorOccurred = true
            bottomHomeViewModel.isIntendedParentQuestionDataLoaded = false
            bottomHomeViewModel.isErrorOccurredIntendedParentQuestion = true
        }
    }
}


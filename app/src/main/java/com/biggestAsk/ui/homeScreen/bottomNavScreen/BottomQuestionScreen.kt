package com.biggestAsk.ui.homeScreen.bottomNavScreen

import android.content.Context
import android.text.TextUtils
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
import com.biggestAsk.data.model.request.StoreBaseScreenQuestionAnsRequest
import com.biggestAsk.data.model.response.*
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.homeScreen.bottomNavScreen.dropDown.selectionChangeDropDown
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.HomeScreenQuestionShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.QuestionBankContentShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.QuestionScreenAnsweredQuestionShimmerAnimation
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.SelectFrequencyShimmerAnimation
import com.biggestAsk.ui.main.viewmodel.BottomQuestionViewModel
import com.biggestAsk.ui.main.viewmodel.FrequencyViewModel
import com.biggestAsk.ui.main.viewmodel.YourAccountViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.biggestAsk.ui.ui.theme.Text_Color
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomQuestionScreen(
    questionViewModel: BottomQuestionViewModel, context: Context,
    homeActivity: HomeActivity,
    yourAccountViewModel: YourAccountViewModel,
    frequencyViewModel: FrequencyViewModel,
) {
    val suggestions =
        listOf("every day", "every 3 days", "every week")
    val questionBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val provider = PreferenceProvider(context)
    val userId = provider.getIntValue(Constants.USER_ID, 0)
    val type = provider.getValue(Constants.TYPE, "")
    val selectedText = remember { mutableStateOf("") }
    val selectedUser = remember { mutableStateOf("") }
    val partnerId = provider.getIntValue(Constants.PARTNER_ID, 0)
    LaunchedEffect(Unit) {
        yourAccountViewModel.getAnsweredQuestionListResponse.observe(homeActivity) {
            if (it != null) {
                handleQuestionAnswerList(
                    result = it,
                    questionViewModel = questionViewModel
                )
            }
        }
        questionViewModel.getHomeScreenQuestionResponse.observe(homeActivity) {
            if (it != null) {
                handleQuestionData(result = it, questionViewModel = questionViewModel)
            }
        }
        questionViewModel.questionBankContentResponse.observe(homeActivity) {
            if (it != null) {
                handleQuestionBankContentData(result = it, questionViewModel = questionViewModel)
            }
        }
        questionViewModel.getFrequencyResponse.observe(homeActivity) {
            if (it != null) {
                handleFrequencyData(result = it, questionViewModel = questionViewModel)
            }
        }
        updateQuestionScreen(
            userId = userId,
            type = type,
            yourAccountViewModel = yourAccountViewModel,
            questionViewModel = questionViewModel,
            homeActivity = homeActivity
        )

    }
    BottomSheetScaffold(scaffoldState = questionBottomSheetScaffoldState, sheetContent = {
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
                text = questionViewModel.questionScreenLatestQuestion,
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
                value = questionViewModel.questionScreenQuestionAnswer,
                onValueChange = {
                    questionViewModel.questionScreenQuestionAnswer = it
                    questionViewModel.isAnswerEmpty = false
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
                        text = stringResource(id = R.string.add_answer),
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
            if (questionViewModel.isAnswerEmpty) {
                Text(
                    text = stringResource(id = R.string.bottom_home_screen_question_ans_empty_text),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 13.dp),
                    fontSize = 12.sp
                )
            }
            if (type == Constants.PARENT) {
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
                selectedUser.value = SimpleDropDown(
                    suggestions = questionViewModel.questionParentList,
                    hint = stringResource(id = R.string.hint_drop_down),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (TextUtils.isEmpty(questionViewModel.questionScreenQuestionAnswer)) {
                                questionViewModel.isAnswerEmpty = true
                            } else {
                                questionViewModel.answerList.add(
                                    Answer(
                                        answer = questionViewModel.questionScreenQuestionAnswer,
                                        question_id = questionViewModel.questionScreenQuestionId,
                                    )
                                )
                                questionViewModel.storeQuestionScreenAnswer(
                                    storeBaseScreenQuestionAnsRequest = StoreBaseScreenQuestionAnsRequest(
                                        answer = questionViewModel.answerList,
                                        category_id = questionViewModel.questionScreenQuestionCategeryId,
                                        partner_id = partnerId.toString(),
                                        type = type!!,
                                        user_id = userId,
                                        user_name = selectedUser.value
                                    )
                                )
                                questionViewModel.storeAnsImportantQuestionResponse.observe(
                                    homeActivity
                                ) {
                                    if (it != null) {
                                        handleStoreAnsImportantQuestion(
                                            result = it,
                                            questionViewModel = questionViewModel,
                                            coroutineScope = coroutineScope,
                                            bottomSheetScaffoldState = questionBottomSheetScaffoldState,
                                            userId = userId,
                                            type = type,
                                            yourAccountViewModel = yourAccountViewModel,
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
    }, content = {
        if (questionBottomSheetScaffoldState.bottomSheetState.isCollapsed) {
            HideKeyboard(homeActivity)
        }
        BackHandler(questionBottomSheetScaffoldState.bottomSheetState.isExpanded) {
            coroutineScope.launch {
                if (questionViewModel.isBottomSheetOpened) {
                    questionBottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    questionBottomSheetScaffoldState.bottomSheetState.collapse()
                }
                questionViewModel.isBottomSheetOpened = false
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            if (questionViewModel.isQuestionBankContentLoaded) {
                QuestionBankContentShimmerAnimation()
            } else {
                if (!questionViewModel.isErrorOccurredQuestionBankContent) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 30.dp),
                        text = stringResource(id = R.string.bottom_ques_screen_desc),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 27.sp
                    )
                }
            }
            if (questionViewModel.isFrequencyDataLoading || frequencyViewModel.isLoading) {
                SelectFrequencyShimmerAnimation()
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                    text = stringResource(id = R.string.bottom_ques_new_ques_pro),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W900,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp),
                    text = stringResource(id = R.string.bottom_ques_freq_ques_pro),
                    style = MaterialTheme.typography.body2,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W800
                )
                selectedText.value = selectionChangeDropDown(
                    suggestions = suggestions,
                    hint = stringResource(id = R.string.frequency),
                    modifier = Modifier
                        .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    frequencyViewModel = frequencyViewModel,
                    context = context,
                    homeActivity = homeActivity,
                    isFrequencyChanged = selectedText.value != "",
                    text = questionViewModel.frequency,
                    questionViewModel = questionViewModel
                )
            }
            if (questionViewModel.isAnsweredQuestionLoading) {
                HomeScreenQuestionShimmerAnimation()
            } else {
                if (!questionViewModel.isErrorOccurredInQuestionLoading) {
                    if (questionViewModel.questionScreenLatestQuestion != "") {
                        Surface(
                            shape = RoundedCornerShape(15.dp),
                            color = Color(0xFF4479CC),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 40.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 37.dp, top = 24.dp, end = 36.dp),
                                    text = stringResource(id = R.string.new_question),
                                    color = Color.White,
                                    style = MaterialTheme.typography.body2,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, top = 6.dp),
                                    text = questionViewModel.questionScreenLatestQuestion,
                                    color = Color.White,
                                    style = MaterialTheme.typography.body2,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = {
                                        questionViewModel.isBottomSheetOpened = true
                                        coroutineScope.launch {
                                            questionViewModel.questionScreenQuestionAnswer = ""
                                            questionViewModel.isAnswerEmpty = false
                                            if (questionBottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                                questionBottomSheetScaffoldState.bottomSheetState.collapse()
                                                questionViewModel.isBottomSheetOpened = true
                                            } else {
                                                questionBottomSheetScaffoldState.bottomSheetState.expand()
                                                questionViewModel.isBottomSheetOpened = false
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(
                                            start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp
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
                                        text = stringResource(id = R.string.answer_the_question),
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
                }
            }
            if (questionViewModel.isAnsweredQuestionLoading) {
                QuestionScreenAnsweredQuestionShimmerAnimation()
            } else {
                if (!questionViewModel.isErrorOccurredInQuestionLoading) {
                    if (questionViewModel.questionAnsweredList.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 10.dp),
                            text = stringResource(id = R.string.bottom_ques_exist_ques),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        questionViewModel.questionAnsweredList.forEachIndexed { index, item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                elevation = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp, end = 23.dp, top = 6.dp, bottom = 15.dp)
                            ) {
                                Column {
                                    item.question?.let { it1 ->
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                                            text = it1,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.body2,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                    Row {
                                        item.user_name?.let { it1 ->
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 24.dp,
                                                    top = 10.dp
                                                ),
                                                text = it1,
                                                color = Custom_Blue,
                                                style = MaterialTheme.typography.body2,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp, end = 24.dp),
                                            text = if (questionViewModel.questionAnsweredDaysList[index] == 0) "Today" else "${questionViewModel.questionAnsweredDaysList[index]} Day ago",
                                            color = Color(0xFF9F9D9B),
                                            style = MaterialTheme.typography.body2,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    item.answer?.let { it1 ->
                                        Text(
                                            modifier = Modifier.padding(
                                                start = 24.dp,
                                                top = 4.dp,
                                                bottom = 22.dp
                                            ),
                                            text = it1,
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
                }
            }
        }
    }, sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
}


fun updateQuestionScreen(
    userId: Int,
    type: String?,
    yourAccountViewModel: YourAccountViewModel,
    questionViewModel: BottomQuestionViewModel,
    homeActivity: HomeActivity,
) {
    yourAccountViewModel.getYourAccountAnsweredQuestionList(userId = userId, type = type!!)
    questionViewModel.getHomeScreenQuestion(
        GetPregnancyMilestoneRequest(
            user_id = userId,
            type = type
        )
    )
    updateFrequency(
        userId = userId,
        type = type,
        questionViewModel = questionViewModel,
    )
    questionViewModel.getQuestionBankContent()

}

fun updateFrequency(
    userId: Int,
    type: String,
    questionViewModel: BottomQuestionViewModel,
) {
    questionViewModel.getFrequency(user_id = userId, type = type)

}

fun handleQuestionBankContentData(
    result: NetworkResult<QuestionBankContentResponse>,
    questionViewModel: BottomQuestionViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            questionViewModel.isQuestionBankContentLoaded = true
            questionViewModel.isErrorOccurredQuestionBankContent = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            questionViewModel.isQuestionBankContentLoaded = false
            questionViewModel.isErrorOccurredQuestionBankContent = false
            questionViewModel.questionBankInfo = result.data?.question_bank?.get(0)?.info.toString()
        }
        is NetworkResult.Error -> {
            // show error message
            questionViewModel.isQuestionBankContentLoaded = false
            questionViewModel.isErrorOccurredQuestionBankContent = true
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
private fun handleStoreAnsImportantQuestion(
    result: NetworkResult<CommonResponse>,
    questionViewModel: BottomQuestionViewModel,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    userId: Int,
    type: String,
    yourAccountViewModel: YourAccountViewModel,
    homeActivity: HomeActivity,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            questionViewModel.isQuestionScreenQuestionAnswered = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            questionViewModel.questionScreenQuestionAnswer = ""
            questionViewModel.questionScreenLatestQuestion = ""
            questionViewModel.answerList.clear()
            questionViewModel.isQuestionScreenQuestionAnswered = false
            updateQuestionScreen(
                userId = userId,
                yourAccountViewModel = yourAccountViewModel,
                type = type,
                questionViewModel = questionViewModel,
                homeActivity = homeActivity
            )
        }
        is NetworkResult.Error -> {
            // show error message
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
            questionViewModel.isQuestionScreenQuestionAnswered = false
        }
    }
}

fun handleQuestionAnswerList(
    result: NetworkResult<GetAnsweredQuestionListResponse>,
    questionViewModel: BottomQuestionViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            questionViewModel.isAnsweredQuestionLoading = true
            questionViewModel.isErrorOccurredInQuestionLoading = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            questionViewModel.isAnsweredQuestionLoading = false
            questionViewModel.isErrorOccurredInQuestionLoading = false
            questionViewModel.questionAnsweredList.clear()
            questionViewModel.questionAnsweredDaysList.clear()
            result.data?.data?.let { questionViewModel.questionAnsweredList.addAll(it) }
            result.data?.days?.let { questionViewModel.questionAnsweredDaysList.addAll(it) }
        }
        is NetworkResult.Error -> {
            // show error message
            questionViewModel.isAnsweredQuestionLoading = false
            questionViewModel.isErrorOccurredInQuestionLoading = true
        }
    }
}

private fun handleQuestionData(
    result: NetworkResult<GetHomeScreenQuestionResponse>,
    questionViewModel: BottomQuestionViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            questionViewModel.isQuestionScreenQuestionDataLoaded = false
            questionViewModel.isErrorOccurredQuestionScreenQuestion = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            if (result.data?.data?.category_id == null || result.data.data.question == "") {
                questionViewModel.questionScreenLatestQuestion = ""
                questionViewModel.questionScreenQuestionCategeryId = 0
                questionViewModel.questionScreenQuestionId = 0
                questionViewModel.isQuestionScreenQuestionDataLoaded = false
                questionViewModel.isErrorOccurredQuestionScreenQuestion = true
            } else {
                questionViewModel.isQuestionScreenQuestionDataLoaded = true
                questionViewModel.questionScreenQuestionCategeryId = result.data.data.category_id
                questionViewModel.questionScreenQuestionId = result.data.data.id
                questionViewModel.questionScreenLatestQuestion = result.data.data.question
                questionViewModel.questionParentList.clear()
                if (result.data.user_name.isNotEmpty()) {
                    questionViewModel.questionParentList.addAll(result.data.user_name)
                }
            }
        }
        is NetworkResult.Error -> {
            // show error message
            questionViewModel.isQuestionScreenQuestionDataLoaded = false
            questionViewModel.isErrorOccurredQuestionScreenQuestion = true
        }
    }
}

private fun handleFrequencyData(
    result: NetworkResult<GetFrequencyResponse>,
    questionViewModel: BottomQuestionViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            questionViewModel.isFrequencyDataLoading = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            questionViewModel.isFrequencyDataLoading = false
            questionViewModel.frequency = result.data?.data?.get(0)?.ques_type!!
        }
        is NetworkResult.Error -> {
            // show error message
            questionViewModel.isFrequencyDataLoading = false
        }
    }
}
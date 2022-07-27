package com.biggestAsk.ui.questionScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.data.model.request.ScreenQuestionStatusRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun QuestionScreenF(
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity,
) {
    val context = LocalContext.current
//    LaunchedEffect(Unit) {
//        dataStoreManager
//            .getLoginDetails()
//            .catch { e ->
//                e.printStackTrace()
//            }
//            .collect {
//                Log.i("TAG", "QuestionScreen: $it")
//            }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(end = 10.dp, top = 50.dp),
                        painter = painterResource(id = R.drawable.ic_img_question_screen_logo),
                        contentDescription = "",
                        contentScale = ContentScale.FillHeight
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 40.dp,
                                start = 68.dp,
                                end = 68.dp
                            ),
                        text = stringResource(id = R.string.question_tv_tittle_text),
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                modifier = Modifier,
                                selected = homeViewModel.selectedValueEveryDayRb.value,
                                onClick = {
                                    homeViewModel.selectedValueEveryDayRb.value = true
                                    homeViewModel.selectedValueEvery3DaysRb.value = false
                                    homeViewModel.selectedValueEveryWeekRb.value = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Custom_Blue
                                )
                            )
                            Text(
                                modifier = Modifier.padding(top = 15.dp),
                                text = stringResource(id = R.string.question_rb_everyday)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                modifier = Modifier,
                                selected = homeViewModel.selectedValueEvery3DaysRb.value,
                                onClick = {
                                    homeViewModel.selectedValueEvery3DaysRb.value = true
                                    homeViewModel.selectedValueEveryDayRb.value = false
                                    homeViewModel.selectedValueEveryWeekRb.value = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Custom_Blue
                                )
                            )
                            Text(
                                modifier = Modifier.padding(top = 15.dp),
                                text = stringResource(id = R.string.question_rb_3_days)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                modifier = Modifier,
                                selected = homeViewModel.selectedValueEveryWeekRb.value,
                                onClick = {
                                    homeViewModel.selectedValueEveryWeekRb.value = true
                                    homeViewModel.selectedValueEvery3DaysRb.value = false
                                    homeViewModel.selectedValueEveryDayRb.value = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Custom_Blue
                                )
                            )
                            Text(
                                modifier = Modifier.padding(top = 15.dp),
                                text = stringResource(id = R.string.question_rb_week)
                            )
                        }
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 20.dp, end = 20.dp)
                .align(Alignment.BottomCenter),
            enabled = true,
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
            ),
            onClick = {
                frequencySubmitApiCall(context, homeViewModel, mainActivity)
            }) {
            Text(
                text = stringResource(id = R.string.question_btn_text_submit_frequency),
                color = Color.White,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W900,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
    if (homeViewModel.isLoading) {
        ProgressBarTransparentBackground("Please wait....")
    }
}

private fun frequencySubmitApiCall(
    context: Context,
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity
) {
    val provider = PreferenceProvider(context)
    val type = provider.getValue("type", "")
    val userId = provider.getIntValue("user_id", 0)
    val frequency =
        if (homeViewModel.selectedValueEveryDayRb.value) "everyday" else if (homeViewModel.selectedValueEvery3DaysRb.value) "every_3_day" else "every_week"
    Log.d("TAG", "QuestionScreenF: $frequency")
    homeViewModel.screenQuestionStatus(
        ScreenQuestionStatusRequest(
            type = type!!,
            user_id = userId,
            question_type = frequency
        )
    )
    homeViewModel.screenQuestionStatus.observe(mainActivity) {
        if (it != null) {
            handleUserData(
                result = it,
                homeViewModel = homeViewModel,
                context = context
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun handleUserData(
    result: NetworkResult<CommonResponse>,
    homeViewModel: HomeViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            homeViewModel.isLoading = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            homeViewModel.isLoading = false
            GlobalScope.launch {
                result.data?.let {
                    context.findActivity()?.finish()
                    context.startActivity(
                        Intent(
                            context,
                            HomeActivity::class.java
                        )
                    )
                }
            }
            val provider = PreferenceProvider(context)
            provider.setValue(
                Constants.LOGIN_STATUS,
                LoginStatus.PARTNER_NOT_ASSIGN.name.lowercase(Locale.getDefault())
            )
        }
        is NetworkResult.Error -> {
            // show error message
            homeViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

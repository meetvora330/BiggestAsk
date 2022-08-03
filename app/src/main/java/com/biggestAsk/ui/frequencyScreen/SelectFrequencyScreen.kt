package com.biggestAsk.ui.frequencyScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.data.model.request.ScreenQuestionStatusRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.homeScreen.bottomNavScreen.simpleDropDown
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.FrequencyViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun SelectFrequencyScreen(
    frequencyViewModel: FrequencyViewModel,
    mainActivity: MainActivity,
) {
    val context = LocalContext.current
    val suggestions =
        listOf("Every day", "Every 3 days", "Every week")
    var selectedText by remember { mutableStateOf("") }
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
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 20.dp),
                            text = stringResource(id = R.string.bottom_ques_screen_desc),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 27.sp
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                            text = stringResource(id = R.string.bottom_ques_freq_ques_pro),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W900,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        selectedText = simpleDropDown(
                            suggestions = suggestions,
                            hint = stringResource(id = R.string.frequency_drop_down_hint_day),
                            modifier = Modifier.padding(top = 17.dp, start = 24.dp, end = 24.dp),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        )
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
                frequencySubmitApiCall(
                    context,
                    frequencyViewModel,
                    mainActivity,
                    selectedText = selectedText
                )
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
    if (frequencyViewModel.isLoading) {
        ProgressBarTransparentBackground("Please wait....")
    }
}

private fun frequencySubmitApiCall(
    context: Context,
    frequencyViewModel: FrequencyViewModel,
    mainActivity: MainActivity,
    selectedText: String
) {
    val provider = PreferenceProvider(context)
    val type = provider.getValue("type", "")
    val userId = provider.getIntValue("user_id", 0)
    val frequency =
        if (frequencyViewModel.selectedValueEveryDayRb.value) "everyday" else if (frequencyViewModel.selectedValueEvery3DaysRb.value) "every_3_day" else "every_week"
    Log.d("TAG", "QuestionScreenF: $frequency")
    frequencyViewModel.screenQuestionStatus(
        ScreenQuestionStatusRequest(
            type = type!!,
            user_id = userId,
            question_type = selectedText
        )
    )
    frequencyViewModel.screenQuestionStatus.observe(mainActivity) {
        if (it != null) {
            handleUserData(
                result = it,
                frequencyViewModel = frequencyViewModel,
                context = context
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun handleUserData(
    result: NetworkResult<CommonResponse>,
    frequencyViewModel: FrequencyViewModel,
    context: Context,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            frequencyViewModel.isLoading = true
            Log.e("TAG", "handleUserData() --> Loading  $result")
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            Log.i("TAG", result.message.toString())
            frequencyViewModel.isLoading = false
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
            frequencyViewModel.isLoading = false
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
        }
    }
}

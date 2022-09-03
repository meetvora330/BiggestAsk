package com.biggestAsk.ui.frequencyScreen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.activity.MainActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.ET_Bg
import com.example.biggestAsk.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun QuestionScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    mainActivity: MainActivity
) {
    val viewModel = MainViewModel()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        homeViewModel.getBaseScreenQuestion()
    }
    homeViewModel.baseScreenQuestion.observe(mainActivity) {
        when (it) {
            is NetworkResult.Loading -> {
                // show a progress bar
                homeViewModel.isLoading = true
                Log.e("TAG", "handleUserData() --> Loading  $it")
            }
            is NetworkResult.Success -> {
                // bind data to the view
                Log.e("TAG", "handleUserData() --> Success  $it")
                homeViewModel.isLoading = false
                homeViewModel.valueStateList.clear()
                it.data?.let { it1 -> homeViewModel.valueStateList.addAll(it1) }
            }
            is NetworkResult.Error -> {
                // show error message
                homeViewModel.isLoading = false
                Log.e("TAG", "handleUserData() --> Error ${it.message}")
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .fillMaxSize(), contentPadding = PaddingValues(bottom = 135.dp)
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
                    homeViewModel.valueStateList.forEachIndexed { index, _ ->
                        QuestionList(
                            viewModel = viewModel,
                            index = index,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .align(Alignment.BottomCenter)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 20.dp, end = 10.dp),
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
                    context.findActivity()?.finish()
                    context.startActivity(
                        Intent(
                            context,
                            HomeActivity::class.java
                        )
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
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 20.dp, end = 10.dp),
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
                    backgroundColor = Color(0xFF9F9D9B),
                ),
                onClick = {
                    context.findActivity()?.finish()
                    context.startActivity(
                        Intent(
                            context,
                            HomeActivity::class.java
                        )
                    )
                }) {
                Text(
                    text = stringResource(id = R.string.question_btn_text_skip),
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W900,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
        }
    }
    if (homeViewModel.isLoading) {
        ProgressBarTransparentBackground(stringResource(id = R.string.please_wait))
    }
}

@Composable
fun QuestionList(
    viewModel: MainViewModel,
    index: Int,
    homeViewModel: HomeViewModel
) {
    val focusManager = LocalFocusManager.current
    var answer by remember { mutableStateOf(homeViewModel.valueStateList[index].answer) }
    var ans by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            elevation = 12.dp,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 34.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier
                            .padding(top = 18.dp, start = 16.dp)
                            .width(40.dp)
                            .height(40.dp),
                        painter = painterResource(id = R.drawable.ic_img_question_card_logo),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp, top = 27.dp),
                        text = stringResource(id = R.string.question_tv_card_name),
                        color = Color(0xFF3870C9),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 39.dp, top = 29.dp, bottom = 26.dp),
                    text = homeViewModel.valueStateList[index].question,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp),
            text = stringResource(id = R.string.question_tv_text_enter_answer),
            color = Color(0xFF7F7D7C),
            fontSize = 14.sp
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 12.dp),
            value = ans, placeholder = {
                Text(text = stringResource(id = R.string.question_tv_text_enter_answer))
            },
            onValueChange = {
                ans = it
                answer = ans
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Custom_Blue,
                backgroundColor = ET_Bg,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            maxLines = 1,
            textStyle = MaterialTheme.typography.body2
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun QuestionScreenPreview() {
//    QuestionScreen(navHostController = rememberNavController())
}
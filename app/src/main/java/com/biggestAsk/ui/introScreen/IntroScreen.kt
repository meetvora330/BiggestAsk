package com.biggestAsk.ui.introScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.response.DataXX
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.main.viewmodel.IntroViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.Light_Gray
import com.biggestAsk.ui.ui.theme.Orange
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Intro(
    state: PagerState,
    items: List<SampleOnBoard>,
    detailsItem: List<DataXX>,
    modifierImg: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        count = detailsItem.size,
        state = state,
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) { page ->
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = modifierImg
                    .padding(bottom = 5.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = items[page].image),
                contentDescription = stringResource(id = R.string.content_description),
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .fillMaxHeight(0.25f),
                text = detailsItem[page].title,
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
            )
            Text(
                maxLines = 5,
                text = detailsItem[page].info,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f)
                    .padding(start = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF75818F)
                )
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroScreen(
    navController: NavHostController,
    introViewModel: IntroViewModel,
    state: PagerState,
    items: List<SampleOnBoard>,
    modifierBox: Modifier = Modifier,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    modifier_img: Modifier = Modifier,
    context: Context,
) {

    Box(
        modifier = modifierBox
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Intro(
                state = state,
                items = items,
                modifier = modifier,
                modifierImg = modifier_img,
                detailsItem = introViewModel.introInfoDetailList
            )
            if (introViewModel.isIntroDataLoaded) {
                HorizontalPagerIndicator(
                    pagerState = state,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = 16.dp),
                    activeColor = Orange,
                    inactiveColor = Light_Gray,
                )
            }
        }
        if (introViewModel.isIntroDataLoaded) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 20.dp, end = 10.dp)
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
                    scope.launch {
                        if (state.currentPage < 3) {
                            state.scrollToPage(
                                state.currentPage + 1
                            )
                        } else {
                            PreferenceProvider(context).setValue(Constants.IS_INTRO_DONE, true)
                            navController.popBackStack()
                            navController.navigate(route = Screen.VerifyEmail.route)
                        }
                    }
                }) {
                Text(
                    text = stringResource(id = R.string.intro_screen_btn_next_text),
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    lineHeight = 28.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900
                )
            }
        }

    }
}



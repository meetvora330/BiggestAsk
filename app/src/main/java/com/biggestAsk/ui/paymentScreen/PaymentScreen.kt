@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.biggestAsk.ui.paymentScreen

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.data.model.LoginStatus
import com.biggestAsk.navigation.Screen
import com.biggestAsk.ui.main.viewmodel.HomeViewModel
import com.biggestAsk.ui.main.viewmodel.MainViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.ui.ui.theme.Login_Sub_Tittle
import com.biggestAsk.ui.ui.theme.Payment_Description
import com.biggestAsk.ui.ui.theme.Text_Accept_Terms
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    context: Context
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState, sheetContent = {
            PaymentScreenBottomSheet(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                coroutineScope = coroutineScope,
                navHostController = navHostController
            )
        }, sheetPeekHeight = 0.dp, sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {
        Box {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.img_login_bg),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_login_tittle),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 52.dp)
                            .width(92.dp)
                            .height(104.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.login_tv_text_sub_tittle),
                        textAlign = TextAlign.Center,
                        color = Login_Sub_Tittle,
                        style = MaterialTheme.typography.body2,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 40.sp
                    )
                    Text(
                        text = stringResource(id = R.string.payment_tv_text_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 29.dp, end = 29.dp, top = 30.dp),
                        color = Payment_Description,
                        fontSize = 15.sp,
                        lineHeight = 25.sp
                    )
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 24.dp, top = 32.dp)
                        .clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            navHostController.navigate(
                                Screen.QuestionScreen.route
                            )
                            val provider = PreferenceProvider(context)
                            provider.setValue(Constants.LOGIN_STATUS,
                                LoginStatus.FREQUENCY_NOT_ADDED.name.lowercase(Locale.getDefault())
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(1.dp, color = Custom_Blue)
                    ) {
                        Column(
                            modifier = Modifier.background(Color.White)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 16.dp),
                                text = stringResource(id = R.string.payment_tv_text_option1),
                                color = Custom_Blue,
                                fontSize = 13.sp
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp),
                                text = stringResource(id = R.string.payment_tv_text_option1_trial),
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.W800,
                                fontSize = 22.sp,
                                lineHeight = 28.sp
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, start = 16.dp, bottom = 20.dp),
                                text = stringResource(id = R.string.payment_tv_text_option1_use_app),
                                fontSize = 15.sp,
                                color = Text_Accept_Terms
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 24.dp, top = 32.dp)
                            .clickable {
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }

                                }
                            },
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(1.dp, color = Custom_Blue)
                    ) {
                        Column(
                            modifier = Modifier.background(Custom_Blue)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                                    text = stringResource(id = R.string.payment_tv_text_option2),
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 22.dp, top = 12.dp),
                                    text = stringResource(id = R.string.payment_tv_text_option2_price),
                                    color = Color.White,
                                    style = MaterialTheme.typography.body2,
                                    lineHeight = 28.sp,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.W800,
                                    textAlign = TextAlign.Right
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp),
                                text = stringResource(id = R.string.payment_tv_text_option2_12_subscription),
                                style = MaterialTheme.typography.body2,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.W800,
                                lineHeight = 28.sp,
                                color = Color.White
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, start = 16.dp, bottom = 20.dp),
                                text = stringResource(id = R.string.payment_tv_text_option2_app_av_365),
                                fontSize = 15.sp,
                                color = Color(0xFFF8F5F2),
                                fontWeight = FontWeight.Light,
                                lineHeight = 20.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PaymentPreview() {
//    PaymentScreen(navHostController = rememberNavController())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentScreenBottomSheet(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            painter = painterResource(id = R.drawable.ic_img_bottom_sheet_opener),
            contentDescription = ""
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 33.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(Start)
                    .padding(start = 30.dp),
                text = stringResource(id = R.string.payment_bs_tittle),
                fontSize = 18.sp,
                style = MaterialTheme.typography.body2,
                lineHeight = 25.sp,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Left
            )
            Text(modifier = Modifier
                .padding(end = 30.dp)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        } else {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                },
                text = stringResource(id = R.string.payment_bs_close_text),
                color = Color(0xFF47A6FF),
                style = MaterialTheme.typography.body2,
                fontSize = 18.sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.W700,
                lineHeight = 22.sp
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 23.dp)
                .fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(start = 31.dp),
                painter = painterResource(id = R.drawable.ic_img_bottom_sheet_icon),
                contentDescription = ""
            )
            Column {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 22.dp),
                    text = stringResource(id = R.string.full_app_name),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W700,
                    lineHeight = 25.sp,
                    fontSize = 18.sp,
                    color = Color(0xFF3E3E3E)
                )
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 22.dp, top = 22.dp),
                    text = stringResource(id = R.string.payment_bs_app_for),
                    fontSize = 15.sp,
                    color = Color(0xFF3E3E3E)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 42.dp)
        ) {
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = stringResource(id = R.string.payment_bs_tittle_account),
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                textAlign = TextAlign.Start,
                lineHeight = 25.sp
            )
            Column {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(start = 18.dp, top = 2.dp),
                    text = "kate.gorushenko@gmail.com",
                    fontSize = 15.sp,
                    color = Color(0xFF3E3E3E)
                )
                Row {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 16.dp, top = 40.dp),
                        text = stringResource(id = R.string.payment_bs_tittle_price),
                        fontSize = 18.sp,
                        color = Color(0xFF3E3E3E),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.W700,
                        lineHeight = 25.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, end = 50.dp),
                        text = stringResource(id = R.string.payment_tv_text_option2_price),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom
    ) {
        Button(
            onClick = {
                navHostController.navigate(
                    Screen.QuestionScreen.route
                )
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            },
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(56.dp),
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
                text = stringResource(id = R.string.payment_bs_subscribe_btn_text),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp,
                color = Color.White,
                lineHeight = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PaymentScreenBottomSheetPreview() {
    PaymentScreenBottomSheet(
        bottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
        coroutineScope = rememberCoroutineScope(),
        navHostController = rememberNavController()
    )
}
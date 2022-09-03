package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.request.NotificationStatusUpdateRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetNotificationStatusResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.DetailedSettingsViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

@Composable
fun DetailedSettings(
    context: Context,
    homeActivity: HomeActivity,
    detailedSettingsViewModel: DetailedSettingsViewModel
) {
    val provider = PreferenceProvider(context)
    val type = provider.getValue(Constants.TYPE, "")
    val userId = provider.getIntValue(Constants.USER_ID, 0)
    LaunchedEffect(Unit) {
        getNotificationStatus(
            userId = userId,
            type = type,
            homeActivity = homeActivity,
            context = context,
            detailedSettingsViewModel = detailedSettingsViewModel
        )
    }
    if (detailedSettingsViewModel.isNotificationStatusUpdated) {
        ProgressBarTransparentBackground(loadingText = "Updating...")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        listDetailedSettingsItem.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.startTittle, style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF676870)
                    )
                )
                Text(
                    text = item.endTittle, style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Custom_Blue
                    )
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = item.item, style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                color = Color(0xFFF8F5F2),
                thickness = 1.dp
            )
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.receive_notification),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF676870)
                    )
                )
                Switch(
                    modifier = Modifier
                        .height(25.dp),
                    checked = detailedSettingsViewModel.checkedStateNotification,
                    onCheckedChange = {
                        detailedSettingsViewModel.checkedStateNotification = it
                        if (type != null) {
                            notificationStatusUpdate(
                                homeActivity = homeActivity,
                                detailedSettingsViewModel = detailedSettingsViewModel,
                                context = context,
                                user_id = userId,
                                type = type
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Custom_Blue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Custom_Blue
                    )
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = if (detailedSettingsViewModel.checkedStateNotification) stringResource(id = R.string.enabled) else stringResource(
                    id = R.string.disabled
                ),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = if (detailedSettingsViewModel.checkedStateNotification) Color.Black else Color(
                        0xFFC7C7CC
                    )
                )
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                color = Color(0xFFF8F5F2),
                thickness = 1.dp
            )
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.receive_newsletters),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF676870)
                    )
                )
                Switch(
                    modifier = Modifier
                        .height(25.dp),
                    checked = detailedSettingsViewModel.checkedStateNewsLetters,
                    onCheckedChange = {
                        detailedSettingsViewModel.checkedStateNewsLetters = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Custom_Blue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Custom_Blue
                    )
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = if (detailedSettingsViewModel.checkedStateNewsLetters) stringResource(id = R.string.enabled) else stringResource(
                    id = R.string.disabled
                ),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = if (detailedSettingsViewModel.checkedStateNewsLetters) Color.Black else Color(
                        0xFFC7C7CC
                    )
                )
            )
        }
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 40.dp),
//            shape = RoundedCornerShape(10.dp),
//            elevation = 1.dp,
//        ) {
//            Column {
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Image(
//                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 8.dp),
//                        painter = painterResource(id = R.drawable.ic_icon_setting_detailed_screen),
//                        contentDescription = ""
//                    )
//                    Column(modifier = Modifier.padding(start = 12.dp)) {
//                        Text(
//                            modifier = Modifier.padding(top = 12.dp),
//                            text = stringResource(id = R.string.changes_saved_success_message),
//                            style = MaterialTheme.typography.body2.copy(
//                                fontWeight = FontWeight.W600,
//                                fontSize = 16.sp,
//                                color = Color.Black
//                            )
//                        )
//                        Row(Modifier) {
//                            Image(
//                                modifier = Modifier.padding(top = 5.dp),
//                                painter = painterResource(id = R.drawable.ic_icon_clock_detailed_setting),
//                                contentDescription = ""
//                            )
//                            Text(
//                                modifier = Modifier.padding(start = 4.dp, top = 3.dp),
//                                text = stringResource(id = R.string.just_now),
//                                style = MaterialTheme.typography.body1.copy(
//                                    fontSize = 12.sp,
//                                    color = Color(0xFFC7C7CC),
//                                    fontWeight = FontWeight.W500,
//                                    lineHeight = 16.sp
//                                )
//                            )
//                        }
//
//                    }
//
//                }
//            }
//        }
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 37.dp, bottom = 20.dp)
                .clickable(indication = null, interactionSource = MutableInteractionSource()) {

                    if (type != null) {
                        detailedSettingsViewModel.userLogout(
                            user_id = userId,
                            type = type
                        )
                    }
                    detailedSettingsViewModel.userLogoutResponse.observe(homeActivity) {
                        if (it != null) {
                            handleUserLogoutData(
                                result = it,
                                detailedSettingsViewModel = detailedSettingsViewModel
                            )
                        }
                    }
                },
            text = buildAnnotatedString {
                val logout = context.resources.getString(R.string.logout)
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.W600,
                        textDecoration = TextDecoration.Underline,
                        color = Custom_Blue,
                        fontSize = 16.sp,
                    )
                ) {
                    append(logout)
                }
            },
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
    }
}


private fun handleUserLogoutData(
    result: NetworkResult<CommonResponse>,
    detailedSettingsViewModel: DetailedSettingsViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            detailedSettingsViewModel.isNotificationStatusFetched = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            detailedSettingsViewModel.isNotificationStatusFetched = false
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            detailedSettingsViewModel.isNotificationStatusFetched = false
        }
    }
}

fun getNotificationStatus(
    userId: Int,
    type: String?,
    homeActivity: HomeActivity,
    context: Context,
    detailedSettingsViewModel: DetailedSettingsViewModel
) {
    if (type != null) {
        detailedSettingsViewModel.getNotificationStatus(
            type = type,
            user_id = userId
        )
    }
    detailedSettingsViewModel.getNotificationStatusResponse.observe(homeActivity) {
        if (it != null) {
            handleGetNotificationStatusData(
                result = it,
                detailedSettingsViewModel = detailedSettingsViewModel,
            )
        }
    }
}

private fun handleGetNotificationStatusData(
    result: NetworkResult<GetNotificationStatusResponse>,
    detailedSettingsViewModel: DetailedSettingsViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            detailedSettingsViewModel.isNotificationStatusFetched = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            detailedSettingsViewModel.isNotificationStatusFetched = false
            detailedSettingsViewModel.checkedStateNotification =
                if (result.data?.status == "active") true else if (result.data?.status == "inactive") false else false
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            detailedSettingsViewModel.isNotificationStatusFetched = false
        }
    }
}

fun notificationStatusUpdate(
    homeActivity: HomeActivity,
    detailedSettingsViewModel: DetailedSettingsViewModel,
    context: Context,
    user_id: Int,
    type: String
) {
    val status =
        if (detailedSettingsViewModel.checkedStateNotification) "active" else if (!detailedSettingsViewModel.checkedStateNotification) "inactive" else ""
    detailedSettingsViewModel.notificationStatusUpdate(
        NotificationStatusUpdateRequest(
            type = type,
            user_id = user_id,
            status = status
        )
    )
    detailedSettingsViewModel.notificationStatusUpdateResponse.observe(homeActivity) {
        if (it != null) {
            handleNotificationStatusUpdateData(
                result = it,
                detailedSettingsViewModel = detailedSettingsViewModel,
                context = context,
                userId = user_id,
                type = type,
                homeActivity = homeActivity
            )
        }
    }
}

private fun handleNotificationStatusUpdateData(
    result: NetworkResult<CommonResponse>,
    detailedSettingsViewModel: DetailedSettingsViewModel,
    context: Context,
    userId: Int,
    type: String,
    homeActivity: HomeActivity
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            detailedSettingsViewModel.isNotificationStatusUpdated = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            Log.e("TAG", "handleUserData() --> Success  $result")
            detailedSettingsViewModel.isNotificationStatusUpdated = false
            getNotificationStatus(
                userId = userId,
                homeActivity = homeActivity,
                type = type,
                context = context,
                detailedSettingsViewModel = detailedSettingsViewModel
            )
        }
        is NetworkResult.Error -> {
            // show error message
            Log.e("TAG", "handleUserData() --> Error ${result.message}")
            detailedSettingsViewModel.isNotificationStatusUpdated = false
        }
    }
}
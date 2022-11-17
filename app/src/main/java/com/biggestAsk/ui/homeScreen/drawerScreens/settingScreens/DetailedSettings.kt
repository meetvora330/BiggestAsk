package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.request.NotificationStatusUpdateRequest
import com.biggestAsk.data.model.response.CommonResponse
import com.biggestAsk.data.model.response.GetNotificationStatusResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.DetailedSettingsViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
/**
 * detailed setting screen
 */
@Composable
fun DetailedSettings(
    context: Context,
    homeActivity: HomeActivity,
    detailedSettingsViewModel: DetailedSettingsViewModel,
) {
    val provider = PreferenceProvider(context)
    val type = provider.getValue(Constants.TYPE, "")
    val userId = provider.getIntValue(Constants.USER_ID, 0)
    LaunchedEffect(Unit) {
        getNotificationStatus(
            userId = userId,
            type = type,
            homeActivity = homeActivity,
            detailedSettingsViewModel = detailedSettingsViewModel
        )
    }
    if (detailedSettingsViewModel.isNotificationStatusUpdated || detailedSettingsViewModel.isNotificationStatusFetched) {
        ProgressBarTransparentBackground(
            loadingText = if (detailedSettingsViewModel.isNotificationStatusUpdated) stringResource(id = R.string.updating) else "Fetching",
            id = R.color.transparent
        )
    } else {
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
                                    user_id = userId,
                                    type = type
                                )
                            }
                        }, enabled = !detailedSettingsViewModel.isNotificationStatusUpdated,
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
        }
    }
}

fun getNotificationStatus(
    userId: Int,
    type: String?,
    homeActivity: HomeActivity,
    detailedSettingsViewModel: DetailedSettingsViewModel,
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
            detailedSettingsViewModel.isNotificationStatusFetched = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            detailedSettingsViewModel.isNotificationStatusFetched = false
            detailedSettingsViewModel.checkedStateNotification =
                if (result.data?.status == Constants.ACTIVE) true else if (result.data?.status == Constants.IN_ACTIVE) false else false
        }
        is NetworkResult.Error -> {
            // show error message
            detailedSettingsViewModel.isNotificationStatusFetched = false
        }
    }
}

fun notificationStatusUpdate(
    homeActivity: HomeActivity,
    detailedSettingsViewModel: DetailedSettingsViewModel,
    user_id: Int,
    type: String,
) {
    val status =
        if (detailedSettingsViewModel.checkedStateNotification) Constants.ACTIVE else if (!detailedSettingsViewModel.checkedStateNotification) Constants.IN_ACTIVE else ""
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
            )
        }
    }
}

private fun handleNotificationStatusUpdateData(
    result: NetworkResult<CommonResponse>,
    detailedSettingsViewModel: DetailedSettingsViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            detailedSettingsViewModel.isNotificationStatusUpdated = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            detailedSettingsViewModel.isNotificationStatusUpdated = false
        }
        is NetworkResult.Error -> {
            // show error message
            detailedSettingsViewModel.isNotificationStatusUpdated = false
        }
    }
}
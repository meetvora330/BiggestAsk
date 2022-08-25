package com.biggestAsk.ui.homeScreen.drawerScreens.notification

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.GetNotificationRequest
import com.biggestAsk.data.model.response.GetNotificationResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.HomeActivity
import com.biggestAsk.ui.main.viewmodel.NotificationViewModel
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun Notification(
    navHostController: NavHostController,
    notificationViewModel: NotificationViewModel,
    homeActivity: HomeActivity,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        val context = LocalContext.current
        val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
        val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
        LaunchedEffect(Unit) {
            getUpdatedNotification("parent", 191, notificationViewModel, homeActivity)
            //type?.let { getUpdatedNotification(it, userId, notificationViewModel, homeActivity) }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 55.dp)
        ) {
            notificationViewModel.updatedList.forEachIndexed { index, item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            navHostController.navigate(
                                NotificationDetailScreenRoute.NotificationDetails.notificationDetails(
                                    R.drawable.ic_icon_notification_1,
                                    item.title.replace(".", ""),
                                    item.notification,
                                    notificationViewModel.notificationDaysList[index]
                                )
                            )
                        }
                        .advancedShadow(
                            color = Color(red = 27, green = 25, blue = 86, alpha = 0.1f.toInt()),
                            alpha = 0f,
                            cornersRadius = 16.dp,
                            shadowBlurRadius = 0.2.dp,
                            offsetX = 0.dp,
                            offsetY = 4.dp
                        )
                        .placeholder(
                            visible = notificationViewModel.isLoading,
                            color = Color.LightGray,
                            // optional, defaults to RectangleShape
                            shape = RoundedCornerShape(14.dp),
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = Color.White,
                            ),
                        )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                                painter = painterResource(id = R.drawable.ic_icon_notification_1),
                                contentDescription = "",
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    val (tv_nTittle, tv_nDays) = createRefs()
                                    Text(
                                        modifier = Modifier.constrainAs(tv_nTittle) {
                                            start.linkTo(parent.start)
                                            top.linkTo(parent.top)
                                        },
                                        text = item.title,
                                        style = MaterialTheme.typography.body1,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                        color = Color.Black
                                    )
                                    Text(
                                        modifier = Modifier
                                            .constrainAs(tv_nDays) {
                                                top.linkTo(parent.top)
                                                end.linkTo(parent.end)
                                            }
                                            .padding(top = 2.dp, end = 24.dp),
                                        text = "${notificationViewModel.notificationDaysList[index]} Day ago",
                                        style = MaterialTheme.typography.body1,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = Color(0xFF9F9D9B)
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                                    text = item.notification,
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    color = Color(0xFF8995A3)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (notificationViewModel.isDataNull) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.NO_NOTIFICATION_FOUND))
        }
    }
}

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

fun getUpdatedNotification(
    type: String,
    user_id: Int,
    notificationViewModel: NotificationViewModel,
    homeActivity: HomeActivity,
) {
    notificationViewModel.getNotification(getNotificationRequest = GetNotificationRequest(
        type = type,
        user_id = user_id
    ))

    notificationViewModel.getNotificationResponse.observe(homeActivity) {
        if (it != null) {
            handleGetNotificationApi(
                result = it,
                notificationViewModel = notificationViewModel,
            )
        } else {
            Log.e("TAG", "GetContactData is null: ")
        }
    }
}

private fun handleGetNotificationApi(
    result: NetworkResult<GetNotificationResponse>,
    notificationViewModel: NotificationViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            notificationViewModel.isLoading = true
            notificationViewModel.isDataNull = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            notificationViewModel.isLoading = false
            notificationViewModel.notificationList.clear()
            result.data?.data?.let { notificationViewModel.notificationList.addAll(it) }
            notificationViewModel.updatedList.clear()
            notificationViewModel.updatedList.addAll(notificationViewModel.notificationList)
            notificationViewModel.notificationDaysList.clear()
            result.data?.let { notificationViewModel.notificationDaysList.addAll(it.days) }
            notificationViewModel.isDataNull = notificationViewModel.notificationList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            notificationViewModel.isLoading = false
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun NotificationPreview() {
//    Notification(navHostController = rememberNavController())
//}
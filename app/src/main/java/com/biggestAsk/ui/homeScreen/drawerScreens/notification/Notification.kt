package com.biggestAsk.ui.homeScreen.drawerScreens.notification

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Notification(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 55.dp)
        ) {
            listNotification.forEach { item ->
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
                                    item.nIcon,
                                    item.nTittle.replace(".",""),
                                    item.nDescription,
                                    item.nDays
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
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                                painter = painterResource(id = item.nIcon),
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
                                        text = item.nTittle,
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
                                        text = item.nDays,
                                        style = MaterialTheme.typography.body1,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = Color(0xFF9F9D9B)
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                                    text = item.nDescription,
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
}

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationPreview() {
    Notification(navHostController = rememberNavController())
}
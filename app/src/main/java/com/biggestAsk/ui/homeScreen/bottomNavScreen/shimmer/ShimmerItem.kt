package com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun shimmerTheme(): Brush {
    val shimmerColorShades = listOf(
        Color.LightGray.copy(0.9f),

        Color.LightGray.copy(0.2f),

        Color.LightGray.copy(0.9f)
    )
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            // Tween Animates between values over specified [durationMillis]
            tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    return Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun PregnancyMilestoneShimmerAnimation() {
    val brush = shimmerTheme()
    PregnancyMilestoneShimmerItem(brush = brush)
}

@Composable
fun PregnancyMilestoneShimmerItem(brush: Brush) {
    Text(
        modifier = Modifier
            .width(200.dp)
            .padding(start = 24.dp, top = 40.dp, bottom = 10.dp)
            .background(brush, RoundedCornerShape(8.dp)),
        text = "",
        style = MaterialTheme.typography.body2,
        fontSize = 16.sp,
        fontWeight = FontWeight.W900,
        lineHeight = 24.sp
    )
    Surface(
        shape = RoundedCornerShape(14.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 18.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .background(brush),
                    text = "",
                    style = MaterialTheme.typography.body2,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W800,
                    lineHeight = 24.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = "",
                    style = MaterialTheme.typography.body2,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 22.sp
                )
            }
            GlideImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .padding(top = 10.dp, start = 24.dp),
                contentScale = ContentScale.Crop, imageModel = ""
            )
        }
    }
}

@Composable
fun NearestMilestoneShimmerAnimation() {
    val brush = shimmerTheme()
    NearestMilestoneShimmerItem(brush = brush)
}

@Composable
fun NearestMilestoneShimmerItem(brush: Brush) {
    Text(
        modifier = Modifier
            .width(150.dp)
            .padding(start = 24.dp, top = 40.dp, bottom = 20.dp)
            .background(brush, RoundedCornerShape(8.dp)),
        text = "",
        style = MaterialTheme.typography.body2,
        fontSize = 16.sp,
        fontWeight = FontWeight.W900,
        lineHeight = 24.sp
    )
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
            )
    ) {
        Column(modifier = Modifier.background(brush = brush)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    modifier = Modifier
                        .padding(
                            top = 14.dp, start = 24.dp, bottom = 24.dp, end = 24.dp
                        )
                        .width(278.dp)
                        .height(160.dp),
                    contentScale = ContentScale.Crop, imageModel = ""
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                text = "",
                style = MaterialTheme.typography.body2,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 32.sp
            )
            Row {
                GlideImage(
                    modifier = Modifier.padding(
                        top = 15.dp,
                        start = 24.dp,
                        bottom = 26.dp
                    ),
                    imageModel = "",
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 17.dp),
                    text = "",
                    color = Color(0xFF9F9D9B),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W400,
                    lineHeight = 16.sp,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun HomeScreenQuestionShimmerAnimation() {
    val brush = shimmerTheme()
    HomeScreenQuestionShimmerItem(brush = brush)
}

@Composable
fun HomeScreenQuestionShimmerItem(brush: Brush) {
    Text(
        modifier = Modifier
            .width(180.dp)
            .padding(start = 24.dp, top = 40.dp, bottom = 18.dp)
            .background(brush, RoundedCornerShape(8.dp)),
        text = "",
        style = MaterialTheme.typography.body2,
        fontSize = 16.sp,
        fontWeight = FontWeight.W900,
        lineHeight = 24.sp
    )
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
            )
    ) {
        Column(
            modifier = Modifier.background(brush),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush)
                    .padding(start = 37.dp, top = 24.dp, end = 36.dp),
                text = "",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                fontSize = 25.sp,
                fontWeight = FontWeight.W900,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 6.dp),
                text = "",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 16.dp,
                        bottom = 24.dp
                    )
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(brush, shape = RoundedCornerShape(30)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                shape = RoundedCornerShape(30),
            ) {
                Text(
                    text = "",
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

@Composable
fun IntendedParentsShimmerAnimation() {
    val brush = shimmerTheme()
    IntendedParentsShimmerItem(brush = brush)
}

@Composable
fun IntendedParentsShimmerItem(brush: Brush) {
    Text(
        modifier = Modifier
            .width(180.dp)
            .padding(start = 24.dp, top = 40.dp, bottom = 20.dp)
            .background(
                brush,
                RoundedCornerShape(8.dp)
            ),
        text = "",
        style = MaterialTheme.typography.body2,
        fontSize = 16.sp,
        fontWeight = FontWeight.W900,
        lineHeight = 24.sp
    )
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 23.dp, bottom = 70.dp)
    ) {
        Column(modifier = Modifier.background(brush)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                text = "",
                color = Color.Black,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W600,
                lineHeight = 24.sp,
                fontSize = 16.sp,
            )
            Row {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                    text = "",
                    style = MaterialTheme.typography.body2,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 24.dp),
                    text = "",
                    color = Color(0xFF9F9D9B),
                    style = MaterialTheme.typography.body2,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }
            Text(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 4.dp,
                    bottom = 22.dp
                ),
                text = "",
                color = Color.Black,
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
fun MilestoneScreenShimmerAnimation() {
    val brush = shimmerTheme()
    MilestoneScreenShimmerItem(brush)
}

@Composable
fun MilestoneScreenShimmerItem(brush: Brush) {
    val height = 142.dp
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
                .background(
                    brush,
                    RoundedCornerShape(20.dp)
                )
        ) {

        }
    }

}

@Composable
fun QuestionScreenAnsweredQuestionShimmerAnimation() {
    val brush = shimmerTheme()
    QuestionScreenAnsweredQuestionShimmerItem(brush = brush)
}

@Composable
fun QuestionScreenAnsweredQuestionShimmerItem(brush: Brush) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 23.dp, top = 34.dp)
    ) {
        Column(modifier = Modifier.background(brush, RoundedCornerShape(8.dp))) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                text = "",
                color = Color.Black,
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 24.sp
                ),
            )
            Row {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                    text = "",
                    style = MaterialTheme.typography.body2.copy(
                        color = Custom_Blue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 24.dp),
                    text = "",
                    color = Color(0xFF9F9D9B),
                    style = MaterialTheme.typography.body1,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }
            Text(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 4.dp,
                    bottom = 22.dp
                ),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 22.sp
                ),
            )
        }
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 18.dp)
    ) {
        Column(modifier = Modifier.background(brush, RoundedCornerShape(8.dp))) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 56.dp),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 24.sp
                ),
            )
            Row {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 10.dp),
                    text = "",
                    style = MaterialTheme.typography.body2.copy(
                        color = Custom_Blue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    ),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 24.dp),
                    text = "",
                    color = Color(0xFF9F9D9B),
                    style = MaterialTheme.typography.body1,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }
            Text(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 4.dp,
                    bottom = 22.dp
                ),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 22.sp
                ),
            )
        }
    }
}
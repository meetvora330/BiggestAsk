package com.biggestAsk.ui.homeScreen.bottomNavScreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
fun ShimmerAnimation() {
    val shimmerColorShades = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
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

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
    BottomHomeShimmerItem(brush = brush)
}

@Composable
fun BottomHomeShimmerItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 40.dp)
                .background(brush),
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
                .padding(start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
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
                        color = Color.Black,
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W800,
                        lineHeight = 24.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(brush),
                        text = "",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp
                    )
                }
                GlideImage(
                    modifier = Modifier
                        .width(100.dp)
                        .height(110.dp)
                        .padding(top = 10.dp, start = 24.dp)
                        .background(brush),
                    contentScale = ContentScale.Crop, imageModel = ""
                )
            }
        }
    }
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 30.dp)
    ) {
        Column(modifier = Modifier.background(brush)) {
            Text(
                modifier = Modifier
                    .padding(start = 24.dp, top = 19.dp)
                    .background(brush),
                text = "",
                color = Color.Black,
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.W800,
                lineHeight = 24.sp
            )

        }
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 24.dp)
            .background(brush),
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
            .padding(start = 24.dp, end = 24.dp, top = 16.dp)
    ) {
        Column(modifier = Modifier.background(brush)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    imageModel = "",
                    modifier = Modifier
                        .padding(
                            top = 14.dp, start = 24.dp, bottom = 24.dp, end = 24.dp
                        )
                        .background(brush),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
                    .background(brush),
                text = "",
                style = MaterialTheme.typography.body2,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 32.sp
            )
            Row {
                GlideImage(
                    imageModel = "",
                    modifier = Modifier
                        .padding(
                            top = 15.dp,
                            start = 24.dp,
                            bottom = 26.dp
                        )
                        .background(brush),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 17.dp)
                        .background(brush),
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
    Text(
        modifier = Modifier
            .padding(start = 24.dp, top = 40.dp)
            .background(brush),
        text = "",
        style = MaterialTheme.typography.body2,
        fontSize = 16.sp,
        fontWeight = FontWeight.W900,
        lineHeight = 24.sp
    )
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF4479CC),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 0.dp
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 37.dp, top = 24.dp, end = 36.dp)
                    .background(brush),
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
                    .padding(start = 24.dp, top = 6.dp)
                    .background(brush),
                text = "",
                color = Color.White,
                style = MaterialTheme.typography.body2,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )

        }
    }

    Text(
        modifier = Modifier
            .padding(start = 24.dp, top = 44.dp)
            .background(brush),
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
            .padding(start = 25.dp, end = 23.dp, top = 16.dp, bottom = 70.dp)

    ) {
        Column {
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
                    modifier = Modifier
                        .padding(start = 24.dp, top = 10.dp)
                        .background(brush),
                    text = "",
                    color = Custom_Blue,
                    style = MaterialTheme.typography.body2,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 24.dp)
                        .background(brush),
                    text = "",
                    color = Color(0xFF9F9D9B),
                    style = MaterialTheme.typography.body2,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }
            Text(
                modifier = Modifier
                    .padding(
                        start = 24.dp,
                        top = 4.dp,
                        bottom = 22.dp
                    )
                    .background(brush),
                text = "",
                color = Color.Black,
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

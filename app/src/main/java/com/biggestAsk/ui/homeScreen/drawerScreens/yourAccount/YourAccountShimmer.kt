package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.biggestAsk.ui.homeScreen.bottomNavScreen.shimmer.shimmerTheme
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun YourAccountSurrogateShimmerAnimation() {
    val brush = shimmerTheme()
    YourAccountSurrogateShimmerItem(brush = brush)
}

@Composable
fun YourAccountSurrogateShimmerItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                32.dp,
                Alignment.CenterHorizontally
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                val (img_user) = createRefs()
                GlideImage(
                    modifier = Modifier
                        .width(120.dp)
                        .height(100.dp)
                        .constrainAs(img_user) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .background(brush, RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop, imageModel = ""
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .width(130.dp)
                    .background(brush, RoundedCornerShape(6.dp)),
                text = "",
                style = MaterialTheme.typography.h2.copy(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    lineHeight = 32.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(160.dp)
                    .padding(top = 10.dp)
                    .background(brush, RoundedCornerShape(8.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF7F7D7C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(190.dp)
                    .padding(top = 10.dp)
                    .background(brush, RoundedCornerShape(8.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 11.dp)
                    .background(brush, RoundedCornerShape(8.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Custom_Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(140.dp)
                    .padding(top = 11.dp)
                    .background(brush, RoundedCornerShape(8.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Custom_Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
        }
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
}

@Composable
fun YourAccountParentShimmerAnimation() {
    val brush = shimmerTheme()
    YourAccountParentShimmerItem(brush = brush)
}

@Composable
fun YourAccountParentShimmerItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 34.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                32.dp,
                Alignment.CenterHorizontally
            )
        ) {
            GlideImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop, imageModel = ""
            )
            GlideImage(
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop, imageModel = ""
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 20.dp,end = 2.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF7F7D7C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 10.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 10.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 11.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Custom_Blue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
            Text(
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 16.dp)
                    .background(brush, RoundedCornerShape(10.dp)),
                text = "",
                style = MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 22.sp
                )
            )
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 23.dp, top = 24.dp)
        ) {
            Column(modifier = Modifier.background(brush, RoundedCornerShape(10.dp))) {
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
            Column(modifier = Modifier.background(brush, RoundedCornerShape(10.dp))) {
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
}






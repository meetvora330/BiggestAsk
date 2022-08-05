package com.biggestAsk.ui.homeScreen.drawerScreens.contactYourProvider

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R

@Composable
fun ContactYourProvider(modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 55.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        ListContactProviders.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, end = 24.dp, top = 16.dp, bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 1.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .padding(top = 16.dp, start = 16.dp),
                            painter = painterResource(id = item.AIcon),
                            contentDescription = "",
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item.title,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item.ATvRepName,
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.ARepName,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item.ATvRepEmail,
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.ARepEmail,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item.TvRepPhone,
                                style = MaterialTheme.typography.body1,
                                fontSize = 14.sp,
                                color = Color(0xFF8995A3)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = item.APhoneNumber,
                                style = MaterialTheme.typography.body1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black,
                                lineHeight = 22.sp
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 21.dp),
                        color = Color(0xFFF4F5F6),
                        thickness = 0.8.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 12.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .width(140.dp)
                                .height(36.dp), shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFF4F4F4)),
                            onClick = { },
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                        ) {
                            Text(
                                text = stringResource(id = R.string.share),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Custom_Blue,
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(140.dp)
                                .height(36.dp), elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            onClick = { }, shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Custom_Blue)
                        ) {
                            Text(
                                text = stringResource(id = R.string.call),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
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
fun ContactYourProviderPreview() {
    ContactYourProvider(
        modifier = Modifier
    )
}
package com.biggestAsk.ui.homeScreen.drawerScreens.community

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.ui.theme.Custom_Blue

@Composable
fun Community() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 55.dp)
        ) {
            listCommunity.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                        .shadow(
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color(0xFFF4F4F4),
                            elevation = 5.dp,
                            clip = true
                        )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                                painter = painterResource(id = item.cIcon),
                                contentDescription = "",
                            )
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    modifier = Modifier.padding(top = 16.dp),
                                    text = item.cTittle,
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = item.cDescription,
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 14.sp,
                                    color = Color(0xFF8995A3)
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
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
                                    text = item.btn1Text,
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
                                    text = item.btn2Text,
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
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CommunityPreview() {
    Community()
}
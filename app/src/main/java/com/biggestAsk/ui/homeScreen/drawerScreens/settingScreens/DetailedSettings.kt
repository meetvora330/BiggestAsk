package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.example.biggestAsk.R

@Composable
fun DetailedSettings() {
    val checkedStateNotification = remember { mutableStateOf(true) }
    val checkedStateNewsLetters = remember { mutableStateOf(true) }
    val context = LocalContext.current
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
                    checked = checkedStateNotification.value,
                    onCheckedChange = { checkedStateNotification.value = it },
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
                text = if (checkedStateNotification.value) stringResource(id = R.string.enabled) else stringResource(
                    id = R.string.disabled
                ),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = if (checkedStateNotification.value) Color.Black else Color(0xFFC7C7CC)
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
                    checked = checkedStateNewsLetters.value,
                    onCheckedChange = { checkedStateNewsLetters.value = it },
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
                text = if (checkedStateNewsLetters.value) stringResource(id = R.string.enabled) else stringResource(
                    id = R.string.disabled
                ),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = if (checkedStateNewsLetters.value) Color.Black else Color(0xFFC7C7CC)
                )
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 1.dp,
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 8.dp),
                        painter = painterResource(id = R.drawable.ic_icon_setting_detailed_screen),
                        contentDescription = ""
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp),
                            text = stringResource(id = R.string.changes_saved_success_message),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.W600,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        )
                        Row(Modifier) {
                            Image(
                                modifier = Modifier.padding(top = 5.dp),
                                painter = painterResource(id = R.drawable.ic_icon_clock_detailed_setting),
                                contentDescription = ""
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp, top = 3.dp),
                                text = stringResource(id = R.string.just_now),
                                style = MaterialTheme.typography.body1.copy(
                                    fontSize = 12.sp,
                                    color = Color(0xFFC7C7CC),
                                    fontWeight = FontWeight.W500,
                                    lineHeight = 16.sp
                                )
                            )
                        }

                    }

                }
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 37.dp, bottom = 20.dp),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailedSettingPreview() {
    DetailedSettings()
}
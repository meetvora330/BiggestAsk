package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.example.biggestAsk.R

const val HELP_URL = Constants.HELP_URL

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Settings(navHostController: NavHostController) {
    val openDialogDeleteSetting = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 60.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        listSettingsItem.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        when (item.title) {
                            Constants.HELP -> {
                                startActivity(
                                    context,
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(HELP_URL)
                                    ),
                                    Bundle()
                                )
                            }
                            Constants.ABOUT -> {
                                navHostController.navigate(SettingSubScreen.AboutApp.route)
                            }
                            Constants.DETAILED_SETTINGS -> {
                                navHostController.navigate(SettingSubScreen.DetailedSetting.route)
                            }
                            Constants.PRIVACY_POLICY -> {
                                navHostController.navigate(SettingSubScreen.PrivacyPolicy.route)
                            }
                            Constants.TERMS_OF_SERVICE -> {
                                navHostController.navigate(SettingSubScreen.TermsOfService.route)
                            }
                        }
                    },
                border = BorderStroke(
                    1.dp, Color(0x1AD2D3DD)
                ), shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 20.dp, bottom = 24.dp)
                ) {
                    Image(
                        modifier = Modifier.padding(start = 29.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = ""
                    )
                    Text(
                        text = item.title, modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 29.dp),
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

        }
        Button(
            onClick = {
                openDialogDeleteSetting.value = true
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, top = 32.dp)
                .height(56.dp), elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            ), shape = RoundedCornerShape(30), colors = ButtonDefaults.buttonColors(
                backgroundColor = Custom_Blue,
            )
        ) {
            Text(
                text = stringResource(id = R.string.reset_milestone),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
        if (openDialogDeleteSetting.value) {
            Dialog(
                onDismissRequest = { openDialogDeleteSetting.value = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = true,
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    SettingDialogDelete(
                        openDialogDeleteSetting = openDialogDeleteSetting,
                        positive_btn_text = stringResource(id = R.string.positive_btn_text),
                        negative_btn_text = stringResource(id = R.string.negative_btn_text)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingPreview() {
    Settings(
        navHostController = rememberNavController()
    )
}

@Composable
fun SettingDialogDelete(
    openDialogDeleteSetting: MutableState<Boolean>,
    positive_btn_text: String,
    negative_btn_text: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .width(54.dp)
                .height(59.dp)
                .padding(top = 16.dp),
            painter = painterResource(id = R.drawable.logo_setting_delete_dialog),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            text = stringResource(id = R.string.are_you_sure_reset_milestone),
            style = MaterialTheme.typography.body1.copy(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.08).sp
            )
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            color = Color(0xFFC7C7CC),
            thickness = 0.5.dp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        openDialogDeleteSetting.value = false
                    },
                text = negative_btn_text,
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF8E8E93),
                    fontWeight = FontWeight.W600,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            )
            Divider(
                color = Color(0xFFC7C7CC),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(1.dp)
                    .height(55.dp),
                thickness = 0.5.dp
            )
            Text(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        Toast
                            .makeText(
                                context,
                                R.string.reset_milestone_success_message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        openDialogDeleteSetting.value = false
                    },
                text = positive_btn_text,
                style = MaterialTheme.typography.body2.copy(
                    color = Color(0xFF007AFF),
                    fontWeight = FontWeight.W600,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingDialogPreview() {
    SettingDialogDelete(remember {
        mutableStateOf(false)
    }, positive_btn_text = "yes", negative_btn_text = "No")
}
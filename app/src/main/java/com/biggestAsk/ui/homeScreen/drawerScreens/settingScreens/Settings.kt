package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.biggestAsk.data.model.request.ResetMilestoneRequest
import com.biggestAsk.data.model.response.ResetMilestoneResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.SettingViewModel
import com.biggestAsk.ui.ui.theme.Custom_Blue
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

const val HELP_URL = Constants.HELP_URL

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Settings(
    navHostController: NavHostController,
    settingViewModel: SettingViewModel,
    homeActivity: HomeActivity,
    context: Context
) {
    if (settingViewModel.isAllMilestoneReset) {
        ProgressBarTransparentBackground(loadingText = "Resetting milestones...")
    } else {
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
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
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
                                    if (isInternetAvailable(context)) {
                                        navHostController.navigate(SettingSubScreen.AboutApp.route)
                                    } else Toast
                                        .makeText(
                                            context,
                                            R.string.no_internet_available,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                Constants.DETAILED_SETTINGS -> {
                                    navHostController.navigate(SettingSubScreen.DetailedSetting.route)
                                }
                                Constants.PRIVACY_POLICY -> {
                                    if (isInternetAvailable(context)) {
                                        navHostController.navigate(SettingSubScreen.PrivacyPolicy.route)
                                    } else Toast
                                        .makeText(
                                            context,
                                            R.string.no_internet_available,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                Constants.TERMS_OF_SERVICE -> {
                                    if (isInternetAvailable(context)) {
                                        navHostController.navigate(SettingSubScreen.TermsOfService.route)
                                    } else Toast
                                        .makeText(
                                            context,
                                            R.string.no_internet_available,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
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
                    settingViewModel.openDialogDeleteSetting = true
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
            if (settingViewModel.openDialogDeleteSetting) {
                Dialog(
                    onDismissRequest = { settingViewModel.openDialogDeleteSetting = false },
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
                            positive_btn_text = stringResource(id = R.string.positive_btn_text),
                            negative_btn_text = stringResource(id = R.string.negative_btn_text),
                            homeActivity = homeActivity,
                            context = context,
                            settingViewModel = settingViewModel
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SettingDialogDelete(
    positive_btn_text: String,
    negative_btn_text: String,
    homeActivity: HomeActivity,
    settingViewModel: SettingViewModel,
    context: Context
) {
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
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        settingViewModel.openDialogDeleteSetting = false
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = negative_btn_text,
                    style = MaterialTheme.typography.body2.copy(
                        color = Color(0xFF8E8E93),
                        fontWeight = FontWeight.W600,
                        fontSize = 17.sp,
                        lineHeight = 22.sp
                    )
                )
            }
            Divider(
                color = Color(0xFFC7C7CC),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(1.dp)
                    .height(55.dp),
                thickness = 0.5.dp
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        resetMilestone(homeActivity, context, settingViewModel)
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
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
}

fun resetMilestone(
    homeActivity: HomeActivity,
    context: Context,
    settingViewModel: SettingViewModel
) {
    val provider = PreferenceProvider(context)
    val userId = provider.getIntValue("user_id", 0)
    val type = provider.getValue("type", "")
    if (type != null) {
        settingViewModel.resetMilestone(
            ResetMilestoneRequest(
                milestone_id = mutableListOf<Int>(),
                type = type,
                user_id = userId
            )
        )
    }
    settingViewModel.resetMilestoneResponse.observe(homeActivity) {
        if (it != null) {
            handleResetMilestoneData(
                result = it,
                context = context,
                settingViewModel = settingViewModel,
            )
        }
    }
}

private fun handleResetMilestoneData(
    result: NetworkResult<ResetMilestoneResponse>,
    context: Context,
    settingViewModel: SettingViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            Log.e("TAG", "handleUserData() --> Loading  $result")
            settingViewModel.isAllMilestoneReset = true
        }
        is NetworkResult.Success -> {
            // bind data to the view
            settingViewModel.isAllMilestoneReset = false
            result.data?.status?.let {
                PreferenceProvider(context).setValue(
                    Constants.LOGIN_STATUS,
                    it
                )
            }
            settingViewModel.openDialogDeleteSetting = false
        }
        is NetworkResult.Error -> {
            // show error message
            settingViewModel.isAllMilestoneReset = false
            settingViewModel.openDialogDeleteSetting = true
        }
    }
}
package com.biggestAsk.ui.homeScreen.drawerScreens.yourAccount

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.request.LogoutRequest
import com.biggestAsk.data.model.response.LogoutResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.data.source.network.isInternetAvailable
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.activity.MainActivity
import com.biggestAsk.ui.introScreen.findActivity
import com.biggestAsk.ui.main.viewmodel.LogoutViewModel
import com.biggestAsk.util.Constants
import com.biggestAsk.util.PreferenceProvider
import com.example.biggestAsk.R

@Composable
fun LogoutDialog(
    context: Context,
    homeActivity: HomeActivity,
    logoutViewModel: LogoutViewModel,
) {
    val type = PreferenceProvider(context).getValue(Constants.TYPE, "")
    val userId = PreferenceProvider(context).getIntValue(Constants.USER_ID, 0)
    Column(modifier = Modifier.fillMaxWidth()) {
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
                contentDescription = stringResource(id = R.string.content_description),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.are_you_sure_you_want_to_logout),
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
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(), Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(52.dp)
                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                        logoutViewModel.openLogoutDialog = false
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.negative_btn_text),
                    style = MaterialTheme.typography.body2.copy(
                        color = Color(0xFF8E8E93),
                        fontWeight = FontWeight.W600,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
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
                        if (isInternetAvailable(context)) {
                            type?.let {
                                logOutUser(
                                    it,
                                    userId,
                                    logoutViewModel,
                                    homeActivity,
                                    context
                                )
                            }
                        } else Toast
                            .makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT)
                            .show()
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.positive_btn_text),
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

fun logOutUser(
    type: String,
    user_id: Int,
    logoutViewModel: LogoutViewModel,
    homeActivity: HomeActivity,
    context: Context,
) {
    logoutViewModel.logOut(
        logoutRequest = LogoutRequest(
            type = type,
            user_id = user_id
        )
    )
    logoutViewModel.logOutResponse.observe(homeActivity) {
        if (it != null) {
            handleLogOutApi(
                result = it,
                logoutViewModel = logoutViewModel,
                context = context,
                homeActivity = homeActivity
            )
        }
    }
}

private fun handleLogOutApi(
    result: NetworkResult<LogoutResponse>,
    logoutViewModel: LogoutViewModel,
    context: Context,
    homeActivity: HomeActivity,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            logoutViewModel.isLoading = true
            logoutViewModel.openLogoutDialog = false
        }
        is NetworkResult.Success -> {
            // bind data to the view
            logoutViewModel.isLoading = false
            logoutViewModel.logOutSuccessMessage = result.data!!.message
            PreferenceProvider(context).setValue(Constants.USER_LOGOUT, true)
            val intent = Intent(homeActivity, MainActivity::class.java)
            context.startActivity(intent)
            context
                .findActivity()
                ?.finish()
            PreferenceProvider(context).clear()
            PreferenceProvider(context).setValue(Constants.IS_INTRO_DONE, true)
            PreferenceProvider(appContext = context).setValue(
                Constants.NOTIFICATION_COUNT,
                "0"
            )
        }
        is NetworkResult.Error -> {
            //show error message
            logoutViewModel.isLoading = false
            logoutViewModel.openLogoutDialog = true
        }
    }
}

package com.biggestAsk.ui.homeScreen.drawerScreens.settingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biggestAsk.data.model.response.GetPrivacyPolicyResponse
import com.biggestAsk.data.source.network.NetworkResult
import com.biggestAsk.ui.activity.HomeActivity
import com.biggestAsk.ui.emailVerification.ProgressBarTransparentBackground
import com.biggestAsk.ui.main.viewmodel.PrivacyPolicyViewModel
import com.example.biggestAsk.R

@Composable
fun PrivacyPolicy(
    homeActivity: HomeActivity,
    privacyPolicyViewModel: PrivacyPolicyViewModel,
) {
    LaunchedEffect(Unit) {
        getPrivacyPolicy(privacyPolicyViewModel, homeActivity)
    }
    if (privacyPolicyViewModel.isLoading) {
        privacyPolicyViewModel.privacyPolicyList.clear()
        ProgressBarTransparentBackground(stringResource(id = R.string.loading))
    } else
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 55.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp),
                    painter = painterResource(id = R.drawable.logo_setting_privacy_policy),
                    contentDescription = ""
                )
                Text(
                    text = if (privacyPolicyViewModel.lastUpdatedDate != "") "Last Updated on ${privacyPolicyViewModel.lastUpdatedDate}" else "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 29.dp),
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W400
                    ),
                )
                privacyPolicyViewModel.privacyPolicyList.forEach { item ->
                    if (privacyPolicyViewModel.privacyPolicyList.isNotEmpty()) {
                        item.title?.let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp, start = 20.dp),
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Start,
                                    fontSize = 22.sp,
                                    color = Color.Black
                                )
                            )
                        }
                        item.info?.let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp, start = 20.dp),
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.W400,
                                    textAlign = TextAlign.Start,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = Color(0xFF9B9BA8)
                                )
                            )
                        }
                    }
                }
            }
        }
}

fun getPrivacyPolicy(
    privacyPolicyViewModel: PrivacyPolicyViewModel,
    homeActivity: HomeActivity,
) {
    privacyPolicyViewModel.getPrivacyPolicy()

    privacyPolicyViewModel.getPrivacyPolicyResponse.observe(homeActivity) {
        if (it != null) {
            handlePrivacyPolicyApi(
                result = it,
                privacyPolicyViewModel = privacyPolicyViewModel
            )
        }
    }
}

private fun handlePrivacyPolicyApi(
    result: NetworkResult<GetPrivacyPolicyResponse>,
    privacyPolicyViewModel: PrivacyPolicyViewModel,
) {
    when (result) {
        is NetworkResult.Loading -> {
            // show a progress bar
            privacyPolicyViewModel.isLoading = true
            privacyPolicyViewModel.isDataNull = false
            privacyPolicyViewModel.privacyPolicyList.clear()
        }
        is NetworkResult.Success -> {
            // bind data to the view
            privacyPolicyViewModel.isLoading = false
            privacyPolicyViewModel.privacyPolicyList =
                result.data!!.privacy_policy.toMutableStateList()
            privacyPolicyViewModel.lastUpdatedDate = result.data.privacy_policy_date
            privacyPolicyViewModel.isDataNull = privacyPolicyViewModel.privacyPolicyList.isEmpty()
        }
        is NetworkResult.Error -> {
            //show error message
            privacyPolicyViewModel.isLoading = false
        }
    }
}